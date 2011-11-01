package ssl.editors.scriptslist;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

import fdk.lst.ScriptLstMaker;

public class ScriptEntryEditor extends EditingSupport {

    private TreeViewer      m_parent;
    private ScriptsLstPage  m_page;
    private int             m_mode;

    public static final int NAME        = 0;
    public static final int DESCRIPTION = 1;
    public static final int LOCALVARS   = 2;

    public ScriptEntryEditor(ColumnViewer viewer, ScriptsLstPage page, int mode) {
        super(viewer);
        m_parent = (TreeViewer) viewer;
        m_page = page;
        m_mode = mode;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return new TextCellEditor(m_parent.getTree());
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        ScriptLstMaker.Entry ent = (ScriptLstMaker.Entry) element;
        switch (m_mode) {
        case NAME:
            return ent.getName();
        case DESCRIPTION:
            return ent.getComment();
        case LOCALVARS:
            return String.valueOf(ent.getLocalVars());
        default:
            return null;
        }
    }

    @Override
    protected void setValue(Object element, Object value) {
        ScriptLstMaker.Entry ent = (ScriptLstMaker.Entry) element;
        boolean f = false;

        switch (m_mode) {
        case NAME:
            if (!ent.getName().equals(value.toString())) {
                ent.setName(value.toString());
                f = true;
            }
            break;
        case DESCRIPTION:
            if (!ent.getComment().equals(value)) {
                ent.setComment(value.toString());
                f = true;
            }
            break;
        case LOCALVARS:
            try {
                if (ent.getLocalVars() != Integer.parseInt(value.toString())) {
                    ent.setLocalVars(Integer.parseInt(value.toString()));
                    f = true;
                }
            } catch (NumberFormatException e) {}
        }

        m_page.setLstDirty(f);
        m_parent.refresh(element);
    }
}
