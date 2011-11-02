package fdt.editors.scriptslist;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import fdk.lst.ScriptLstMaker;

public class ScriptListLabelProvider extends ColumnLabelProvider {
    public static final int NUMBER      = 0;
    public static final int NAME        = 1;
    public static final int DESCRIPTION = 2;
    public static final int LOCALVARS   = 3;

    private int             m_type;

    public ScriptListLabelProvider(int type) {
        m_type = type;
    }

    @Override
    public String getText(Object element) {
        ScriptLstMaker.Entry ent = (ScriptLstMaker.Entry) element;

        switch (m_type) {
        case NUMBER:
            return String.valueOf(ent.getIndex());
        case NAME:
            return ent.getName();
        case DESCRIPTION:
            return ent.getComment();
        case LOCALVARS:
            return String.valueOf(ent.getLocalVars());
        }
        return null;
    }
}
