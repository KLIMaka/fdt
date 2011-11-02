package ssl.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ssl.SslPlugin;
import ssl.editors.proto.ProtoAdaptorsFactory;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.BasicAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class Ammo extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_ac;
    private Text                m_dr;
    private Text                m_mult;
    private Text                m_div;
    private Text                m_q;
    private Combo               m_caliber;

    private ProtoControlAdapter m_protoAdapter;
    private IProject            m_proj;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param changeListener
     */
    public Ammo(Composite parent, ProtoAdaptorsFactory fact) {
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
        GridLayout gridLayout = new GridLayout(7, false);
        gridLayout.horizontalSpacing = 8;
        setLayout(gridLayout);

        Label lblCaliber = m_toolkit.createLabel(this, "Caliber", SWT.NONE);
        lblCaliber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_caliber = new Combo(this, SWT.NONE);
        m_caliber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        m_toolkit.adapt(m_caliber);
        m_toolkit.paintBordersFor(m_caliber);
        m_protoAdapter.adopt(m_caliber, new BasicAccessor("caliber"));

        Label label = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 3));
        m_toolkit.adapt(label, true, true);

        Label lblNewLabel = m_toolkit.createLabel(this, "Quantity", SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_q = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_q.setText("");
        m_q.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_q, new BasicAccessor("quantity"));

        Label lblAcAdjust = m_toolkit.createLabel(this, "AC adjust", SWT.NONE);
        lblAcAdjust.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_ac = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_ac.setText("");
        m_ac.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        m_protoAdapter.adopt(m_ac, new BasicAccessor("acAjust"));

        Label lblDrAdjust = m_toolkit.createLabel(this, "DR adjust", SWT.NONE);
        lblDrAdjust.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_dr = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_dr.setText("");
        m_dr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_dr, new BasicAccessor("drAjust"));

        Label lblDmgMult = m_toolkit.createLabel(this, "Dmg mod", SWT.NONE);
        lblDmgMult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_mult = m_toolkit.createText(this, "New Text", SWT.NONE);
        GridData gd_mult = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_mult.widthHint = 20;
        gd_mult.minimumWidth = 20;
        m_mult.setLayoutData(gd_mult);
        m_mult.setText("");
        m_protoAdapter.adopt(m_mult, new BasicAccessor("dmgMult"));

        Label label_1 = m_toolkit.createLabel(this, "/", SWT.NONE);
        label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_div = m_toolkit.createText(this, "New Text", SWT.NONE);
        GridData gd_div = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_div.widthHint = 20;
        gd_div.minimumWidth = 20;
        m_div.setLayoutData(gd_div);
        m_div.setText("");
        m_protoAdapter.adopt(m_div, new BasicAccessor("dmgDiv"));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_protoAdapter.fill();
    }

    @Override
    public void setup() throws Exception {
        MSG proto_msg = SslPlugin.getCachedMsg(m_proj, "text/english/game/proto.msg");

        for (int i = 300; i <= 318; i++)
            m_caliber.add(proto_msg.get(i).getMsg());
    }

    @Override
    public Control toControl() {
        return this;
    }

}
