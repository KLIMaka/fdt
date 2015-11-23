package fdt.editors.scriptslist;

import org.eclipse.jface.viewers.*;

import fdk.lst.ScriptLstMaker;
import fdk.msg.*;

public class SCRNameEditor extends EditingSupport {

	private TreeViewer m_parent;
	private ScriptsLstPage m_page;
	private MSG m_msg;

	public SCRNameEditor(ColumnViewer viewer, ScriptsLstPage page, MSG msg) {
		super(viewer);
		m_parent = (TreeViewer) viewer;
		m_page = page;
		m_msg = msg;
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
		int index = ent.getIndex();

		MsgEntry msg = m_msg.get(index + 100);
		if (msg != null)
			return msg.getMsg();
		else
			return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		boolean f = false;
		ScriptLstMaker.Entry ent = (ScriptLstMaker.Entry) element;
		int index = ent.getIndex() + 100;
		MsgEntry msg = m_msg.get(index);

		if (msg != null) {
			if (!msg.getMsg().equals(value)) {
				msg.setMsg(value.toString());
				f = true;
			}
		} else {
			m_msg.put(new MsgEntry(index, "", value.toString(), "", ""));
			f = true;
		}

		m_page.setMsgDirty(f);
		m_parent.refresh(element);
	}
}
