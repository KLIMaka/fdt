package fdt.editors.proto;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fdk.lst.LST;

public class ProEditorContentProvider implements ITreeContentProvider {

    private LST m_lst;

    @Override
    public void dispose() {}

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        m_lst = (LST) newInput;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return m_lst.toArray();
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
