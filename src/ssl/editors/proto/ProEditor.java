package ssl.editors.proto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;

import ssl.SslPlugin;
import ssl.editors.proto.sections.IFillSection;
import ssl.editors.proto.sections.sets.SetSelector;
import ssl.editors.scriptslist.ScriptsList;
import fdk.lst.BasicEntryMaker;
import fdk.lst.BasicEntryMaker.Entry;
import fdk.lst.LST;
import fdk.msg.MSG;
import fdk.proto.PRO;
import fdk.proto.PRO.Type;
import fdk.proto.Prototype;

public class ProEditor extends FormPage {
    private class ChangeTypeAction extends Action {

        private Type m_changeType;

        public ChangeTypeAction(String name, PRO.Type type) {
            super(name, AS_RADIO_BUTTON);
            m_changeType = type;
        }

        @Override
        public void run() {
            changeType(m_changeType);
        }
    }

    private Ref<LST>       m_lst      = new Ref<LST>();
    private Ref<MSG>       m_msg      = new Ref<MSG>();
    private Ref<Prototype> m_proto    = new Ref<Prototype>();
    private IProject       m_project;
    private PRO.Type       m_type;
    private String         m_protoFileName;
    private Entry          m_selectedEntry;
    private FilteredTree   m_filteredTree;
    private SetSelector    m_selector = new SetSelector();
    private Section        m_details;
    private boolean        m_dirty    = false;

    /**
     * Create the form page.
     * 
     * @param id
     * @param title
     */
    public ProEditor(String id, String title) {
        super(id, title);
    }

