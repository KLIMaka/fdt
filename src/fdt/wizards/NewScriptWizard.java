package fdt.wizards;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewScriptWizard extends BasicNewFileResourceWizard {

    private String m_file;

    @Override
    public void addPages() {
        super.addPages();
        WizardNewFileCreationPage p = (WizardNewFileCreationPage) getPage("newFilePage1");
        p.setFileExtension("ssl");
        p.setTitle("New script");
        p.setDescription("Creates a new script");
        setWindowTitle("New script");
    }

    @Override
    public boolean performFinish() {
        if (super.performFinish()) {
            WizardNewFileCreationPage p = (WizardNewFileCreationPage) getPage("newFilePage1");
            m_file = p.getFileName();
            return true;
        }

        return false;
    }

    public String getFile() {
        return m_file;
    }
}
