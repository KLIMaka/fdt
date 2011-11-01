package ssl.fs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import fdk.fs.IFileEntry;

public class DatDir extends DatNode {

    protected Map<String, DatNode> m_childs     = new TreeMap<String, DatNode>();
    protected IFileInfo[]          m_childInfos = null;

    public DatDir(IFileEntry node, Resource newRes, IFileStore parent) {
        super(node, newRes, parent);
        collectChilds();
    }

    private void collectChilds() {
        m_childs.clear();

        for (IFileEntry ent : m_ent.getSub()) {
            if (ent.isDirectory())
                m_childs.put(ent.getName(), new DatDir(ent, m_resource, this));
            else m_childs.put(ent.getName(), new DatNode(ent, m_resource, this));
        }
    }

    @Override
    public IFileInfo[] childInfos(int options, IProgressMonitor monitor) throws CoreException {
        if (monitor != null) {
            monitor.setTaskName(getName());
        }

        collectChilds();

        m_childInfos = new IFileInfo[m_childs.values().size()];
        int i = 0;
        for (DatNode node : m_childs.values()) {
            m_childInfos[i++] = node.fetchInfo();
        }

        return m_childInfos;
    }

    @Override
    public IFileStore getChild(IPath path) {

        if (path.segmentCount() == 1)
            return m_childs.get(path.segment(0));
        else {
            DatNode node = m_childs.get(path.segment(0));
            if (node != null) {
                return node.getChild(path.removeFirstSegments(1));
            }
        }

        return null;
    }

    @Override
    public IFileStore getChild(String name) {
        return m_childs.get(name);
    }

    @Override
    public IFileStore getFileStore(IPath path) {
        return getChild(path);
    }

    @Override
    public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public OutputStream openOutputStream(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public void copy(IFileStore destination, int options, IProgressMonitor monitor) throws CoreException {
        destination.mkdir(0, monitor);
    }

    @Override
    public boolean isParentOf(IFileStore other) {
        for (DatNode node : m_childs.values())
            if (node == other) return true;
        return false;
    }

}
