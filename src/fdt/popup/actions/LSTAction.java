package fdt.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import fdt.adapters.ScriptAdapter;
import fdt.editors.scriptslist.ScriptsList;

public abstract class LSTAction implements IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		ScriptAdapter item = (ScriptAdapter) ((StructuredSelection) selection).getFirstElement();
		select(action, item);
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		IWorkbenchPage page = targetPart.getSite().getPage();
		IProject project = null;
		if (targetPart instanceof ScriptsList)
			project = ((FileEditorInput) ((ScriptsList) targetPart).getEditorInput()).getFile().getProject();
		setProject(project, page);
	}

	abstract public void setProject(IProject proj, IWorkbenchPage page);

	abstract public void select(IAction action, ScriptAdapter lstent);

}
