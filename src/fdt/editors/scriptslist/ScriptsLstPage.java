package fdt.editors.scriptslist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;

import com.swtdesigner.ResourceManager;

import fdk.lst.LST;
import fdk.lst.ScriptLstMaker;
import fdk.msg.MSG;
import fdk.msg.WrongMsgEncoding;
import fdt.FDT;
import fdt.wizards.NewScriptWizard;

public class ScriptsLstPage extends FormPage {

    private IProject     m_project;
    private FilteredTree m_scriptsViewer;
    private boolean      m_LstDirty   = false;
    private boolean      m_MsgDirty   = false;

    private MSG          m_scrnameMsg = null;
    private LST          m_scriptsLst = null;

    /**
     * Create the form page.
     * 
     * @param id
     * @param title
     */
    public ScriptsLstPage(String id, String title) {
        super(id, title);
    }

    /**
     * Create the form page.
     * 
     * @param editor
     * @param id
     * @param title
     * @wbp.parser.constructor
     * @wbp.eval.method.parameter id "Some id"
     * @wbp.eval.method.parameter title "Some title"
     */
    public ScriptsLstPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /**
     * Create contents of the form.
     * 
     * @param managedForm
     */
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        // managedForm.getForm().setDelayedReflow(true);
        m_project = ((FileEditorInput) getEditorInput()).getFile().getProject();

        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Scripts List");
        Composite body = form.getBody();
        toolkit.decorateFormHeading(form.getForm());
        toolkit.paintBordersFor(body);
        managedForm.getForm().getBody().setLayout(new GridLayout(1, false));

        Section sctnScriptslst = managedForm.getToolkit().createSection(managedForm.getForm().getBody(),
                Section.COMPACT | Section.EXPANDED | Section.DESCRIPTION | Section.TITLE_BAR);
        sctnScriptslst.setDescription("Shows all registread scripts");
        sctnScriptslst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        managedForm.getToolkit().paintBordersFor(sctnScriptslst);
        sctnScriptslst.setText("Scripts.lst");

        Composite composite = managedForm.getToolkit().createComposite(sctnScriptslst, SWT.NONE);
        managedForm.getToolkit().paintBordersFor(composite);
        sctnScriptslst.setClient(composite);

        composite.setLayout(new GridLayout(2, false));

        try {
            IProject project = ((FileEditorInput) getEditorInput()).getFile().getProject();

            Charset cs = Charset.forName(m_project.getDefaultCharset());
            m_scriptsLst = new LST(FDT.getFile(project, "scripts/SCRIPTS.LST").getContents(), cs,
                    new ScriptLstMaker());

            IFile SCRNameMsg = FDT.getFile(project, "text/english/game/scrname.msg");
            m_scrnameMsg = new MSG(SCRNameMsg.getContents(), cs);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }

        m_scriptsViewer = new FilteredTree(composite, SWT.BORDER | SWT.FULL_SELECTION, new PatternFilter(), true);
        m_scriptsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
        final Tree tree = m_scriptsViewer.getViewer().getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        managedForm.getToolkit().paintBordersFor(tree);

        TreeViewerColumn treeViewerColumn = new TreeViewerColumn(m_scriptsViewer.getViewer(), SWT.NONE);
        treeViewerColumn.setLabelProvider(new ScriptListLabelProvider(0));
        TreeColumn trclmnNumber = treeViewerColumn.getColumn();
        trclmnNumber.setWidth(70);
        trclmnNumber.setText("Index");

        TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(m_scriptsViewer.getViewer(), SWT.NONE);
        treeViewerColumn_1.setLabelProvider(new ScriptListLabelProvider(1));
        treeViewerColumn_1.setEditingSupport(new ScriptEntryEditor(m_scriptsViewer.getViewer(), this,
                ScriptEntryEditor.NAME));
        TreeColumn trclmnName = treeViewerColumn_1.getColumn();
        trclmnName.setWidth(100);
        trclmnName.setText("Name");

        TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(m_scriptsViewer.getViewer(), SWT.NONE);
        treeViewerColumn_2.setLabelProvider(new ScriptListLabelProvider(2));
        treeViewerColumn_2.setEditingSupport(new ScriptEntryEditor(m_scriptsViewer.getViewer(), this,
                ScriptEntryEditor.DESCRIPTION));
        TreeColumn trclmnDescripton = treeViewerColumn_2.getColumn();
        trclmnDescripton.setWidth(258);
        trclmnDescripton.setText("Descripton");

        TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(m_scriptsViewer.getViewer(), SWT.NONE);
        TreeColumn trclmnLocalVars = treeViewerColumn_4.getColumn();
        treeViewerColumn_4.setLabelProvider(new ScriptListLabelProvider(3));
        treeViewerColumn_4.setEditingSupport(new ScriptEntryEditor(m_scriptsViewer.getViewer(), this,
                ScriptEntryEditor.LOCALVARS));
        trclmnLocalVars.setWidth(70);
        trclmnLocalVars.setText("Local Vars");

        TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(m_scriptsViewer.getViewer(), SWT.NONE);
        treeViewerColumn_3.setLabelProvider(new SCRNameColumnLabelProvider(m_scrnameMsg));
        treeViewerColumn_3.setEditingSupport(new SCRNameEditor(m_scriptsViewer.getViewer(), this, m_scrnameMsg));
        TreeColumn trclmnScrName = treeViewerColumn_3.getColumn();
        trclmnScrName.setWidth(153);
        trclmnScrName.setText("SCR Name");

        // //////////////////////////////////
        getSite().setSelectionProvider(m_scriptsViewer.getViewer());
        MenuManager contextMenu = new MenuManager() {
            @Override
            public IContributionItem[] getItems() {

                IContributionItem[] items = super.getItems();
                List<IContributionItem> filteredItems = new ArrayList<IContributionItem>();
                for (IContributionItem item : items) {
                    if (item != null && item.getId() != null && item.getId().startsWith("SSL"))
                        filteredItems.add(item);
                }

                items = new IContributionItem[filteredItems.size()];
                return filteredItems.toArray(items);
            }
        };
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.removeAll();

        getSite().registerContextMenu(contextMenu, m_scriptsViewer.getViewer());
        Control control = m_scriptsViewer.getViewer().getControl();
        final Menu menu1 = contextMenu.createContextMenu(control);
        control.setMenu(menu1);

        // ///////////////////////

        m_scriptsViewer.getViewer().setContentProvider(new ScriptsListContentProvider());
        m_scriptsViewer.getViewer().setInput(m_scriptsLst);

        Button button = managedForm.getToolkit().createButton(composite, "New script...", SWT.NONE);
        button.setImage(ResourceManager.getPluginImage("SSL", "icons/add.gif"));
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                NewScriptWizard wizard = new NewScriptWizard();
                IStructuredSelection currentSelection = new StructuredSelection(m_project.getFolder("src"));
                wizard.init(PlatformUI.getWorkbench(), currentSelection);
                WizardDialog dialog = new WizardDialog(ScriptsLstPage.this.getSite().getShell(), wizard);

                if (dialog.open() == WizardDialog.OK) {
                    String content = wizard.getFile().replaceAll("\\.ssl$", ".int");
                    while (content.length() < 16)
                        content += " ";
                    content += ";                                               # local_vars=0";
                    m_scriptsLst.add(new ScriptLstMaker.Entry(content, 0));
                    m_scriptsViewer.getViewer().setInput(m_scriptsLst);
                    m_scriptsViewer.getViewer().reveal(m_scriptsLst.get(m_scriptsLst.size() - 1));
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
                mb.setMessage("Really delete "
                        + ((ScriptLstMaker.Entry) m_scriptsLst.get(m_scriptsLst.size() - 1)).getName() + "?");

                if (mb.open() == SWT.YES) {
                    m_scriptsLst.remove(m_scriptsLst.size() - 1);
                    m_scriptsViewer.getViewer().setInput(m_scriptsLst);
                    m_scriptsViewer.getViewer().reveal(m_scriptsLst.get(m_scriptsLst.size() - 1));
                    setLstDirty(true);

                    if (m_scrnameMsg.get(m_scriptsLst.size() + 101) != null) {
                        m_scrnameMsg.remove(m_scriptsLst.size() + 101);
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
        return m_LstDirty || m_MsgDirty;
    }

    public void setMsgDirty(boolean dirty) {
        m_MsgDirty = dirty;
        if (m_MsgDirty) ((ScriptsList) getEditor()).setDirty();
    }

    public void setLstDirty(boolean dirty) {
        m_LstDirty = dirty;
        if (m_LstDirty) ((ScriptsList) getEditor()).setDirty();
    }

    protected void saveLst(IProgressMonitor monitor) throws CoreException, IOException {
        Charset cs = Charset.forName(m_project.getDefaultCharset());
        IFolder fld = FDT.getFolderForce(m_project, "scripts");
        File out = new File(fld.getLocation().toFile(), "scripts.lst");
        LST slst = (LST) m_scriptsViewer.getViewer().getInput();
        slst.save(new FileOutputStream(out), cs);

        // for refreshing
        FDT.getFolder(m_project, "/scripts").getFile("scripts.lst").refreshLocal(IResource.DEPTH_ONE, monitor);
        m_LstDirty = false;
    }

    protected void saveMsg(IProgressMonitor monitor) throws CoreException, WrongMsgEncoding, FileNotFoundException,
            IOException {
        Charset cs = Charset.forName(m_project.getDefaultCharset());
        IFolder fld = FDT.getFolderForce(m_project, "text/english/game");
        File out = new File(fld.getLocation().toFile(), "scrname.msg");
        m_scrnameMsg.write(new FileOutputStream(out), cs);

        // for refreshing
        FDT.getFolder(m_project, "/text/english/game").getFile("scrname.msg")
                .refreshLocal(IResource.DEPTH_ONE, monitor);
        m_MsgDirty = false;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        try {
            if (m_LstDirty) saveLst(monitor);
            if (m_MsgDirty) saveMsg(monitor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.doSave(monitor);
    }
}
