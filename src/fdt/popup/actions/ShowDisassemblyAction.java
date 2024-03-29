package fdt.popup.actions;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.*;

import fdk.util.Utils;
import fdt.*;
import fdt.adapters.ScriptAdapter;
import fdt.preferences.PreferenceConstants;

public class ShowDisassemblyAction extends LSTAction {

	private IProject m_proj;
	private IWorkbenchPage m_page;
	private ScriptAdapter m_item;

	@Override
	public void run(IAction action) {
		try {
			SslSettingsContext ctx = new SslSettingsContext(m_proj);
			File decompiler = new File(ctx.get(PreferenceConstants.P_DECOMPILER));
			File intTmp = new File(decompiler.getParentFile(), "tmp.int");
			FileOutputStream intTmpSt = new FileOutputStream(intTmp);
			Utils.copyStrream(Fdt.getFile(m_proj, "scripts/" + m_item.getName()).getContents(), intTmpSt);
			intTmpSt.close();

			Process proc = Runtime.getRuntime().exec(decompiler + " -d tmp.int", null, decompiler.getParentFile());
			BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while (r.readLine() != null)
				;
			proc.waitFor();

			intTmp.delete();

			IStorage store = new InputStreamStorage(new FileInputStream(new File(decompiler.getParentFile(), "tmp.dump")), m_item.getName() + "<disassemble>");
			IStorageEditorInput input = new InputStreamInput(store);
			m_page.openEditor(input, "org.eclipse.ui.DefaultTextEditor");
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setProject(IProject proj, IWorkbenchPage page) {
		m_proj = proj;
		m_page = page;

	}

	@Override
	public void select(IAction action, ScriptAdapter lstent) {
		m_item = lstent;
		m_proj = m_proj == null ? (m_item == null ? null : m_item.getProject()) : m_proj;

		if (m_item != null && Fdt.getFile(m_proj, "scripts/" + m_item.getName()) != null) {
			action.setEnabled(true);
		} else
			action.setEnabled(false);
	}
}
