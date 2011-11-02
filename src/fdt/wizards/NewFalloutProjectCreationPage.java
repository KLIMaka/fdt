package fdt.wizards;

import java.nio.charset.Charset;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDEEncoding;

import fdk.cfg.CFGFile;
import fdt.FDT;
import fdt.compilers.CompilersList;
import fdt.preferences.FalloutPreferencePage;
import fdt.preferences.PreferenceConstants;

public class NewFalloutProjectCreationPage extends WizardNewProjectCreationPage {
    private Text     m_tfPath;
    private Combo    m_cbEncoding;

    private String   m_encoding     = "";
    private String   m_workDir      = "";
    private String   m_compilerPath = "";
    private String   m_compilerType = "";

    private Text     m_tfCompilerPath;
    private Group    m_grpEncoding;
    private Group    m_grpCompiler;

    private Listener fieldListener  = new Listener() {
                                        public void handleEvent(Event e) {
                                            boolean valid = validatePage();
                                            setPageComplete(valid);

                                        }
                                    };

    /**
     * Create the wizard.
     */
    public NewFalloutProjectCreationPage() {
        super("wizardPage");
        setTitle("Wizard Page title");
        setDescription("Wizard Page description");
    }

    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    public void createControl(Composite parent) {
        super.createControl(parent);

        Composite container = new Composite((Composite) getControl(), SWT.NONE);
        setControl(container);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        container.setLayout(new GridLayout(2, false));
        new Label(container, SWT.NONE);

        Link link = new Link(container, SWT.NONE);
        link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IPreferencePage page = new FalloutPreferencePage();
                PreferenceManager mgr = new PreferenceManager();
                IPreferenceNode node = new PreferenceNode("1", page);
                mgr.addToRoot(node);
                PreferenceDialog dialog = new PreferenceDialog(getShell(), mgr);
                dialog.create();
                dialog.setMessage(page.getTitle());
                dialog.open();
            }
        });
        link.setText("<a>Configure defaults...</a>");

        m_grpEncoding = new Group(container, SWT.NONE);
        m_grpEncoding.setText("Project details");
        m_grpEncoding.setLayout(new GridLayout(3, false));
        m_grpEncoding.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        Label lblEncoding = new Label(m_grpEncoding, SWT.NONE);
        lblEncoding.setText("Encoding: ");

        m_cbEncoding = new Combo(m_grpEncoding, SWT.NONE);
        m_cbEncoding.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setEncoding(m_cbEncoding.getText());
            }
        });
        m_cbEncoding.setItems(getEncodings());
        m_cbEncoding.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        m_cbEncoding.setText(FDT.getDefault().getPreferenceStore().getString(PreferenceConstants.P_ENCODING));
        m_cbEncoding.addListener(SWT.Modify, fieldListener);
        new Label(m_grpEncoding, SWT.NONE);

        Label lblFalloutConfig = new Label(m_grpEncoding, SWT.NONE);
        lblFalloutConfig.setText("Fallout config: ");

        m_tfPath = new Text(m_grpEncoding, SWT.BORDER | SWT.READ_ONLY);
        m_tfPath.setEditable(true);
        m_tfPath.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPath(m_tfPath.getText());
            }
        });
        m_tfPath.setText(FDT.getDefault().getPreferenceStore().getString(PreferenceConstants.P_FALLOUT_DIRECTORY)
                + "\\fallout2.cfg");
        m_tfPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_tfPath.addListener(SWT.Modify, fieldListener);

        Button btnBrowse = new Button(m_grpEncoding, SWT.NONE);
        btnBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Open congig file");
                openDialog.setFilterExtensions(new String[] { "*.cfg" });
                openDialog.setFilterNames(new String[] { "Fallout2 config" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_tfPath.setText(selected);
                }
            }
        });
        btnBrowse.setText("Browse...");
        List<?> encs = IDEEncoding.getIDEEncodings();
        String encS[] = new String[encs.size()];
        encs.toArray(encS);

        m_grpCompiler = new Group(container, SWT.NONE);
        m_grpCompiler.setText("SSL Compiler");
        m_grpCompiler.setLayout(new GridLayout(3, false));
        m_grpCompiler.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        Label lblCompilerType = new Label(m_grpCompiler, SWT.NONE);
        lblCompilerType.setText("Compiler type:");

        final Combo m_cbCompilerType = new Combo(m_grpCompiler, SWT.READ_ONLY);
        m_cbCompilerType.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setCompilerType(m_cbCompilerType.getText());
            }
        });
        m_cbCompilerType.setItems(CompilersList.getInstance().getSupportedCompilers());
        m_cbCompilerType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        m_cbCompilerType.select(0);
        m_cbCompilerType.addListener(SWT.Modify, fieldListener);

        Label lblCompilerPath = new Label(m_grpCompiler, SWT.NONE);
        lblCompilerPath.setText("Compiler path:");

        m_tfCompilerPath = new Text(m_grpCompiler, SWT.BORDER);
        m_tfCompilerPath.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setCompilerPath(m_tfCompilerPath.getText());
                String comp = CompilersList.getInstance().getCompilerByPath(m_tfCompilerPath.getText());
                if (comp != null) {
                    m_cbCompilerType.setText(comp);
                }
            }
        });
        m_tfCompilerPath.setText(FDT.getDefault().getPreferenceStore().getString(PreferenceConstants.P_COMPILER));
        m_tfCompilerPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_tfCompilerPath.addListener(SWT.Modify, fieldListener);

        Button btnBrowse_2 = new Button(m_grpCompiler, SWT.NONE);
        btnBrowse_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog openDialog = new FileDialog(getShell(), SWT.OPEN);
                openDialog.setText("Select compiler");
                openDialog.setFilterExtensions(new String[] { "*.exe" });
                openDialog.setFilterNames(new String[] { "Executables" });
                String selected = openDialog.open();
                if (selected != null) {
                    m_tfCompilerPath.setText(selected);
                }
            }
        });
        btnBrowse_2.setText("Browse...");

        getContainer().getShell().setSize(550, 550);
    }

    protected String[] getEncodings() {
        List<?> encs = IDEEncoding.getIDEEncodings();
        String ret[] = new String[encs.size()];
        encs.toArray(ret);
        return ret;
    }

    @Override
    protected boolean validatePage() {
        if (getEncoding().equals("") || !Charset.isSupported(getEncoding())) {
            setErrorMessage("Invalid encoding");
            return false;
        }

        {
            try {
                @SuppressWarnings("unused")
                CFGFile cfg = new CFGFile(getPath());
            } catch (Exception e) {
                setErrorMessage("Invalid config");
                return false;
            }
        }

        if (CompilersList.getInstance().getCompiler(getCompilerType()).check(getCompilerPath()) == false) {
            setErrorMessage("Invalid compiler");
            return false;
        }

        return super.validatePage();
    }

    public String getEncoding() {
        return m_encoding;
    }

    public void setEncoding(String encoding) {
        m_encoding = encoding;
    }

    public String getPath() {
        return m_workDir;
    }

    public void setPath(String path) {
        m_workDir = path;
    }

    public String getCompilerPath() {
        return m_compilerPath;
    }

    public void setCompilerPath(String compilerPath) {
        m_compilerPath = compilerPath;
    }

    public String getCompilerType() {
        return m_compilerType;
    }

    public void setCompilerType(String compilerType) {
        m_compilerType = compilerType;
    }

}
