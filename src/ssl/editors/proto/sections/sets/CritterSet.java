package ssl.editors.proto.sections.sets;

import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import ssl.editors.proto.sections.Critter;
import ssl.editors.proto.sections.CritterDetails;
import ssl.editors.proto.sections.CritterFlags;
import ssl.editors.proto.sections.Description;
import ssl.editors.proto.sections.IFillSection;
import ssl.editors.proto.sections.Lighting;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class CritterSet extends Composite implements IFillSection {

    private Expander            m_expander;

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Description         m_descr;
    private Lighting            m_lighting;
    private Critter             m_critter;
    private ExpandableComposite m_expandableComposite;
    private Composite           m_composite;
    private ScrolledComposite   m_scrolledComposite;
    private Composite           m_composite_1;
    private CritterFlags        m_critterFlags;
    private FRMPanel            m_img;
    private CritterDetails      m_critterDetails;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     * @param cl
     */
    public CritterSet(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(1, false));

        m_scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
        m_scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        m_toolkit.adapt(m_scrolledComposite);
        m_toolkit.paintBordersFor(m_scrolledComposite);
        m_scrolledComposite.setExpandHorizontal(true);
        m_scrolledComposite.setExpandVertical(true);

        m_composite = new Composite(m_scrolledComposite, SWT.NONE);
        m_composite.setLayout(new GridLayout(1, false));
        m_toolkit.adapt(m_composite);
        m_toolkit.paintBordersFor(m_composite);

        m_expander = new Expander(m_composite, m_scrolledComposite);

        ExpandableComposite xpndblcmpstNewExpandablecomposite = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        xpndblcmpstNewExpandablecomposite.addExpansionListener(m_expander);
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite);
        xpndblcmpstNewExpandablecomposite.setText("Description");
        xpndblcmpstNewExpandablecomposite.setExpanded(true);

        m_descr = new Description(xpndblcmpstNewExpandablecomposite, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_descr);
        m_toolkit.paintBordersFor(m_descr);
        xpndblcmpstNewExpandablecomposite.setClient(m_descr);

        ExpandableComposite xpndblcmpstCritter = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstCritter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        xpndblcmpstCritter.addExpansionListener(m_expander);
        m_toolkit.paintBordersFor(xpndblcmpstCritter);
        xpndblcmpstCritter.setText("Critter");
        xpndblcmpstCritter.setExpanded(true);

        m_critter = new Critter(xpndblcmpstCritter, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_critter);
        m_toolkit.paintBordersFor(m_critter);
        xpndblcmpstCritter.setClient(m_critter);

        m_expandableComposite = m_toolkit.createExpandableComposite(m_composite, ExpandableComposite.TWISTIE);
        m_expandableComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(m_expandableComposite);
        m_expandableComposite.addExpansionListener(m_expander);
        m_expandableComposite.setText("Flags");
        m_expandableComposite.setExpanded(true);

        m_composite_1 = m_toolkit.createComposite(m_expandableComposite, SWT.NONE);
        m_toolkit.paintBordersFor(m_composite_1);
        m_expandableComposite.setClient(m_composite_1);
        m_composite_1.setLayout(new GridLayout(2, false));

        m_critterFlags = new CritterFlags(m_composite_1, SWT.NONE, proto, msg, cl);
        m_critterFlags.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
        m_toolkit.adapt(m_critterFlags);
        m_toolkit.paintBordersFor(m_critterFlags);

        m_critterDetails = new CritterDetails(m_composite_1, SWT.NONE, proto, msg, cl);
        m_critterDetails.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        m_toolkit.adapt(m_critterDetails);
        m_toolkit.paintBordersFor(m_critterDetails);

        m_img = new FRMPanel(m_composite_1, SWT.NONE);
        m_img.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        m_toolkit.adapt(m_img);
        m_toolkit.paintBordersFor(m_img);

        ExpandableComposite xpndblcmpstLighting = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstLighting.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        xpndblcmpstLighting.addExpansionListener(m_expander);
        m_toolkit.paintBordersFor(xpndblcmpstLighting);
        xpndblcmpstLighting.setText("Lighting");
        xpndblcmpstLighting.setExpanded(true);

        m_lighting = new Lighting(xpndblcmpstLighting, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_lighting);
        m_toolkit.paintBordersFor(m_lighting);
        xpndblcmpstLighting.setClient(m_lighting);
        m_scrolledComposite.setContent(m_composite);
        m_scrolledComposite.setMinSize(m_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_descr.fill(proto, proj);
        m_lighting.fill(proto, proj);
        m_critter.fill(proto, proj);
        m_critterFlags.fill(proto, proj);
        m_critterDetails.fill(proto, proj);

        int fid = (Integer) proto.get().getFields().get("gndFID");
        InputStream frms = FID.getByFID(proj, fid);
        m_img.setImage(frms, SslPlugin.getStdPal(proj));
    }

    @Override
    public Control toControl() {
        return this;
    }

    @Override
    public void setup(IProject proj) throws Exception {
        m_critterDetails.setup(proj);
        m_critter.setup(proj);
    }
}
