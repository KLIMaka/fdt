package fdt.editors.proto.sections;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;

import fdk.msg.*;
import fdk.proto.Prototype;
import fdt.Fdt;
import fdt.editors.proto.*;
import fdt.editors.proto.CategoryParser.*;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.util.Ref;

public class Critter extends Composite implements IFillSection {

	private static class StatContentProvider implements ITreeContentProvider {
		private Category m_cat;

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			m_cat = (Category) newInput;
		}

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			return m_cat.getMembers();
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Category)
				return ((Category) parentElement).getMembers();
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

		public void dispose() {
		}

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
			return m_proto.get().get(skill.id);
		}

		@Override
		protected void setValue(Object element, Object value) {
			Skill skill = (Skill) element;
			try {
				Integer val = Integer.valueOf(value.toString());
				if (!val.equals(m_proto.get().get(skill.id))) {
					m_proto.get().set(skill.id, val);
					getViewer().refresh(element);
					m_changeListener.change();
				}
			} catch (Exception e) {
			}
		}
	}

	private class StatEditor extends EditingSupport {

		public static final int BASE = 0;
		public static final int PLUS = 1;

		private int m_mode;

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
			int name = stat.getName() + (m_mode == PLUS ? 140 : 0);
			return m_proto.get().get(name);
		}

		@Override
		protected void setValue(Object element, Object value) {
			Field stat = (Field) element;
			int name = stat.getName() + (m_mode == PLUS ? 140 : 0);
			try {
				Integer val = Integer.valueOf(value.toString());
				if (!val.equals(m_proto.get().get(stat.getName()))) {
					m_proto.get().set(name, val);
					getViewer().refresh(element);
					m_changeListener.change();
				}
			} catch (Exception e) {
			}
		}
	}

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Tree m_stats1;

	private Ref<Prototype> m_proto;
	private IChangeListener m_changeListener;
	private TreeViewer m_skills;
	private TreeViewer m_stats;
	private MSG m_msg;
	private IProject m_proj;

	private static class Skill {
		public String name;
		public int id;

		public Skill(String name, int id) {
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
				if (element instanceof Category)
					return ((Category) element).getName();
				if (element instanceof Field)
					return m_msg.get(((Field) element).getDescr()).getMsg();
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
					return String.valueOf(m_proto.get().get(((Field) element).getName()));
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
					return String.valueOf(m_proto.get().get(((Field) element).getName() + 140));
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
					int p = m_proto.get().get(((Field) element).getName() + 140);
					int b = m_proto.get().get(((Field) element).getName());
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
				if (m_proto.get() == null)
					return null;
				return m_proto.get().get(((Skill) element).id) + "%";
			}
		});
		TreeColumn trclmnValue = treeViewerColumn_4.getColumn();
		trclmnValue.setWidth(50);
		trclmnValue.setText("Value");
		m_skills.setContentProvider(new SkillContentProvider());

	}

	private void fillSkills() throws WrongMsgEncoding, IOException, CoreException {
		MSG skill_msg = Fdt.getCachedMsg(m_proj, "text/english/game/skill.msg");
		Skill[] skills = new Skill[18];

		skills[0] = new Skill(skill_msg.get(100).getMsg(), Prototype.SMALL_GUNS);
		skills[1] = new Skill(skill_msg.get(101).getMsg(), Prototype.BIG_GUNS);
		skills[2] = new Skill(skill_msg.get(102).getMsg(), Prototype.ENERG_WEP);
		skills[3] = new Skill(skill_msg.get(103).getMsg(), Prototype.UNARMED);
		skills[4] = new Skill(skill_msg.get(104).getMsg(), Prototype.MELEE);
		skills[5] = new Skill(skill_msg.get(105).getMsg(), Prototype.THROWING);
		skills[6] = new Skill(skill_msg.get(106).getMsg(), Prototype.FIRST_AID);
		skills[7] = new Skill(skill_msg.get(107).getMsg(), Prototype.DOCTOR);
		skills[8] = new Skill(skill_msg.get(108).getMsg(), Prototype.SNEAK);
		skills[9] = new Skill(skill_msg.get(109).getMsg(), Prototype.LOCKPIÑK);
		skills[10] = new Skill(skill_msg.get(110).getMsg(), Prototype.STEAL);
		skills[11] = new Skill(skill_msg.get(111).getMsg(), Prototype.TRAPS);
		skills[12] = new Skill(skill_msg.get(112).getMsg(), Prototype.SCIENCE);
		skills[13] = new Skill(skill_msg.get(113).getMsg(), Prototype.REPAIR);
		skills[14] = new Skill(skill_msg.get(114).getMsg(), Prototype.SPEECH);
		skills[15] = new Skill(skill_msg.get(115).getMsg(), Prototype.BARTER);
		skills[16] = new Skill(skill_msg.get(116).getMsg(), Prototype.GAMBLING);
		skills[17] = new Skill(skill_msg.get(117).getMsg(), Prototype.OUTDOORSMAN);

		m_skills.setInput(skills);
	}

	private void fillStats() throws WrongMsgEncoding, IOException, CoreException {
		m_msg = Fdt.getCachedMsg(m_proj, "text/english/game/stat.msg");
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
