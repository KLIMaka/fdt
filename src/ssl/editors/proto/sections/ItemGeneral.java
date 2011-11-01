package ssl.editors.proto.sections;

import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ssl.SslPlugin;
import ssl.editors.frm.FRMPanel;
import ssl.editors.frm.FID;
import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.BasicAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class ItemGeneral extends Composite {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_size;
    private Text                m_weight;
    private Text                m_cost;
    private FRMPanel            m_inv;
    private FRMPanel            m_gnd;

    private ProtoControlAdapter m_protoAdaptor;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     */
    public ItemGeneral(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        m_protoAdaptor = new ProtoControlAdapter(proto, msg, cl);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        GridLayout gridLayout = new GridLayout(6, false);
        gridLayout.horizontalSpacing = 8;
        setLayout(gridLayout);

        Label lblSize = m_toolkit.createLabel(this, "Size", SWT.NONE);
        lblSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_size = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_size.setText("");
        m_size.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdaptor.adopt(m_size, new BasicAccessor("size"));

        Label lblWeight = m_toolkit.createLabel(this, "Weight", SWT.NONE);
        lblWeight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_weight = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_weight.setText("");
        m_weight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdaptor.adopt(m_weight, new BasicAccessor("weight"));

        Label lblCost = m_toolkit.createLabel(this, "Cost", SWT.NONE);
        lblCost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_cost = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_cost.setText("");
        m_cost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdaptor.adopt(m_cost, new BasicAccessor("cost"));

        Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
        m_toolkit.adapt(label, true, true);

        Composite composite = m_toolkit.createComposite(this, SWT.NONE);
        {
            TableWrapLayout twl_composite = new TableWrapLayout();
            twl_composite.numColumns = 2;
            composite.setLayout(twl_composite);
        }
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
        m_toolkit.paintBordersFor(composite);

        m_gnd = new FRMPanel(composite, SWT.NONE);
        m_gnd.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
        m_toolkit.adapt(m_gnd);
        m_toolkit.paintBordersFor(m_gnd);

        m_inv = new FRMPanel(composite, SWT.NONE);
        TableWrapData twd_m_inv = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
        twd_m_inv.heightHint = 148;
        m_inv.setLayoutData(twd_m_inv);
        m_toolkit.adapt(m_inv);
        m_toolkit.paintBordersFor(m_inv);

    }

    public void fill(Ref<Prototype> proto, IProject proj) throws CoreException {
        Map<String, Integer> fields = proto.get().getFields();

        m_protoAdaptor.fill();

        int fid = fields.get("gndFID");
        InputStream frms = FID.getByFID(proj, fid);
        m_gnd.setImage(frms, SslPlugin.getStdPal(proj));

        fid = fields.get("invFID");
        frms = FID.getByFID(proj, fid);
        m_inv.setImage(frms, SslPlugin.getStdPal(proj));
    }

}
