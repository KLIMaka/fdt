package fdt.adapters;

import org.eclipse.core.resources.IProject;

public class ScriptAdapter {
	private IProject project;
	private String name;

	public ScriptAdapter(IProject proj, String name) {
		this.project = proj;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IProject getProject() {
		return project;
	}
}