    private void changeType(PRO.Type type) {
        try {
            m_type = type;
            Charset cs;
            cs = Charset.forName(m_project.getDefaultCharset());
            m_lst.set(new LST(SslPlugin.getFile(m_project, PRO.getLst(m_type)).getContents(), cs, new BasicEntryMaker()));
            m_msg.set(new MSG(SslPlugin.getFile(m_project, PRO.getMsg(m_type)).getContents(), cs));
            m_filteredTree.getViewer().setInput(m_lst.get());
        } catch (Exception ex) {
            MessageBox mb = new MessageBox(getSite().getShell());
            mb.setText("Error");
            mb.setMessage(ex.getLocalizedMessage());
            mb.open();
        }
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
    public ProEditor(FormEditor editor, String id, String title) {
        super(editor, id, title);

        m_project = ((FileEditorInput) getEditor().getEditorInput()).getFile().getProject();
    }

    /**
     * Create contents of the form.
     * 
     * @param managedForm
     */
    @Override
    protected void createFormContent(IManagedForm managedForm) {

        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("PROTO Editor");
        Composite body = form.getBody();
        toolkit.decorateFormHeading(form.getForm());
        toolkit.paintBordersFor(body);
        managedForm.getForm().getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

        SashForm sashForm = new SashForm(managedForm.getForm().getBody(), SWT.SMOOTH);
        managedForm.getToolkit().adapt(sashForm);
        managedForm.getToolkit().paintBordersFor(sashForm);

        Composite composite = new Composite(sashForm, SWT.NONE);
        managedForm.getToolkit().adapt(composite);
        managedForm.getToolkit().paintBordersFor(composite);
        composite.setLayout(new GridLayout(1, false));

        Section sctnProtoList = managedForm.getToolkit().createSection(composite, Section.EXPANDED | Section.TITLE_BAR);
        sctnProtoList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        managedForm.getToolkit().paintBordersFor(sctnProtoList);
        sctnProtoList.setText("PROTO List");
        Composite composite_2 = new Composite(sctnProtoList, SWT.NONE);
        managedForm.getToolkit().adapt(composite_2);
        managedForm.getToolkit().paintBordersFor(composite_2);
        sctnProtoList.setClient(composite_2);
        composite_2.setLayout(new GridLayout(1, false));

        m_filteredTree = new FilteredTree(composite_2, SWT.BORDER | SWT.FULL_SELECTION, new PatternFilter(), true);
        Tree tree = m_filteredTree.getViewer().getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        m_filteredTree.getViewer().setContentProvider(new ProEditorContentProvider());
        managedForm.getToolkit().adapt(m_filteredTree);
        managedForm.getToolkit().paintBordersFor(m_filteredTree);
        m_filteredTree.getViewer().setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                BasicEntryMaker.Entry ent = (BasicEntryMaker.Entry) element;
                return ent.getIndex() + " " + ent.getComment() + " " + m_msg.get().get(ent.getIndex() * 100);
            }

        });

        TreeViewerColumn treeViewerColumn = new TreeViewerColumn(m_filteredTree.getViewer(), SWT.NONE);
        TreeColumn treeColumn = treeViewerColumn.getColumn();
        treeColumn.setWidth(65);
        treeColumn.setText("Number");
        treeViewerColumn.setLabelProvider(new ProColumnLabelProvider(ProColumnLabelProvider.NUMBER, null));

        TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(m_filteredTree.getViewer(), SWT.NONE);
        TreeColumn treeColumn_1 = treeViewerColumn_1.getColumn();
        treeColumn_1.setWidth(100);
        treeColumn_1.setText("PRO Name");
        treeViewerColumn_1.setLabelProvider(new ProColumnLabelProvider(ProColumnLabelProvider.PRO_NAME, null));

        TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(m_filteredTree.getViewer(), SWT.NONE);
        TreeColumn treeColumn_2 = treeViewerColumn_2.getColumn();
        treeColumn_2.setWidth(200);
        treeColumn_2.setText("Name");
        treeViewerColumn_2.setLabelProvider(new ProColumnLabelProvider(ProColumnLabelProvider.NAME, m_msg));

        m_filteredTree.getViewer().setContentProvider(new ProEditorContentProvider());
        m_filteredTree.getViewer().setInput(m_lst.get());
        m_filteredTree.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection sel = (StructuredSelection) event.getSelection();
                BasicEntryMaker.Entry ent = (BasicEntryMaker.Entry) sel.getFirstElement();
                if (ent == null) return;

                try {

                    m_dirty = false;
                    setDirty();
                    m_protoFileName = ent.getValue();
                    m_selectedEntry = ent;
                    m_proto.set(new Prototype(SslPlugin.getFile(m_project, PRO.getProDir(m_type) + m_protoFileName)
                            .getContents()));

                    IFillSection sect = m_selector.getSet(m_proto.get());
                    sect.fill(m_proto, m_project);
                    if (sect != m_details.getClient()) {
                        if (m_details.getClient() != null) m_details.getClient().setVisible(false);
                        m_details.setClient(sect.toControl());
                        m_details.getClient().setVisible(true);
                        m_details.layout();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    MessageBox mb = new MessageBox(getSite().getShell(), SWT.ERROR);
                    mb.setText("Error");
                    mb.setMessage(ex.toString());
                    mb.open();
                }

            }
        });

        // ///////////////////////////////////////////////

        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(sctnProtoList);

        toolBarManager.add(new ChangeTypeAction("Critters", Type.CRITTER));
        toolBarManager.add(new ChangeTypeAction("Scenery", Type.SCENERY));
        toolBarManager.add(new ChangeTypeAction("Items", Type.ITEM));
        toolBarManager.add(new ChangeTypeAction("Misc", Type.MISC));
        toolBarManager.add(new ChangeTypeAction("Walls", Type.WALL));

        toolBarManager.update(true);
        sctnProtoList.setTextClient(toolbar);

        // //////////////////////////////////////

        Composite composite_1 = new Composite(sashForm, SWT.NONE);
        managedForm.getToolkit().adapt(composite_1);
        managedForm.getToolkit().paintBordersFor(composite_1);
        composite_1.setLayout(new GridLayout(1, false));

        m_details = managedForm.getToolkit().createSection(composite_1, Section.EXPANDED | Section.TITLE_BAR);
        m_details.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        managedForm.getToolkit().paintBordersFor(m_details);
        m_details.setText("PROTO Details");

        // /////////////////////////////////////////////////////////

        try {
            m_selector.generateSets(m_details, toolkit, m_project, m_proto, m_msg, new IChangeListener() {
                @Override
                public void change() {
                    m_dirty = true;
                    setDirty();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDirty() {
        ((ScriptsList) getEditor()).setDirty();
    }

    @Override
    public boolean isDirty() {
        return m_dirty;
    }

    protected void saveMsg(IProgressMonitor monitor) throws CoreException, FileNotFoundException, IOException {
        Charset cs = Charset.forName(m_project.getDefaultCharset());
        String loc = PRO.getMsg(m_type);
        Matcher m = Pattern.compile("(.*)\\/(.*)").matcher(loc);
        m.find();
        String folder = m.group(1);
        String file = m.group(2);
        IFolder fld = SslPlugin.getFolderForce(m_project, folder);
        File out = new File(fld.getLocation().toFile(), file);
        m_msg.get().write(new FileOutputStream(out), cs);

        // for refreshing
        SslPlugin.getFolder(m_project, folder).getFile(file).refreshLocal(IResource.DEPTH_ONE, monitor);
    }

    protected void saveProto(IProgressMonitor monitor) throws CoreException, FileNotFoundException, IOException {
        String folder = PRO.getProDir(m_type);
        String file = m_protoFileName;
        IFolder fld = SslPlugin.getFolderForce(m_project, folder);
        File out = new File(fld.getLocation().toFile(), file);
        out.setWritable(true);
        m_proto.get().write(new FileOutputStream(out));
        out.setReadOnly();

        SslPlugin.getFolder(m_project, folder).getFile(file).refreshLocal(IResource.DEPTH_ONE, monitor);

    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        try {
            saveMsg(monitor);
            saveProto(monitor);
            m_filteredTree.getViewer().refresh(m_selectedEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.doSave(monitor);
        m_dirty = false;
    }
}
