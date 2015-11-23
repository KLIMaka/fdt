package fdt;

import static fdt.Fdt.SSL_SETTINGS_SCOPE;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.preferences.*;
import org.osgi.service.prefs.BackingStoreException;

public class SslSettingsContext {

	private boolean isDirty = false;
	private IEclipsePreferences settings;

	public SslSettingsContext(IProject project) {
		IScopeContext projectScope = new ProjectScope(project);
		settings = projectScope.getNode(SSL_SETTINGS_SCOPE);
	}

	private String getDefault(String name) {
		return Fdt.getDefault().getPreferenceStore().getString(name);
	}

	public String get(String name) {
		String res = settings.get(name, null);
		if (res == null) {
			return getDefault(name);
		} else {
			return res;
		}
	}

	public void set(String name, String val) {
		settings.put(name, val);
		isDirty = true;
	}

	public void flush() throws BackingStoreException {
		if (isDirty) {
			settings.flush();
			isDirty = false;
		}
	}

	public static String getSslSetting(IProject project, String name) {
		return new SslSettingsContext(project).get(name);
	}
}
