package ssl.editors.proto.sections.sets;

import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ssl.SslPlugin;
import ssl.editors.frm.FRMPanel;
import ssl.editors.frm.FID;
import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.sections.Description;
import ssl.editors.proto.sections.IFillSection;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class DefaultSet extends Composite implements IFillSection {

    private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
    private Description       m_descr;
    private FRMPanel          m_img;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     * @param cl
     */
    public DefaultSet(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(1, false));

        ExpandableComposite xpndblcmpstNewExpandablecomposite = m_toolkit.createExpandableComposite(this,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite);
        xpndblcmpstNewExpandablecomposite.setText("Description");
        xpndblcmpstNewExpandablecomposite.setExpanded(true);

        m_descr = new Description(xpndblcmpstNewExpandablecomposite, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_descr);
        m_toolkit.paintBordersFor(m_descr);
        xpndblcmpstNewExpandablecomposite.setClient(m_descr);

        ExpandableComposite xpndblcmpstImage = m_toolkit.createExpandableComposite(this, ExpandableComposite.TWISTIE);
        GridData gd_xpndblcmpstImage = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
        gd_xpndblcmpstImage.heightHint = 131;
        xpndblcmpstImage.setLayoutData(gd_xpndblcmpstImage);
        m_toolkit.paintBordersFor(xpndblcmpstImage);
        xpndblcmpstImage.setText("Image");
        xpndblcmpstImage.setExpanded(true);

        Composite composite = m_toolkit.createComposite(xpndblcmpstImage, SWT.NONE);
        m_toolkit.paintBordersFor(composite);
        xpndblcmpstImage.setClient(composite);
        composite.setLayout(new GridLayout(1, false));

        m_img = new FRMPanel(composite, SWT.NONE);
        GridData gd_img = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_img.heightHint = 233;
        m_img.setLayoutData(gd_img);
        m_img.setBounds(0, 0, 92, 68);
        m_toolkit.adapt(m_img);
        m_toolkit.paintBordersFor(m_img);

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_descr.fill(proto, proj);

        int fid = (Integer) proto.get().getFields().get("gndFID");
        InputStream frms = FID.getByFID(proj, fid);
        m_img.setImage(frms, SslPlugin.getStdPal(proj));
    }

    @Override
    public Control toControl() {
        return this;
    }

    @Override
    public void setup(IProject proj) throws Exception {}

}