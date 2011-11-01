package ssl.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ide.IDEEncoding;
import org.eclipse.ui.ide.dialogs.AbstractEncodingFieldEditor;

public class MyEncodingFieldEditor extends AbstractEncodingFieldEditor {

    public MyEncodingFieldEditor(String name, String labelText, String groupTitle, Composite parent) {
        super();
        init(name, labelText);
        setGroupTitle(groupTitle);
        createControl(parent);
    }

    @Override
    public void setPreferenceStore(IPreferenceStore store) {
        if (store != null) {
            super.setPreferenceStore(store);
        } else {
            ((FieldEditor) this).setPreferenceName(null);
        }
    }

    @Override
    protected String getStoredValue() {
        return getPreferenceStore().getString(getPreferenceName());
    }

    @Override
    protected void doStore() {
        String encoding = getSelectedEncoding();

        if (hasSameEncoding(encoding)) {
            return;
        }

        IDEEncoding.addIDEEncoding(encoding);

        if (encoding.equals(getDefaultEnc())) {
            getPreferenceStore().setToDefault(getPreferenceName());
        } else {
            getPreferenceStore().setValue(getPreferenceName(), encoding);
        }
    }

}
