package ssl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import ssl.builder.SSLBuilder;

public class MyResourceChangeReporter implements IResourceChangeListener {

    class DeltaPrinter implements IResourceDeltaVisitor {
        public boolean visit(IResourceDelta delta) {
            IResource res = delta.getResource();
            if (res instanceof IFile && SSLBuilder.checkSSLName(res.getName())) {
                IProject project = res.getProject();
                try {
                    project.getFolder("Patches\\scripts").refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            return true; // visit the children
        }
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        try {
            if (event.getType() == IResourceChangeEvent.POST_BUILD) {
                event.getDelta().accept(new DeltaPrinter());
            }
        } catch (Exception e) {}

    }

}
