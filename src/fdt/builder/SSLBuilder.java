package fdt.builder;

import static fdt.Fdt.PLUGIN_ID;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import fdt.Fdt;

public class SSLBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "FDT.SSLBuilder";

	private static Pattern SSL_PATTERN = Pattern.compile(".*\\.[Ss][Ss][Ll]$");

	public static boolean checkSSLName(String name) {
		return SSL_PATTERN.matcher(name).find();
	}

	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private int getSslFilesCount(IResource resource) throws CoreException {

		if (resource.getType() == IResource.FILE && checkSSLName(resource.getName())) {
			return 1;
		} else if (resource instanceof IContainer) {
			IContainer folder = (IContainer) resource;
			int res = 0;
			for (IResource d : folder.members()) {
				res += getSslFilesCount(d);
			}
			return res;
		}

		return 0;
	}

	private int getChangedSslFilesCount(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource.getType() == IResource.FILE && checkSSLName(resource.getName())) {
			return 1;
		} else if (resource.getType() == IResource.FOLDER || resource.getType() == IResource.PROJECT) {
			int res = 0;
			for (IResourceDelta d : delta.getAffectedChildren(IResourceDelta.ADDED | IResourceDelta.CHANGED)) {
				res += getChangedSslFilesCount(d);
			}
			return res;
		}
		return 0;
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		try {
			getProject().accept(new SSLCResourceVisitor(getProject(), monitor, getSslFilesCount(getProject())));
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR, PLUGIN_ID, "Error while full build", e));
		}
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		try {
			delta.accept(new SSLCResourceVisitor(getProject(), monitor, getChangedSslFilesCount(delta)));
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR, Fdt.PLUGIN_ID, "Error while incrementel build", e));
		}
	}
}
