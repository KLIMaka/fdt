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

public class PidSelector extends Dialog {

    private IProject m_proj;
    private int      m_type;
    private FRMList  m_list;
    private int      m_filter;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public PidSelector(Shell parentShell, IProject proj, int type, int filter) {
        super(parentShell);
        m_proj = proj;
        m_type = type;
        m_filter = filter;
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
        ProtoProvider provider;
        if (m_filter == -1) {
            provider = new ProtoProvider(m_proj);
        } else {
            provider = new FilteredProtoProvider(m_proj, m_filter);
        }
        m_list.setLabelProvider(provider);
        m_list.setContentProvider(provider);
        m_list.setInput(m_type);
        getShell().setText("Select PID");

        return container;
    }

    public Object getSelection() {
        return m_list.getSelection();
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
