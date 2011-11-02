package fdt.builder;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class FileSearch implements IResourceVisitor {
    private File      m_file;
    private IResource m_res;

    public FileSearch(File file) {
        m_file = file;
    }

    @Override
    public boolean visit(IResource resource) throws CoreException {
        IPath location = resource.getLocation();
        if (location != null && location.toFile().equals(m_file)) {
            m_res = resource;
            return false;
        }
        return true;
    }

    public IResource getRes() {
        return m_res;
    }

}
