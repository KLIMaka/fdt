package ssl.editors.proto.sections.sets;

import java.util.Map;
import java.util.TreeMap;

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

import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.sections.Ammo;
import ssl.editors.proto.sections.Armor;
import ssl.editors.proto.sections.Container;
import ssl.editors.proto.sections.Description;
import ssl.editors.proto.sections.Drugs;
import ssl.editors.proto.sections.IFillSection;
import ssl.editors.proto.sections.ItemFlags;
import ssl.editors.proto.sections.ItemGeneral;
import ssl.editors.proto.sections.Key;
import ssl.editors.proto.sections.Lighting;
import ssl.editors.proto.sections.Misk;
import ssl.editors.proto.sections.Script;
import ssl.editors.proto.sections.Weapon;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class GenericItemSet extends Composite implements IFillSection {

    private final FormToolkit         m_toolkit  = new FormToolkit(Display.getCurrent());
    private Description               m_descr;
    private Lighting                  m_lighting;
    private ItemFlags                 m_itemFlags;
    private ItemGeneral               m_itemGeneral;
    private Composite                 m_composite;
    private Expander                  m_expander;
    private ExpandableComposite       m_details;

    private Map<String, IFillSection> m_sections = new TreeMap<String, IFillSection>();
    private ScrolledComposite         m_scroll;
    private ExpandableComposite       m_expandableComposite;
    private Script                    m_script;

    private Ref<Prototype>            m_proto;
    private Ref<MSG>                  m_msg;
    private IChangeListener           m_changeListener;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     * @param cl
     */
    public GenericItemSet(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        m_proto = proto;
        m_msg = msg;
        m_changeListener = cl;

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(1, false));

        m_scroll = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_scroll = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_scroll.heightHint = 786;
        m_scroll.setLayoutData(gd_scroll);
        m_toolkit.adapt(m_scroll);
        m_toolkit.paintBordersFor(m_scroll);
        m_scroll.setExpandHorizontal(true);
        m_scroll.setExpandVertical(true);

        m_composite = new Composite(m_scroll, SWT.NONE);
        m_toolkit.adapt(m_composite);
        m_toolkit.paintBordersFor(m_composite);
        m_composite.setLayout(new GridLayout(1, false));

        m_expander = new Expander(m_composite, m_scroll);

        ExpandableComposite xpndblcmpstNewExpandablecomposite = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite.addExpansionListener(m_expander);
        xpndblcmpstNewExpandablecomposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite);
        xpndblcmpstNewExpandablecomposite.setText("Description");
        xpndblcmpstNewExpandablecomposite.setExpanded(true);

        m_descr = new Description(xpndblcmpstNewExpandablecomposite, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_descr);
        m_toolkit.paintBordersFor(m_descr);
        xpndblcmpstNewExpandablecomposite.setClient(m_descr);

        m_details = m_toolkit.createExpandableComposite(m_composite, ExpandableComposite.TWISTIE);
        m_details.addExpansionListener(m_expander);
        m_details.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(m_details);
        m_details.setText("Details");
        m_details.setExpanded(true);

        ExpandableComposite xpndblcmpstNewExpandablecomposite_2 = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite_2.addExpansionListener(m_expander);
        xpndblcmpstNewExpandablecomposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite_2);
        xpndblcmpstNewExpandablecomposite_2.setText("General");
        xpndblcmpstNewExpandablecomposite_2.setExpanded(true);

        m_itemGeneral = new ItemGeneral(xpndblcmpstNewExpandablecomposite_2, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_itemGeneral);
        m_toolkit.paintBordersFor(m_itemGeneral);
        xpndblcmpstNewExpandablecomposite_2.setClient(m_itemGeneral);

        m_expandableComposite = m_toolkit.createExpandableComposite(m_composite, ExpandableComposite.TWISTIE);
        m_expandableComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(m_expandableComposite);
        m_expandableComposite.addExpansionListener(m_expander);
        m_expandableComposite.setText("Script");
        m_expandableComposite.setExpanded(true);

        m_script = new Script(m_expandableComposite, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_script);
        m_toolkit.paintBordersFor(m_script);
        m_expandableComposite.setClient(m_script);

        ExpandableComposite xpndblcmpstNewExpandablecomposite_3 = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite_3.addExpansionListener(m_expander);
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite_3);
        xpndblcmpstNewExpandablecomposite_3.setText("Flags");
        xpndblcmpstNewExpandablecomposite_3.setExpanded(true);

        m_itemFlags = new ItemFlags(xpndblcmpstNewExpandablecomposite_3, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_itemFlags);
        m_toolkit.paintBordersFor(m_itemFlags);
        xpndblcmpstNewExpandablecomposite_3.setClient(m_itemFlags);

        ExpandableComposite xpndblcmpstNewExpandablecomposite_1 = m_toolkit.createExpandableComposite(m_composite,
                ExpandableComposite.TWISTIE);
        xpndblcmpstNewExpandablecomposite_1.addExpansionListener(m_expander);
        xpndblcmpstNewExpandablecomposite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.paintBordersFor(xpndblcmpstNewExpandablecomposite_1);
        xpndblcmpstNewExpandablecomposite_1.setText("Lighting");
        xpndblcmpstNewExpandablecomposite_1.setExpanded(true);

        m_lighting = new Lighting(xpndblcmpstNewExpandablecomposite_1, SWT.NONE, proto, msg, cl);
        m_toolkit.adapt(m_lighting);
        m_toolkit.paintBordersFor(m_lighting);
        xpndblcmpstNewExpandablecomposite_1.setClient(m_lighting);
        m_scroll.setContent(m_composite);
        m_scroll.setMinSize(m_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    private IFillSection getDetails(Ref<Prototype> proto) {
        int subtype = (Integer) proto.get().getFields().get("objSubType");

        switch (subtype) {
        case 0:
            return m_sections.get("armor");
        case 1:
            return m_sections.get("container");
        case 2:
            return m_sections.get("drugs");
        case 3:
            return m_sections.get("weapon");
        case 4:
            return m_sections.get("ammo");
        case 5:
            return m_sections.get("misk");
        case 6:
            return m_sections.get("key");
        default:
            return null;
        }
    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_descr.fill(proto, proj);
        m_lighting.fill(proto, proj);
        m_itemGeneral.fill(proto, proj);
        m_itemFlags.fill(proto, proj);
        m_script.fill(proto, proj);

        IFillSection sect = getDetails(proto);
        sect.fill(proto, proj);
        if (m_details.getClient() != sect) {
            if (m_details.getClient() != null) m_details.getClient().setVisible(false);
            m_details.setClient(sect.toControl());
            m_details.getClient().setVisible(true);
            m_composite.layout();
            m_scroll.setMinSize(m_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }

    }

    private IFillSection add(IFillSection sel, String name) {
        m_toolkit.adapt((Composite) sel.toControl());
        m_toolkit.paintBordersFor((Composite) sel.toControl());
        m_sections.put(name, sel);
        return sel;
    }

    @Override
    public void setup(IProject proj) throws Exception {
        add(new Armor(m_details, SWT.NONE, m_proto, m_msg, m_changeListener), "armor").setup(proj);
        add(new Container(m_details, SWT.NONE, m_proto, m_msg, m_changeListener), "container").setup(proj);
        add(new Drugs(m_details, SWT.NONE, m_proto, m_msg, m_changeListener), "drugs").setup(proj);
        add(new Weapon(m_details, SWT.NONE, m_proto, m_msg, m_changeListener), "weapon").setup(proj);
        add(new Ammo(m_details, SWT.NONE, m_proto, m_msg, m_changeListener), "ammo").setup(proj);
        add(new Misk(m_details, SWT.NONE), "misk").setup(proj);
        add(new Key(m_details, SWT.NONE), "key").setup(proj);

        m_script.setup(proj);
    }

    @Override
    public Control toControl() {
        return this;
    }
}
