package ssl.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ssl.editors.proto.ProtoAdaptorsFactory;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.BasicAccessor;
import ssl.editors.proto.accessor.MaskAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.proto.Prototype;

public class Container extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_size;
    private Button              m_pickup;
    private Button              m_hands;

    private ProtoControlAdapter m_protoAdaptor;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     * @param changeListener
     */
    public Container(Composite parent, ProtoAdaptorsFactory fact) {
        super(parent, SWT.NONE);
        m_protoAdaptor = fact.create();
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(4, false));

        Label lblMaxSize = m_toolkit.createLabel(this, "Max size", SWT.NONE);
        lblMaxSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_size = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_size.setText("");
        m_size.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdaptor.adopt(m_size, new BasicAccessor("maxSize"));

        m_pickup = new Button(this, SWT.CHECK);
        m_toolkit.adapt(m_pickup, true, true);
        m_pickup.setText("Cannot Pick Up ");
        m_protoAdaptor.adopt(m_pickup, new MaskAccessor("containerFlags", 0x00000001));

        m_hands = new Button(this, SWT.CHECK);
        m_toolkit.adapt(m_hands, true, true);
        m_hands.setText("Magic Hands Grnd");
        m_protoAdaptor.adopt(m_hands, new MaskAccessor("containerFlags", 0x00000008));

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_protoAdaptor.fill();
    }

    @Override
    public void setup() throws Exception {}

    @Override
    public Control toControl() {
        return this;
    }

}
