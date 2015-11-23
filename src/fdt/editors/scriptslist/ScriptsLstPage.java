package fdt.editors.scriptslist;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.*;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.ui.part.FileEditorInput;

import com.swtdesigner.ResourceManager;

import fdk.lst.*;
import fdk.msg.*;
import fdt.Fdt;
import fdt.wizards.NewScriptWizard;

public class ScriptsLstPage extends FormPage {

	private IProject project;
	private FilteredTree scriptsViewer;
	private boolean lstDirty = false;
	private boolean msgDirty = false;

	private MSG scrnameMsg = null;
	private LST scriptsLst = null;

	public ScriptsLstPage(String id, String title) {
		super(id, title);
	}

	public ScriptsLstPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		project = ((FileEditorInput) getEditorInput()).getFile().getProject();

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Scripts List");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		body.setLayout(new GridLayout(1, false));

		Section sctnScriptslst = managedForm.getToolkit().createSection(body, Section.COMPACT | Section.EXPANDED | Section.DESCRIPTION | Section.TITLE_BAR);
		sctnScriptslst.setDescription("Shows all registread scripts");
		sctnScriptslst.setLayoutData(new GridData(GridData.FILL_BOTH));
		sctnScriptslst.setText("Scripts.lst");
		managedForm.getToolkit().paintBordersFor(sctnScriptslst);

		Composite composite = managedForm.getToolkit().createComposite(sctnScriptslst, SWT.NONE);
		sctnScriptslst.setClient(composite);
		managedForm.getToolkit().paintBordersFor(composite);

		composite.setLayout(new GridLayout(2, false));

		try {
			Charset cs = Charset.forName(project.getDefaultCharset());
			scriptsLst = new LST(Fdt.getFile(project, "scripts/SCRIPTS.LST").getContents(), cs, new ScriptLstMaker());

			IFile SCRNameMsg = Fdt.getFile(project, "text/english/game/scrname.msg");
			scrnameMsg = new MSG(SCRNameMsg.getContents(), cs);

		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
		}

		scriptsViewer = new FilteredTree(composite, SWT.BORDER | SWT.FULL_SELECTION, new PatternFilter(), true);
		scriptsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		final Tree tree = scriptsViewer.getViewer().getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		managedForm.getToolkit().paintBordersFor(tree);

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(scriptsViewer.getViewer(), SWT.NONE);
		treeViewerColumn.setLabelProvider(new ScriptListLabelProvider(0));
		TreeColumn trclmnNumber = treeViewerColumn.getColumn();
		trclmnNumber.setWidth(70);
		trclmnNumber.setText("Index");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(scriptsViewer.getViewer(), SWT.NONE);
		treeViewerColumn_1.setLabelProvider(new ScriptListLabelProvider(1));
		treeViewerColumn_1.setEditingSupport(new ScriptEntryEditor(scriptsViewer.getViewer(), this, ScriptEntryEditor.NAME));
		TreeColumn trclmnName = treeViewerColumn_1.getColumn();
		trclmnName.setWidth(100);
		trclmnName.setText("Name");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(scriptsViewer.getViewer(), SWT.NONE);
		treeViewerColumn_2.setLabelProvider(new ScriptListLabelProvider(2));
		treeViewerColumn_2.setEditingSupport(new ScriptEntryEditor(scriptsViewer.getViewer(), this, ScriptEntryEditor.DESCRIPTION));
		TreeColumn trclmnDescripton = treeViewerColumn_2.getColumn();
		trclmnDescripton.setWidth(258);
		trclmnDescripton.setText("Descripton");

		TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(scriptsViewer.getViewer(), SWT.NONE);
		TreeColumn trclmnLocalVars = treeViewerColumn_4.getColumn();
		treeViewerColumn_4.setLabelProvider(new ScriptListLabelProvider(3));
		treeViewerColumn_4.setEditingSupport(new ScriptEntryEditor(scriptsViewer.getViewer(), this, ScriptEntryEditor.LOCALVARS));
		trclmnLocalVars.setWidth(70);
		trclmnLocalVars.setText("Local Vars");

		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(scriptsViewer.getViewer(), SWT.NONE);
		treeViewerColumn_3.setLabelProvider(new SCRNameColumnLabelProvider(scrnameMsg));
		treeViewerColumn_3.setEditingSupport(new SCRNameEditor(scriptsViewer.getViewer(), this, scrnameMsg));
		TreeColumn trclmnScrName = treeViewerColumn_3.getColumn();
		trclmnScrName.setWidth(153);
		trclmnScrName.setText("SCR Name");

