package fdt.editors.proto;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import fdk.lst.BasicEntryMaker;
import fdk.msg.MSG;
import fdk.msg.MsgEntry;
import fdt.util.Ref;

public class ProColumnLabelProvider extends ColumnLabelProvider {

    public static final int NUMBER   = 0;
    public static final int PRO_NAME = 1;
    public static final int NAME     = 2;

    private int             m_mode;
    private Ref<MSG>        m_msg;

    public ProColumnLabelProvider(int mode, Ref<MSG> msg) {
        m_mode = mode;
        m_msg = msg;
    }

    @Override
    public String getText(Object element) {
        BasicEntryMaker.Entry ent = (BasicEntryMaker.Entry) element;

        switch (m_mode) {
        case NUMBER:
            return String.valueOf(ent.getIndex());
        case PRO_NAME:
            return ent.getValue();
        case NAME:
            MsgEntry ment = m_msg.get().get(ent.getIndex() * 100);
            if (ment != null) return ment.getMsg();
            return "";
        }
        return null;
    }
}
