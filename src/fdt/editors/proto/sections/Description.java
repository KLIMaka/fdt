package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.*;

import fdk.proto.Prototype;
import fdt.editors.proto.accessor.MaskOffsetAccessor;
import fdt.editors.proto.adaptors.*;
import fdt.util.Ref;

public class Description extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Text m_name;
	private Text m_descr;

	private ProtoControlAdapter m_protoAdapter;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param msg
	 * @param proto
	 * @param cl
	 */
	public Description(Composite parent, ProtoAdaptorsFactory fact) {
		super(parent, SWT.NONE);
		m_protoAdapter = fact.create();
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				m_toolkit.dispose();
			}
		});
		m_toolkit.adapt(this);
		m_toolkit.paintBordersFor(this);
		{
			TableWrapLayout tableWrapLayout = new TableWrapLayout();
			tableWrapLayout.numColumns = 2;
			setLayout(tableWrapLayout);
		}

		Label lblName = m_toolkit.createLabel(this, "Name", SWT.NONE);
		lblName.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

		m_name = m_toolkit.createText(this, "New Text", SWT.NONE);
		m_name.setText("");
		m_name.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 1));
		m_protoAdapter.adoptMsg(m_name, new MaskOffsetAccessor(Prototype.PROTO_ID, 0x0000ffff, 100, 0));

		Label lblDescription = m_toolkit.createLabel(this, "Description", SWT.NONE);
		lblDescription.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

		m_descr = m_toolkit.createText(this, "New Text", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		m_descr.setText("");
		TableWrapData twd_descr = new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL, 1, 1);
		twd_descr.grabHorizontal = true;
		twd_descr.heightHint = 60;
		m_descr.setLayoutData(twd_descr);
		m_protoAdapter.adoptMsg(m_descr, new MaskOffsetAccessor(Prototype.PROTO_ID, 0x0000ffff, 100, 1));

	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean f) {
		Point p = super.computeSize(wHint, hHint, f);
		p.x = 0;
		return p;
	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		m_protoAdapter.fill();
	}

	@Override
	public void setup() throws Exception {
	}

	@Override
	public Control toControl() {
		return this;
	}

}
