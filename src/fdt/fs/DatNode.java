package fdt.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import fdk.fs.IFileEntry;
import fdk.util.Utils;

public class DatNode implements IFileStore {

    protected IFileEntry m_ent;
    protected Resource   m_resource;
    protected FileInfo   m_info = null;
    private IFileStore   m_parent;

    public DatNode(IFileEntry node, Resource newRes, IFileStore parent) {
        m_ent = node;
        m_resource = newRes;
        m_parent = parent;
    }

    @Override
    public IFileInfo[] childInfos(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public String[] childNames(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public IFileStore[] childStores(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public void copy(IFileStore destination, int options, IProgressMonitor monitor) throws CoreException {

        OutputStream out = destination.openOutputStream(0, monitor);
        InputStream in = openInputStream(0, monitor);

        try {
            Utils.copyStrream(in, out);

        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, "ssl", "File cannot copy"));
        } finally {
            try {
                out.close();
            } catch (IOException e) {}
        }

    }

    @Override
    public void delete(int options, IProgressMonitor monitor) throws CoreException {}

    @Override
    public IFileInfo fetchInfo() {
        if (m_info == null) {
            m_info = new FileInfo(m_ent.getName());
            if (m_ent.isDirectory())
                m_info.setDirectory(true);
            else m_info.setDirectory(false);

            m_info.setExists(true);
            m_info.setAttribute(EFS.ATTRIBUTE_READ_ONLY, true);
        }

        return m_info;
    }

    @Override
    public IFileInfo fetchInfo(int options, IProgressMonitor monitor) throws CoreException {
        return fetchInfo();
    }

    @Override
    public IFileStore getChild(IPath path) {
        return null;
    }

    @Override
    public IFileStore getChild(String name) {
        return null;
    }

    @Override
    public IFileStore getFileStore(IPath path) {
        return null;
    }

    @Override
    public IFileSystem getFileSystem() {
        return m_resource.getFs();
    }

    @Override
    public String getName() {
        return m_ent.getName();
    }

    @Override
    public IFileStore getParent() {
        return m_parent;
    }

    @Override
    public boolean isParentOf(IFileStore other) {
        return false;
    }

    @Override
    public IFileStore mkdir(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public void move(IFileStore destination, int options, IProgressMonitor monitor) throws CoreException {}

    @Override
    public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException {
        try {
            return m_resource.getSource().getFile(m_ent);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public OutputStream openOutputStream(int options, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public void putInfo(IFileInfo info, int options, IProgressMonitor monitor) throws CoreException {}

    @Override
    public File toLocalFile(int options, IProgressMonitor monitor) throws CoreException {

        if (options == EFS.CACHE) {

            try {

                InputStream in = m_resource.getSource().getFile(m_ent);
                if (in == null) return null;

                File f = File.createTempFile(m_ent.getName(), null);
                // f.getParentFile().mkdirs();

                FileOutputStream fout = new FileOutputStream(f);
                Utils.copyStrream(in, fout);
                fout.close();

                return f;
            } catch (IOException e1) {
                return null;
            }

        } else return null;
    }

    @Override
    public URI toURI() {
        try {
            String fragment = m_ent.getFullName().replace('\\', '/');
            String query = m_resource.getUri().getQuery();
            String path = m_resource.getUri().getPath();
            URI uri = new URI("dat", null, path, query, fragment);
            return uri;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    @Override
    public String toString() {
        return m_resource.getUri().getQuery() + "/" + m_ent.getFullName().replace('\\', '/');
    }

}
