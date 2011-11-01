package ssl.preferences;

import java.nio.charset.Charset;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ssl.SslPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = SslPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_ENCODING, Charset.defaultCharset().name());
    }

}
