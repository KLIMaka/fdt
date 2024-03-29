package fdt.popup.actions;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import fdk.util.Utils;
import fdt.*;
import fdt.adapters.ScriptAdapter;
import fdt.preferences.PreferenceConstants;

public class DecompileAction extends LSTAction {

	private IProject m_proj;
	private IWorkbenchPage m_page;
	private ScriptAdapter m_item;

	@Override
	public void run(IAction action) {
		try {
			SslSettingsContext ctx = new SslSettingsContext(m_proj);
			File decompiler = new File(ctx.get(PreferenceConstants.P_DECOMPILER));

			// TODO move check to property page
			if (!decompiler.exists() || decompiler.isDirectory())
				return;

			IFolder fld = m_proj.getFolder("src");
			if (!fld.exists())
				fld.create(true, true, null);
			IFile file = fld.getFile(m_item.getName().replaceAll("\\.[Ii][Nn][Tt]$", ".ssl"));
			if (!file.exists()) {
				File intTmp = new File(decompiler.getParentFile(), "tmp.int");
				FileOutputStream intTmpSt = new FileOutputStream(intTmp);
				Utils.copyStrream(Fdt.getFile(m_proj, "scripts/" + m_item.getName()).getContents(), intTmpSt);
				intTmpSt.close();

				File outSsl = new File(fld.getLocation().toFile(), m_item.getName().replaceAll("\\.[Ii][Nn][Tt]$", ".ssl"));
				Process proc = Runtime.getRuntime().exec(decompiler + " tmp.int \"" + outSsl.getCanonicalPath() + "\"", null, decompiler.getParentFile());
				BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while (r.readLine() != null)
					;
				proc.waitFor();

				intTmp.delete();
			}

			IDE.openEditor(m_page, file);
		} catch (NullPointerException e) {
		} catch (Exception e) {
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
