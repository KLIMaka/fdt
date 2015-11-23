package fdt.controls.frm.list.selectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import fdt.Fdt;
import fdt.controls.frm.list.FRMList;
import fdt.controls.frm.list.providers.FrmProvider;

public class FidSelector extends Dialog {

	private FRMList list;
	private int type;
	private IProject proj;

	public FidSelector(Shell parentShell, IProject proj, int type) {
		super(parentShell);
		this.proj = proj;
		this.type = type;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public boolean close() {
		list.dispose();
		return super.close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		list = new FRMList(container);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		list.setCellSize(100, 100);
		list.setPal(Fdt.getStdPal(proj));
		FrmProvider provider = new FrmProvider(proj);
		list.setLabelProvider(provider);
		list.setContentProvider(provider);
		list.setInput(type);
		getShell().setText("Select FID");

		return container;
	}

	public Object getSelection() {
		return list.getSelection();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 600);
	}

}
