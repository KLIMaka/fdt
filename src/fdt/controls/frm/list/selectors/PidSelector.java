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
import fdt.controls.frm.list.providers.*;

public class PidSelector extends Dialog {

	private IProject proj;
	private int type;
	private FRMList list;
	private int filter;

	public PidSelector(Shell parentShell, IProject proj, int type, int filter) {
		super(parentShell);
		this.proj = proj;
		this.type = type;
		this.filter = filter;
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
		ProtoProvider provider;
		if (filter == -1) {
			provider = new ProtoProvider(proj);
		} else {
			provider = new FilteredProtoProvider(proj, filter);
		}
		list.setLabelProvider(provider);
		list.setContentProvider(provider);
		list.setInput(type);
		getShell().setText("Select PID");

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
