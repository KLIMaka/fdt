package fdt.compilers;

import static org.eclipse.core.resources.IMarker.*;
import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import java.io.File;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

public class SslProblemMark {

	public static final String MARKER_TYPE = "FDT.sslcProblem";

	private File file;
	private String message;
	private int severity;
	private int line;

	public SslProblemMark(File file, String message, int severity, int line) {
		this.file = file;
		this.message = message;
		this.severity = severity;
		this.line = line;
	}

	public SslProblemMark(String file, String message, int severity, int line) {
		this(new File(file), message, severity, line);
	}

	public void apply(IProject project, IFile srcFile) throws CoreException {
		IPath path = new Path(file.getAbsolutePath()).makeRelativeTo(project.getLocation());
		IResource dstFile = project.findMember(path);
		dstFile = dstFile == null ? srcFile : dstFile;

		for (IMarker m : dstFile.findMarkers(MARKER_TYPE, true, DEPTH_ZERO)) {
			if (m.getAttribute(MESSAGE).equals(message)) {
				return;
			}
		}

		IMarker marker = dstFile.createMarker(MARKER_TYPE);
		marker.setAttribute(MESSAGE, message);
		marker.setAttribute(SEVERITY, severity);
		marker.setAttribute(LINE_NUMBER, line == -1 ? 1 : line);
	}
}
