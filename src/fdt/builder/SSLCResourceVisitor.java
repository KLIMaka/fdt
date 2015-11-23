package fdt.builder;

import static fdt.builder.SSLBuilder.checkSSLName;
import static fdt.compilers.SslProblemMark.MARKER_TYPE;
import static fdt.preferences.PreferenceConstants.*;
import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import java.io.*;
import java.util.Collection;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.console.*;

import fdk.cfg.CFGFile;
import fdt.*;
import fdt.compilers.*;

public class SSLCResourceVisitor implements IResourceVisitor, IResourceDeltaVisitor {

	class Compiler {
		private File outDir;
		private File cpPath;
		private String cpType;

		public Compiler(File outD, File cpP, String cpT) {
			outDir = outD;
			cpPath = cpP;
			cpType = cpT;

			outDir.mkdirs();
		}

		public Collection<SslProblemMark> compile(IFile file, MessageConsoleStream msg) throws Exception {
			File outFile = new File(outDir, file.getName().replaceAll("\\.[Ss][Ss][Ll]$", ".int"));
			return CompilersList.getInstance().getCompiler(cpType).compile(cpPath, file, outFile, msg);
		}
	}

	private Compiler compiler;
	private MessageConsole console;
	private IProgressMonitor monitor;
	private IProject project;

	public SSLCResourceVisitor(IProject project, IProgressMonitor monitor, int files) throws FileNotFoundException, IOException {
		this.project = project;
		this.monitor = monitor;
		this.console = createConsole("SSLC Console");
		this.compiler = createCompiler(project);
		monitor.beginTask("Building scripts", files);
	}

	private Compiler createCompiler(IProject project) throws FileNotFoundException, IOException {
		SslSettingsContext ctx = new SslSettingsContext(project);
		String cpPath = ctx.get(P_COMPILER);
		String cpType = ctx.get(C_COMPILER_TYPE);
		String cfgPath = ctx.get(C_CFG);

		File fCfg = new File(cfgPath);
		CFGFile cfg = new CFGFile(fCfg);
		File outDir = new File(cfg.getPath("system.master_patches"), "scripts");

		return new Compiler(outDir, new File(cpPath), cpType);
	}

	public boolean visit(IResource resource) {
		if (monitor.isCanceled()) {
			return false;
		}
		monitor.subTask("Building script: " + resource.getFullPath().toString());

		try {
			boolean ret = compile(resource);
			monitor.worked(ret ? 1 : 0);
		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
		}

		return true;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (monitor.isCanceled()) {
			return false;
		}
		IResource resource = delta.getResource();
		monitor.subTask("Building script: " + resource.getFullPath().toString());

		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
		case IResourceDelta.CHANGED:
			return visit(resource);
		}

		return true;
	}

	private MessageConsole createConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName())) {
				MessageConsole messageConsole = (MessageConsole) existing[i];
				messageConsole.clearConsole();
				return messageConsole;
			}
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	protected boolean compile(IResource resource) throws Exception {
		if (!(resource instanceof IFile) || !checkSSLName(resource.getName()))
			return false;

		deleteMarkers(resource);
		IFile file = (IFile) resource;

		Collection<SslProblemMark> marks = compiler.compile(file, console.newMessageStream());

		for (SslProblemMark mark : marks) {
			mark.apply(project, file);
		}

		return true;
	}

	private void deleteMarkers(IResource file) throws CoreException {
		file.deleteMarkers(MARKER_TYPE, false, DEPTH_ZERO);
	}
}
