package fdt.editors.scriptslist;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import fdt.Fdt;
import fdt.editors.proto.ProEditor;

public class ScriptsList extends FormEditor {

	@Override
	protected void addPages() {
		try {
			addPage(new ScriptsLstPage(this, "scripts.lst", "Scripts"));
			addPage(new ProEditor(this, "proto", "PROTO Editor"));
		} catch (PartInitException e) {
			Fdt.getDefault().handleException(e);
		}

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		getActivePageInstance().doSave(monitor);
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setDirty() {
		firePropertyChange(PROP_DIRTY);
	}

}
