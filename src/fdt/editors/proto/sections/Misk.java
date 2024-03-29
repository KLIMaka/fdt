package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;

import fdk.proto.Prototype;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.util.Ref;

public class Misk extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Text m_pid;
	private Text m_charges;
	private Text m_type;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Misk(Composite parent, ProtoAdaptorsFactory fact) {
		super(parent, SWT.NONE);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				m_toolkit.dispose();
			}
		});
		m_toolkit.adapt(this);
		m_toolkit.paintBordersFor(this);
		setLayout(new GridLayout(6, false));

		Label lblPowerPid = m_toolkit.createLabel(this, "Power PID", SWT.NONE);
		lblPowerPid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_pid = m_toolkit.createText(this, "New Text", SWT.NONE);
		m_pid.setText("");
		m_pid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPowerType = m_toolkit.createLabel(this, "Power type", SWT.NONE);
		lblPowerType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_type = m_toolkit.createText(this, "New Text", SWT.NONE);
		m_type.setText("");
		m_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblCharges = m_toolkit.createLabel(this, "Charges", SWT.NONE);
		lblCharges.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_charges = m_toolkit.createText(this, "New Text", SWT.NONE);
		m_charges.setText("");
		m_charges.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		m_pid.setText("0x" + Integer.toHexString(proto.get().get(Prototype.POWER_PID)).toUpperCase());
		m_type.setText("0x" + Integer.toHexString(proto.get().get(Prototype.POWER_TYPE)).toUpperCase());
		m_charges.setText("0x" + Integer.toHexString(proto.get().get(Prototype.CHARGES)).toUpperCase());
	}

	@Override
	public void setup() throws Exception {
	}

	@Override
	public Control toControl() {
		return this;
	}
}
