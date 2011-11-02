package fdt.editors.scriptslist;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fdk.lst.LST;

public class ScriptsListContentProvider implements ITreeContentProvider {

    LST m_list;

    @Override
    public void dispose() {}

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        m_list = (LST) newInput;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return m_list.toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        return null;
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return false;
    }

}
