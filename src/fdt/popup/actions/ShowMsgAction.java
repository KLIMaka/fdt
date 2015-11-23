package fdt.popup.actions;

import org.eclipse.core.resources.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import fdt.Fdt;
import fdt.adapters.ScriptAdapter;

public class ShowMsgAction extends LSTAction {

	private IWorkbenchPage m_page;
	private IProject m_proj;
	private ScriptAdapter m_item;

	public ShowMsgAction() {
	}

	protected IFile getFile() {
		return Fdt.getFile(m_proj, "text/english/dialog/" + m_item.getName().replaceAll("\\.[Ii][Nn][Tt]$", ".MSG"));
	}

	@Override
	public void run(IAction action) {
		IFile file = getFile();

		if (file != null) {
			try {
				IDE.openEditor(m_page, file);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setProject(IProject proj, IWorkbenchPage page) {
		m_page = page;
		m_proj = proj;
	}

	@Override
	public void select(IAction action, ScriptAdapter lstent) {
		action.setEnabled(false);
		m_item = lstent;

		if (m_item != null) {
			m_proj = m_proj == null ? m_item.getProject() : m_proj;
			if (getFile() != null)
				action.setEnabled(true);
		}
	}
}
