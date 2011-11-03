package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import fdk.msg.MSG;
import fdk.proto.Prototype;
import fdt.FDT;
import fdt.controls.frm.FIDSelectPanel;
import fdt.editors.proto.accessor.BasicAccessor;
import fdt.editors.proto.accessor.OffsetAccessor;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.editors.proto.adaptors.ProtoControlAdapter;
import fdt.util.FID;
import fdt.util.Ref;

public class Armor extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_ac;
    private Text                m_DTNormal;
    private Text                m_DTLaser;
    private Text                m_DTFire;
    private Text                m_DTPlasma;
    private Text                m_DTElec;
    private Text                m_DTEMP;
    private Text                m_DTExpl;
    private Text                m_DRNormal;
    private Text                m_DRLaser;
    private Text                m_DRFire;
    private Text                m_DRPlasma;
    private Text                m_DRElec;
    private Text                m_DREMP;
    private Text                m_DRExpl;
    private Combo               m_perk;
    private FIDSelectPanel      m_view;
    private Button              m_male;
    private Button              m_female;
    private Label               m_label;
    private Label               m_label_1;

    private int[]               m_frames  = { 0, 0 };
    private int                 m_frame   = 0;
    private IProject            m_proj;

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
    public Armor(Composite parent, ProtoAdaptorsFactory fact) {
        super(parent, SWT.NONE);
        m_protoAdaptor = fact.create();
        m_proj = fact.getProject();
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(1, false));

        Composite composite_2 = m_toolkit.createComposite(this, SWT.NONE);
        {
            TableWrapLayout twl_composite_2 = new TableWrapLayout();
            twl_composite_2.numColumns = 5;
            composite_2.setLayout(twl_composite_2);
        }
        composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        m_toolkit.paintBordersFor(composite_2);

        Composite composite_3 = m_toolkit.createComposite(composite_2, SWT.NONE);
        {
            TableWrapLayout twl_composite_3 = new TableWrapLayout();
            twl_composite_3.numColumns = 3;
            composite_3.setLayout(twl_composite_3);
        }
        composite_3.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.FILL, 3, 2));
        m_toolkit.paintBordersFor(composite_3);
        new Label(composite_3, SWT.NONE);

        Label lblDt = m_toolkit.createLabel(composite_3, "DT", SWT.NONE);
        lblDt.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.TOP, 1, 1));

        Label lblDr = m_toolkit.createLabel(composite_3, "DR", SWT.NONE);
        lblDr.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.TOP, 1, 1));

        Label lblNormal = m_toolkit.createLabel(composite_3, "Normal", SWT.NONE);
        lblNormal.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));

        m_DTNormal = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTNormal.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_DTNormal.setText("");
        m_protoAdaptor.adopt(m_DTNormal, new BasicAccessor("DTNormal"));

        m_DRNormal = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRNormal.setText("");
        m_DRNormal.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRNormal, new BasicAccessor("DRNormal"));

        Label lblLaser = m_toolkit.createLabel(composite_3, "Laser", SWT.NONE);
        lblLaser.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblLaser.setSize(27, 15);

        m_DTLaser = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTLaser.setText("");
        m_DTLaser.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DTLaser, new BasicAccessor("DTLaser"));

        m_DRLaser = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRLaser.setText("");
        m_DRLaser.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRLaser, new BasicAccessor("DRLaser"));

        Label lblNewLabel = m_toolkit.createLabel(composite_3, "Fire", SWT.NONE);
        lblNewLabel.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblNewLabel.setSize(19, 15);

        m_DTFire = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTFire.setText("");
        m_DTFire.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DTFire, new BasicAccessor("DTFire"));

        m_DRFire = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRFire.setText("");
        m_DRFire.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRFire, new BasicAccessor("DRFire"));

        Label lblPlasma = m_toolkit.createLabel(composite_3, "Plasma", SWT.NONE);
        lblPlasma.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblPlasma.setSize(38, 15);

        m_DTPlasma = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTPlasma.setText("");
        m_DTPlasma.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DTPlasma, new BasicAccessor("DTPlasma"));

        m_DRPlasma = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRPlasma.setText("");
        m_DRPlasma.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRPlasma, new BasicAccessor("DRPlasma"));

        Label lblElectrical = m_toolkit.createLabel(composite_3, "Electrical", SWT.NONE);
        lblElectrical.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblElectrical.setSize(47, 15);

        m_DTElec = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTElec.setText("");
        m_DTElec.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DTElec, new BasicAccessor("DTElectrical"));

        m_DRElec = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRElec.setText("");
        m_DRElec.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRElec, new BasicAccessor("DRElectrical"));

        Label lblEmp = m_toolkit.createLabel(composite_3, "EMP", SWT.NONE);
        lblEmp.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblEmp.setSize(24, 15);

        m_DTEMP = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTEMP.setText("");
        m_DTEMP.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DTEMP, new BasicAccessor("DTEMP"));

        m_DREMP = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DREMP.setText("");
        m_DREMP.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DREMP, new BasicAccessor("DREMP"));

        Label lblExplosion = m_toolkit.createLabel(composite_3, "Explosion", SWT.NONE);
        lblExplosion.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.MIDDLE, 1, 1));
        lblExplosion.setSize(50, 15);

        m_DTExpl = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DTExpl.setText("");
        m_DTExpl.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE, 1, 1));
        m_protoAdaptor.adopt(m_DTExpl, new BasicAccessor("DTExplode"));

        m_DRExpl = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_DRExpl.setText("");
        m_DRExpl.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_protoAdaptor.adopt(m_DRExpl, new BasicAccessor("DRExplode"));

        m_label = new Label(composite_3, SWT.SEPARATOR | SWT.HORIZONTAL);
        m_label.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 3));
        m_toolkit.adapt(m_label, true, true);

        @SuppressWarnings("unused")
        Label lblArmorClass = m_toolkit.createLabel(composite_3, "Armor Class", SWT.NONE);

        m_ac = m_toolkit.createText(composite_3, "New Text", SWT.NONE);
        m_ac.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        m_ac.setText("100");
        m_protoAdaptor.adopt(m_ac, new BasicAccessor("ac"));

        @SuppressWarnings("unused")
        Label label = m_toolkit.createLabel(composite_3, "%", SWT.NONE);

        m_label_1 = new Label(composite_2, SWT.SEPARATOR | SWT.VERTICAL);
        m_label_1.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL, 3, 1));
        m_toolkit.adapt(m_label_1, true, true);

        @SuppressWarnings("unused")
        Label lblPerk = m_toolkit.createLabel(composite_2, "Perk", SWT.NONE);

        m_perk = new Combo(composite_2, SWT.NONE);
        m_perk.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        m_toolkit.adapt(m_perk);
        m_toolkit.paintBordersFor(m_perk);
        m_protoAdaptor.adopt(m_perk, new OffsetAccessor("perk", 1));

        m_male = new Button(composite_2, SWT.RADIO);
        m_male.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_frame = 0;
                try {
                    updateFrame();
                } catch (CoreException e1) {
                    e1.printStackTrace();
                }
            }
        });
        m_male.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));
        m_male.setSelection(true);
        m_toolkit.adapt(m_male, true, true);
        m_male.setText("Male");

        m_female = new Button(composite_2, SWT.RADIO);
        m_female.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_frame = 1;
                try {
                    updateFrame();
                } catch (CoreException e1) {
                    e1.printStackTrace();
                }
            }
        });
        m_female.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));
        m_toolkit.adapt(m_female, true, true);
        m_female.setText("Female");

        m_view = new FIDSelectPanel(composite_2, fact, "maleFID", FID.CRITTERS);
        m_view.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL, 1, 2));
        m_toolkit.adapt(m_view);
        m_toolkit.paintBordersFor(m_view);

    }

    public void fill(Ref<Prototype> proto, IProject proj) throws CoreException {
        m_protoAdaptor.fill();
        updateFrame();
    }

    private void updateFrame() throws CoreException {
        if (m_frame == 0) {
            m_view.setField("maleFID");
        } else {
            m_view.setField("femaleFID");
        }
        m_view.fill();
    }

    @Override
    public void setup() throws Exception {
        MSG perk_msg = FDT.getCachedMsg(m_proj, "text/english/game/perk.msg");

        m_perk.add("None");
        for (int i = 101; i <= 219; i++)
            m_perk.add(perk_msg.get(i).getMsg());
    }

    @Override
    public Control toControl() {
        return this;
    }

}