		// //////////////////////////////////
		getSite().setSelectionProvider(scriptsViewer.getViewer());
		MenuManager contextMenu = new MenuManager() {
			@Override
			public IContributionItem[] getItems() {

				IContributionItem[] items = super.getItems();
				List<IContributionItem> filteredItems = new ArrayList<IContributionItem>();
				for (IContributionItem item : items) {
					if (item != null && item.getId() != null && item.getId().startsWith("FDT"))
						filteredItems.add(item);
				}

				items = new IContributionItem[filteredItems.size()];
				return filteredItems.toArray(items);
			}
		};
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.removeAll();

		getSite().registerContextMenu(contextMenu, scriptsViewer.getViewer());
		Control control = scriptsViewer.getViewer().getControl();
		final Menu menu1 = contextMenu.createContextMenu(control);
		control.setMenu(menu1);

		// ///////////////////////

		scriptsViewer.getViewer().setContentProvider(new ScriptsListContentProvider());
		scriptsViewer.getViewer().setInput(scriptsLst);

		Button button = managedForm.getToolkit().createButton(composite, "New script...", SWT.NONE);
		button.setImage(ResourceManager.getPluginImage("SSL", "icons/add.gif"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewScriptWizard wizard = new NewScriptWizard();
				IStructuredSelection currentSelection = new StructuredSelection(project.getFolder("src"));
				wizard.init(PlatformUI.getWorkbench(), currentSelection);
				WizardDialog dialog = new WizardDialog(ScriptsLstPage.this.getSite().getShell(), wizard);

				if (dialog.open() == WizardDialog.OK) {
					String content = wizard.getFile().replaceAll("\\.ssl$", ".int");
					while (content.length() < 16)
						content += " ";
					content += ";                                               # local_vars=0";
					scriptsLst.add(new ScriptLstMaker.Entry(content, 0));
					scriptsViewer.getViewer().setInput(scriptsLst);
					scriptsViewer.getViewer().reveal(scriptsLst.get(scriptsLst.size() - 1));
					setLstDirty(true);
				}
			}
		});
		button.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

		Button btnDeleteLast = new Button(composite, SWT.NONE);
		btnDeleteLast.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox mb = new MessageBox(getSite().getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setText("Deleting script entry");
				mb.setMessage("Really delete " + ((ScriptLstMaker.Entry) scriptsLst.get(scriptsLst.size() - 1)).getName() + "?");

				if (mb.open() == SWT.YES) {
					scriptsLst.remove(scriptsLst.size() - 1);
					scriptsViewer.getViewer().setInput(scriptsLst);
					scriptsViewer.getViewer().reveal(scriptsLst.get(scriptsLst.size() - 1));
					setLstDirty(true);

					if (scrnameMsg.get(scriptsLst.size() + 101) != null) {
						scrnameMsg.remove(scriptsLst.size() + 101);
						setMsgDirty(true);
					}
				}

			}
		});
		btnDeleteLast.setImage(ResourceManager.getPluginImage("SSL", "icons/trash.gif"));
		btnDeleteLast.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		managedForm.getToolkit().adapt(btnDeleteLast, true, true);
		btnDeleteLast.setText("Delete last");
	}

	@Override
	public boolean isDirty() {
		return lstDirty || msgDirty;
	}

	public void setMsgDirty(boolean dirty) {
		msgDirty = dirty;
		if (msgDirty)
			((ScriptsList) getEditor()).setDirty();
	}

	public void setLstDirty(boolean dirty) {
		lstDirty = dirty;
		if (lstDirty)
			((ScriptsList) getEditor()).setDirty();
	}

	protected void saveLst(IProgressMonitor monitor) throws CoreException, IOException {
		Charset cs = Charset.forName(project.getDefaultCharset());
		IFolder fld = Fdt.getFolderForce(project, "scripts");
		File out = new File(fld.getLocation().toFile(), "scripts.lst");
		LST slst = (LST) scriptsViewer.getViewer().getInput();
		slst.save(new FileOutputStream(out), cs);

		// for refreshing
		Fdt.getFolder(project, "/scripts").getFile("scripts.lst").refreshLocal(IResource.DEPTH_ONE, monitor);
		lstDirty = false;
	}

	protected void saveMsg(IProgressMonitor monitor) throws CoreException, WrongMsgEncoding, FileNotFoundException, IOException {
		Charset cs = Charset.forName(project.getDefaultCharset());
		IFolder fld = Fdt.getFolderForce(project, "text/english/game");
		File out = new File(fld.getLocation().toFile(), "scrname.msg");
		scrnameMsg.write(new FileOutputStream(out), cs);

		// for refreshing
		Fdt.getFolder(project, "/text/english/game").getFile("scrname.msg").refreshLocal(IResource.DEPTH_ONE, monitor);
		msgDirty = false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			if (lstDirty)
				saveLst(monitor);
			if (msgDirty)
				saveMsg(monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.doSave(monitor);
	}
}
