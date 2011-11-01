package ssl.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.MaskOffsetAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class Description extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_name;
    private Text                m_descr;

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
    public Description(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        m_protoAdapter = new ProtoControlAdapter(proto, msg, cl);
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
        m_protoAdapter.adoptMsg(m_name, new MaskOffsetAccessor("protoID", 0x0000ffff, 100, 0));

        Label lblDescription = m_toolkit.createLabel(this, "Description", SWT.NONE);
        lblDescription.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));

        m_descr = m_toolkit.createText(this, "New Text", SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        m_descr.setText("");
        TableWrapData twd_descr = new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL, 1, 1);
        twd_descr.grabHorizontal = true;
        twd_descr.heightHint = 60;
        m_descr.setLayoutData(twd_descr);
        m_protoAdapter.adoptMsg(m_descr, new MaskOffsetAccessor("protoID", 0x0000ffff, 100, 1));

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
    public void setup(IProject proj) throws Exception {}

    @Override
    public Control toControl() {
        return this;
    }

}
