package fdt;

import static fdt.builder.SSLBuilder.checkSSLName;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

public class FdtResourceChangeReporter implements IResourceChangeListener {

	class DeltaPrinter implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) {
			IResource res = delta.getResource();
			if (res instanceof IFile && checkSSLName(res.getName())) {
				IProject project = res.getProject();
				try {
					project.getFolder("Patches\\scripts").refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					Fdt.getDefault().handleException(e);
				}
			}
			return true;
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			if (event.getType() == IResourceChangeEvent.POST_BUILD) {
				event.getDelta().accept(new DeltaPrinter());
			}
		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
		}
	}

}
