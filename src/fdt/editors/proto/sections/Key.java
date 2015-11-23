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

public class Key extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Text m_key;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Key(Composite parent, ProtoAdaptorsFactory fact) {
		super(parent, SWT.NONE);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				m_toolkit.dispose();
			}
		});
		m_toolkit.adapt(this);
		m_toolkit.paintBordersFor(this);
		setLayout(new GridLayout(2, false));

		Label lblKey = m_toolkit.createLabel(this, "Key", SWT.NONE);
		lblKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_key = m_toolkit.createText(this, "New Text", SWT.NONE);
		m_key.setText("");
		m_key.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		int key = (Integer) proto.get().get(Prototype.KEY_UNK);
		m_key.setText("0x" + Integer.toHexString(key).toUpperCase());
	}

	@Override
	public void setup() throws Exception {
	}

	@Override
	public Control toControl() {
		return this;
	}

}
