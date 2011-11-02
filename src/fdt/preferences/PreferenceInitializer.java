package fdt.preferences;

import java.nio.charset.Charset;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import fdt.FDT;

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
        IPreferenceStore store = FDT.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_ENCODING, Charset.defaultCharset().name());
    }

}
