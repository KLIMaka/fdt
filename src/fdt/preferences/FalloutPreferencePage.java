package fdt.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.*;

import fdt.Fdt;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class FalloutPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public FalloutPreferencePage() {
		super("Fallout", GRID);
		setPreferenceStore(Fdt.getDefault().getPreferenceStore());
		setDescription("Default Fallout preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI
	 * blocks needed to manipulate various types of preferences. Each field editor
	 * knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new MyEncodingFieldEditor(PreferenceConstants.P_ENCODING, "Default encoding", "Encoding", getFieldEditorParent()));

		addField(new DirectoryFieldEditor(PreferenceConstants.P_FALLOUT_DIRECTORY, "Fallout directory", getFieldEditorParent()));

		addField(new FileFieldEditor(PreferenceConstants.P_COMPILER, "SSL Compiler", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_COMPILER_OPTIONS, "Compiler options", StringFieldEditor.UNLIMITED, getFieldEditorParent()));

		addField(new FileFieldEditor(PreferenceConstants.P_PREPROCESSOR, "Preprocesor", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.P_PP_OPTIONS, "Preprocessor options", StringFieldEditor.UNLIMITED, getFieldEditorParent()));

		addField(new FileFieldEditor(PreferenceConstants.P_DECOMPILER, "Decompiler", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
