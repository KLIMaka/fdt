package fdt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

public class ContextSettings {

    private boolean             m_dirty = false;
    private IEclipsePreferences m_settings;

    public ContextSettings(IProject project) {
        IScopeContext projectScope = new ProjectScope(project);
        m_settings = projectScope.getNode("SSL");
    }

    public String getString(String name) {
        String res = m_settings.get(name, null);
        if (res == null) {
            return FDT.getDefault().getPreferenceStore().getString(name);
        } else {
            return res;
        }
    }

    public void set(String name, String val) {
        m_settings.put(name, val);
        m_dirty = true;
    }

    public String syncWithDefaults(String name) {
        String def = FDT.getDefault().getPreferenceStore().getString(name);
        if (def != null) {
            set(name, def);
            return def;
        }
        return null;
    }

    public void flush() throws BackingStoreException {
        if (m_dirty) {
            m_settings.flush();
            m_dirty = false;
        }
    }
}
