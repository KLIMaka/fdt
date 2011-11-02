package fdt.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import fdk.cfg.CFGFile;
import fdt.ContextSettings;
import fdt.compilers.CompilersList;
import fdt.preferences.PreferenceConstants;

public class FalloutProjectPropertyPage extends PropertyPage {
    private Composite       m_composite;
    private Text            m_cfgPath;
    private Group           m_group;
    private Label           m_label;
    private Combo           m_cpType;
    private Label           m_label_1;
    private Text            m_cpPath;
    private Button          m_button;
    private Label           m_label_2;
    private Text            m_ppPath;
    private Button          m_button_1;

    private IProject        m_project;
    private ContextSettings m_settings;
    private Label           m_label_3;
    private Text            m_dcpPath;
    private Button          m_button_2;
    private Label           m_label_4;
    private Text            m_cpOptions;
    private Label           m_label_5;
    private Text            m_ppOptions;

    /**
     * Constructor for SamplePropertyPage.
     */
    public FalloutProjectPropertyPage() {
        super();
    }

    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(Composite parent) {
        m_project = ((IProject) getElement());
        m_settings = new ContextSettings(m_project);

        m_composite = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        m_composite.setLayoutData(data);

        m_composite.setLayout(new GridLayout(3, false));

        Label lblFalloutConfig = new Label(m_composite, SWT.NONE);
        lblFalloutConfig.setText("Fallout config");

        m_cfgPath = new Text(m_composite, SWT.BORDER);
        m_cfgPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_cfgPath.setText(m_settings.getString(PreferenceConstants.C_CFG));

        Button btnBrowse = new Button(m_composite, SWT.NONE);
        btnBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Open congig file");
                openDialog.setFilterExtensions(new String[] { "*.cfg" });
                openDialog.setFilterNames(new String[] { "Fallout2 config" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_cfgPath.setText(selected);
                }
            }
        });
        btnBrowse.setText("Browse...");

        m_group = new Group(m_composite, SWT.NONE);
        m_group.setText("SSL Compiler");
        m_group.setLayout(new GridLayout(3, false));
        m_group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

        m_label = new Label(m_group, SWT.NONE);
        m_label.setText("Compiler Type");

        m_cpType = new Combo(m_group, SWT.READ_ONLY);
        m_cpType.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.C_COMPILER_TYPE, m_cpType.getText());
            }
        });
        m_cpType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        m_cpType.setItems(CompilersList.getInstance().getSupportedCompilers());
        m_cpType.setText(m_settings.getString(PreferenceConstants.C_COMPILER_TYPE));
        new Label(m_group, SWT.NONE);

        m_label_1 = new Label(m_group, SWT.NONE);
        m_label_1.setText("Compiler Path");

        m_cpPath = new Text(m_group, SWT.BORDER);
        m_cpPath.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.P_COMPILER, m_cpPath.getText());
                String comp = CompilersList.getInstance().getCompilerByPath(m_cpPath.getText());
                if (comp != null) {
                    m_cpType.setText(comp);
                }
            }
        });
        m_cpPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_cpPath.setText(m_settings.getString(PreferenceConstants.P_COMPILER));

        m_button = new Button(m_group, SWT.NONE);
        m_button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Select compiler");
                openDialog.setFilterExtensions(new String[] { "*.exe" });
                openDialog.setFilterNames(new String[] { "Executables" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_cpPath.setText(selected);
                }
            }
        });
        m_button.setText("Browse...");

        m_label_4 = new Label(m_group, SWT.NONE);
        m_label_4.setText("Compiler options");

        m_cpOptions = new Text(m_group, SWT.BORDER);
        m_cpOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_cpOptions.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.P_COMPILER_OPTIONS, m_cpOptions.getText());
            }
        });
        m_cpOptions.setText(m_settings.getString(PreferenceConstants.P_COMPILER_OPTIONS));
        new Label(m_group, SWT.NONE);

        m_label_2 = new Label(m_group, SWT.NONE);
        m_label_2.setText("Preprocessot Path");

        m_ppPath = new Text(m_group, SWT.BORDER);
        m_ppPath.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.P_PREPROCESSOR, m_ppPath.getText());
            }
        });
        m_ppPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_ppPath.setText(m_settings.getString(PreferenceConstants.P_PREPROCESSOR));

        m_button_1 = new Button(m_group, SWT.NONE);
        m_button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Select preprocessor");
                openDialog.setFilterExtensions(new String[] { "*.exe" });
                openDialog.setFilterNames(new String[] { "Executables" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_ppPath.setText(selected);
                }
            }
        });
        m_button_1.setText("Browse...");

        m_label_5 = new Label(m_group, SWT.NONE);
        m_label_5.setText("Preprocessor options");

        m_ppOptions = new Text(m_group, SWT.BORDER);
        m_ppOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_ppOptions.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.P_PP_OPTIONS, m_ppOptions.getText());
            }
        });
        m_ppOptions.setText(m_settings.getString(PreferenceConstants.P_PP_OPTIONS));
        new Label(m_group, SWT.NONE);

        m_label_3 = new Label(m_group, SWT.NONE);
        m_label_3.setText("Decompiler Path");

        m_dcpPath = new Text(m_group, SWT.BORDER);
        m_dcpPath.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                m_settings.set(PreferenceConstants.P_DECOMPILER, m_dcpPath.getText());
            }
        });
        m_dcpPath.setText(m_settings.getString(PreferenceConstants.P_DECOMPILER));
        m_dcpPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        m_button_2 = new Button(m_group, SWT.NONE);
        m_button_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        m_button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Select decompiler");
                openDialog.setFilterExtensions(new String[] { "*.exe" });
                openDialog.setFilterNames(new String[] { "Executables" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_dcpPath.setText(selected);
                }
            }
        });
        m_button_2.setText("Browse...");
        return m_composite;
    }

    protected void performDefaults() {
        super.performDefaults();
    }

    public boolean performOk() {
        setErrorMessage(null);

        try {
            @SuppressWarnings("unused")
            CFGFile cfg = new CFGFile(m_cfgPath.getText());
        } catch (Exception e) {
            setErrorMessage("Invalid config");
            return false;
        }

        if (CompilersList.getInstance().getCompiler(m_cpType.getText()).check(m_cpPath.getText()) == false) {
            setErrorMessage("Invalid compiler");
            return false;
        }

        return super.performOk();
    }

    @Override
    protected void performApply() {
        if (performOk() == true) {

            try {
                m_settings.flush();
            } catch (Exception e) {}
        }

    }

}
