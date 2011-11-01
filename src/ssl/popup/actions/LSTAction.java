package ssl.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;

import ssl.adapters.ScriptAdapter;
import ssl.editors.scriptslist.ScriptsList;

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
