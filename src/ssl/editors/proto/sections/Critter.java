package ssl.editors.proto.sections;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ssl.SslPlugin;
import ssl.editors.proto.CategoryParser;
import ssl.editors.proto.CategoryParser.Category;
import ssl.editors.proto.CategoryParser.Field;
import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.ProtoAdaptorsFactory;
import ssl.editors.proto.Ref;
import fdk.msg.MSG;
import fdk.msg.WrongMsgEncoding;
import fdk.proto.Prototype;

public class Critter extends Composite implements IFillSection {

    private static class StatContentProvider implements ITreeContentProvider {
        private Category m_cat;

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            m_cat = (Category) newInput;
        }

        public void dispose() {}

        public Object[] getElements(Object inputElement) {
            return m_cat.getMembers();
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Category) return ((Category) parentElement).getMembers();
            return null;
        }

        public Object getParent(Object element) {
            return null;
        }

        public boolean hasChildren(Object element) {
            return element instanceof Category;
        }
    }

    private static class SkillContentProvider implements ITreeContentProvider {
        private Skill[] m_skillz;

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            m_skillz = (Skill[]) newInput;
        }

        public void dispose() {}

        public Object[] getElements(Object inputElement) {
            return m_skillz;
        }

        public Object[] getChildren(Object parentElement) {
            return null;
        }

        public Object getParent(Object element) {
            return null;
        }

        public boolean hasChildren(Object element) {
            return false;
        }
    }

    private class SkillEditor extends EditingSupport {

        public SkillEditor(ColumnViewer viewer) {
            super(viewer);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new TextCellEditor(((TreeViewer) getViewer()).getTree());
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            Skill skill = (Skill) element;
            return m_proto.get().getFields().get(skill.id).toString();
        }

        @Override
        protected void setValue(Object element, Object value) {
            Skill skill = (Skill) element;
            try {
                Integer val = Integer.valueOf(value.toString());
                if (!val.equals(m_proto.get().getFields().get(skill.id))) {
                    m_proto.get().getFields().put(skill.id, val);
                    getViewer().refresh(element);
                    m_changeListener.change();
                }
            } catch (Exception e) {}
        }
    }

    private class StatEditor extends EditingSupport {

        public static final int BASE = 0;
        public static final int PLUS = 1;

        private int             m_mode;

        public StatEditor(ColumnViewer viewer, int mode) {
            super(viewer);
            m_mode = mode;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new TextCellEditor(((TreeViewer) getViewer()).getTree());
        }

        @Override
        protected boolean canEdit(Object element) {
            return element instanceof Field;
        }

        @Override
        protected Object getValue(Object element) {
            Field stat = (Field) element;
            String name = stat.getName().concat(m_mode == PLUS ? "+" : "");
            return m_proto.get().getFields().get(name).toString();
        }

        @Override
        protected void setValue(Object element, Object value) {
            Field stat = (Field) element;
            String name = stat.getName().concat(m_mode == PLUS ? "+" : "");
            try {
                Integer val = Integer.valueOf(value.toString());
                if (!val.equals(m_proto.get().getFields().get(stat.getName()))) {
                    m_proto.get().getFields().put(name, val);
                    getViewer().refresh(element);
                    m_changeListener.change();
                }
            } catch (Exception e) {}
        }
    }

    private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
    private Tree              m_stats1;

    private Ref<Prototype>    m_proto;
    private IChangeListener   m_changeListener;
    private TreeViewer        m_skills;
    private TreeViewer        m_stats;
    private MSG               m_msg;
    private IProject          m_proj;

    private static class Skill {
        public String name;
        public String id;

        public Skill(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public Critter(Composite parent, ProtoAdaptorsFactory fact) {
        super(parent, SWT.NONE);
        m_proto = fact.getPro();
        m_changeListener = fact.getChangeListener();
        m_proj = fact.getProject();
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(2, false));

        Group grpStats = new Group(this, SWT.NONE);
        grpStats.setLayout(new GridLayout(1, false));
        GridData gd_grpStats = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_grpStats.heightHint = 145;
        grpStats.setLayoutData(gd_grpStats);
        grpStats.setText("Stats");
        m_toolkit.adapt(grpStats);
        m_toolkit.paintBordersFor(grpStats);

        m_stats = new TreeViewer(grpStats, SWT.BORDER | SWT.FULL_SELECTION);
        m_stats1 = m_stats.getTree();
        m_stats1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        m_stats1.setHeaderVisible(true);
        m_stats1.setLinesVisible(true);
        m_toolkit.paintBordersFor(m_stats1);

        TreeViewerColumn treeViewerColumn = new TreeViewerColumn(m_stats, SWT.NONE);
        treeViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                if (element instanceof Category) return ((Category) element).getName();
                if (element instanceof Field) return m_msg.get(((Field) element).getDescr()).getMsg();
                return null;
            }
        });
        TreeColumn trclmnName = treeViewerColumn.getColumn();
        trclmnName.setWidth(130);
        trclmnName.setText("Name");

        TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(m_stats, SWT.NONE);
        treeViewerColumn_1.setEditingSupport(new StatEditor(m_stats, StatEditor.BASE));
        treeViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                if (element instanceof Field && m_proto.get() != null) {
                    return m_proto.get().getFields().get(((Field) element).getName()).toString();
                }

                return null;
            }
        });
        TreeColumn trclmnBase = treeViewerColumn_1.getColumn();
        trclmnBase.setWidth(50);
        trclmnBase.setText("Base");

        TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(m_stats, SWT.NONE);
        treeViewerColumn_2.setEditingSupport(new StatEditor(m_stats, StatEditor.PLUS));
        treeViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                if (element instanceof Field && m_proto.get() != null) {
                    return m_proto.get().getFields().get(((Field) element).getName() + "+").toString();
                }

                return null;
            }
        });
        TreeColumn trclmnPlus = treeViewerColumn_2.getColumn();
        trclmnPlus.setWidth(50);
        trclmnPlus.setText("Plus");

        TreeViewerColumn treeViewerColumn_5 = new TreeViewerColumn(m_stats, SWT.NONE);
        treeViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {

                if (element instanceof Field && m_proto.get() != null) {
                    int p = (Integer) m_proto.get().getFields().get(((Field) element).getName() + "+");
                    int b = (Integer) m_proto.get().getFields().get(((Field) element).getName());
                    return String.valueOf(p + b);
                }

                return null;
            }
        });
        TreeColumn trclmnSum = treeViewerColumn_5.getColumn();
        trclmnSum.setWidth(50);
        trclmnSum.setText("Sum");
        m_stats.setContentProvider(new StatContentProvider());

        Group grpSkills = new Group(this, SWT.NONE);
        grpSkills.setLayout(new GridLayout(1, false));
        GridData gd_grpSkills = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_grpSkills.heightHint = 189;
        grpSkills.setLayoutData(gd_grpSkills);
        grpSkills.setText("Skills");
        m_toolkit.adapt(grpSkills);
        m_toolkit.paintBordersFor(grpSkills);

        m_skills = new TreeViewer(grpSkills, SWT.BORDER | SWT.FULL_SELECTION);
        Tree skills1 = m_skills.getTree();
        skills1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        skills1.setLinesVisible(true);
        skills1.setHeaderVisible(true);
        m_toolkit.paintBordersFor(skills1);

        TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(m_skills, SWT.NONE);
        treeViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                return ((Skill) element).name;
            }
        });
        TreeColumn trclmnName_1 = treeViewerColumn_3.getColumn();
        trclmnName_1.setWidth(130);
        trclmnName_1.setText("Name");

        TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(m_skills, SWT.NONE);
        treeViewerColumn_4.setEditingSupport(new SkillEditor(m_skills));
        treeViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                if (m_proto.get() == null) return null;
                return m_proto.get().getFields().get(((Skill) element).id).toString() + "%";
            }
        });
        TreeColumn trclmnValue = treeViewerColumn_4.getColumn();
        trclmnValue.setWidth(50);
        trclmnValue.setText("Value");
        m_skills.setContentProvider(new SkillContentProvider());

    }

    private void fillSkills() throws WrongMsgEncoding, IOException, CoreException {
        MSG skill_msg = SslPlugin.getCachedMsg(m_proj, "text/english/game/skill.msg");
        Skill[] skills = new Skill[18];

        skills[0] = new Skill(skill_msg.get(100).getMsg(), "smallGuns");
        skills[1] = new Skill(skill_msg.get(101).getMsg(), "bigGuns");
        skills[2] = new Skill(skill_msg.get(102).getMsg(), "energyWeapons");
        skills[3] = new Skill(skill_msg.get(103).getMsg(), "unarmed");
        skills[4] = new Skill(skill_msg.get(104).getMsg(), "melee");
        skills[5] = new Skill(skill_msg.get(105).getMsg(), "throwing");
        skills[6] = new Skill(skill_msg.get(106).getMsg(), "firsAid");
        skills[7] = new Skill(skill_msg.get(107).getMsg(), "doctor");
        skills[8] = new Skill(skill_msg.get(108).getMsg(), "sneak");
        skills[9] = new Skill(skill_msg.get(109).getMsg(), "lockpick");
        skills[10] = new Skill(skill_msg.get(110).getMsg(), "steal");
        skills[11] = new Skill(skill_msg.get(111).getMsg(), "traps");
        skills[12] = new Skill(skill_msg.get(112).getMsg(), "science");
        skills[13] = new Skill(skill_msg.get(113).getMsg(), "repair");
        skills[14] = new Skill(skill_msg.get(114).getMsg(), "speech");
        skills[15] = new Skill(skill_msg.get(115).getMsg(), "barter");
        skills[16] = new Skill(skill_msg.get(116).getMsg(), "gambling");
        skills[17] = new Skill(skill_msg.get(117).getMsg(), "outdoorsman");

        m_skills.setInput(skills);
    }

    private void fillStats() throws WrongMsgEncoding, IOException, CoreException {
        m_msg = SslPlugin.getCachedMsg(m_proj, "text/english/game/stat.msg");
        Map<String, Category> cats = CategoryParser.getCategory(getClass().getResourceAsStream("protocat.xml"));
        m_stats.setInput(cats.get("Critter"));
    }

    public void fill(Ref<Prototype> proto, IProject proj) {
        m_skills.refresh();
        m_stats.refresh();
    }

    @Override
    public void setup() throws Exception {
        fillSkills();
        fillStats();
    }

    @Override
    public Control toControl() {
        return this;
    }

}
