package ssl.editors.scriptslist;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import fdk.lst.ScriptLstMaker;
import fdk.msg.MSG;
import fdk.msg.MsgEntry;

public class SCRNameColumnLabelProvider extends ColumnLabelProvider {

    private MSG m_msg;

    public SCRNameColumnLabelProvider(MSG msg) {
        m_msg = msg;
    }

    @Override
    public String getText(Object element) {
        ScriptLstMaker.Entry ent = (ScriptLstMaker.Entry) element;
        int index = ent.getIndex();

        MsgEntry msg = m_msg.get(index + 100);
        if (msg != null) {
            return msg.getMsg();
        } else {
            return "";
        }
    }
}
