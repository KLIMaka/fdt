package ssl.editors.frm;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ssl.SslPlugin;

public class FidSelector extends Dialog {

    private FRMList  m_list;
    private int      m_type;
    private IProject m_proj;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public FidSelector(Shell parentShell, IProject proj, int type) {
        super(parentShell);
        m_proj = proj;
        m_type = type;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    public boolean close() {
        m_list.dispose();
        return super.close();
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        m_list = new FRMList(container);
        m_list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        m_list.setCellSize(100, 100);
        m_list.setPal(SslPlugin.getStdPal(m_proj));
        FrmProvider provider = new FrmProvider(m_proj);
        m_list.setLabelProvider(provider);
        m_list.setContentProvider(provider);
        m_list.setInput(m_type);
        getShell().setText("Select FID");

        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(600, 600);
    }

}
