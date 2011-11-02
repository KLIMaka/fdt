package fdt.adapters;

import org.eclipse.core.resources.IProject;

public class ScriptAdapter {
    private IProject m_project;
    private String   m_name;

    public ScriptAdapter(IProject proj, String name) {
        m_project = proj;
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public IProject getProject() {
        return m_project;
    }
}
