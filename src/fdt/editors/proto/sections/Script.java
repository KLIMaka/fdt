package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;

import fdk.lst.*;
import fdk.proto.Prototype;
import fdt.Fdt;
import fdt.editors.proto.accessor.BasicAccessor;
import fdt.editors.proto.adaptors.*;
import fdt.util.Ref;

public class Script extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Text m_descr;
	private Text m_id;
	private LST m_scriptLst;

	private ProtoControlAdapter m_protoAdapter;
	private IProject m_proj;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param msg
	 * @param proto
	 */
	public Script(Composite parent, ProtoAdaptorsFactory fact) {
		super(parent, SWT.NONE);
		m_protoAdapter = fact.create();
		m_proj = fact.getProject();
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				m_toolkit.dispose();
			}
		});
		m_toolkit.adapt(this);
		m_toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, false));

		Label lblScriptId = m_toolkit.createLabel(this, "Script ID", SWT.NONE);
		lblScriptId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_id = new Text(this, SWT.NONE);
		m_id.setEditable(false);
		GridData gd_id = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_id.widthHint = 70;
		m_id.setLayoutData(gd_id);
		m_protoAdapter.adoptHex(m_id, new BasicAccessor(Prototype.SCRIPT));

		m_descr = m_toolkit.createText(this, "New Text", SWT.READ_ONLY);
		m_descr.setText("");
		m_descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		int sid = (Integer) proto.get().get(Prototype.SCRIPT);
		m_protoAdapter.fill();
		if (sid != -1) {
			m_descr.setText(((ScriptLstMaker.Entry) m_scriptLst.get(sid & 0x0000ffff)).getComment());
		} else {
			m_descr.setText("None");
		}

	}

	@Override
	public void setup() throws Exception {
		m_scriptLst = Fdt.getCachedLST(m_proj, "scripts/scripts.lst");
	}

	@Override
	public Control toControl() {
		return this;
	}

}
