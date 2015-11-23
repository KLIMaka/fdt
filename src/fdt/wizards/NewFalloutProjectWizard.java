package fdt.wizards;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.osgi.service.prefs.BackingStoreException;

import fdk.cfg.CFGFile;
import fdt.SslSettingsContext;
import fdt.builder.*;
import fdt.preferences.PreferenceConstants;

public class NewFalloutProjectWizard extends BasicNewResourceWizard implements INewWizard {

	private NewFalloutProjectCreationPage mainPage;

	public NewFalloutProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		try {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				protected void execute(IProgressMonitor monitor) {
					createProject(monitor != null ? monitor : new NullProgressMonitor());
				}
			};
			getContainer().run(false, true, op);
		} catch (InvocationTargetException x) {
			return false;
		} catch (InterruptedException x) {
			return false;
		}
		return true;
	}

	public void addPages() {
		super.addPages();
		mainPage = new NewFalloutProjectCreationPage();
		mainPage.setTitle("Create a Fallout Mod Project");
		mainPage.setDescription("Create a Fallout Mod project in the workspace");
		addPage(mainPage);
	}

	protected void createProject(IProgressMonitor monitor) {
		monitor.beginTask("Creating project", 20);
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			monitor.subTask("Creating directories");

			IProject project = root.getProject(mainPage.getProjectName());
			IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
			if (!Platform.getLocation().equals(mainPage.getLocationPath()))
				description.setLocation(mainPage.getLocationPath());
			description.setNatureIds(new String[] { SSLNature.NATURE_ID });
			ICommand command = description.newCommand();
			command.setBuilderName(SSLBuilder.BUILDER_ID);
			description.setBuildSpec(new ICommand[] { command });
			project.create(description, monitor);
			monitor.worked(10);
			project.open(monitor);
			project.setDefaultCharset(mainPage.getEncoding(), monitor);
			monitor.worked(10);

			try {

				SslSettingsContext settings = new SslSettingsContext(project);
				settings.set(PreferenceConstants.P_ENCODING, mainPage.getEncoding());
				settings.set(PreferenceConstants.P_COMPILER, mainPage.getCompilerPath());
				settings.set(PreferenceConstants.C_CFG, mainPage.getPath());
				settings.set(PreferenceConstants.C_COMPILER_TYPE, mainPage.getCompilerType());
				settings.flush();

				IFolder link = project.getFolder("Resources");
				String conf = mainPage.getPath().replaceAll("\\\\", "/");
				String query = project.getName() + "/" + conf;
				URI uri = new URI("dat", null, "/", query, null);
				link.createLink(uri, IResource.BACKGROUND_REFRESH, monitor);

				IFolder patches = project.getFolder("Patches");
				CFGFile cfg = new CFGFile(mainPage.getPath());
				File patchesDir = cfg.getPath("system.master_patches");
				if (!patchesDir.exists()) {
					patchesDir.mkdirs();
					patchesDir.mkdir();
				}
				project.getFile("script.lst").create(new ByteArrayInputStream(new byte[] {}), true, monitor);
				project.getFolder("src").create(true, true, monitor);

				patches.createLink(new Path(patchesDir.toString()), IResource.BACKGROUND_REFRESH, monitor);
			} catch (URISyntaxException e) {
			} catch (UnsupportedEncodingException e) {
			} catch (BackingStoreException e) {
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		} catch (CoreException x) {
		}
	}
}
