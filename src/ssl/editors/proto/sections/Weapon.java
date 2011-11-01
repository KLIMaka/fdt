package ssl.editors.proto.sections;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

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
import ssl.editors.frm.FRMPanel;
import ssl.editors.frm.FID;
import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.BasicAccessor;
import ssl.editors.proto.accessor.MaskShiftAccessor;
import ssl.editors.proto.accessor.OffsetAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import ssl.editors.proto.accessor.ZeroAddAccessor;
import fdk.lst.BasicEntryMaker;
import fdk.lst.IEntry;
import fdk.lst.LST;
import fdk.msg.MSG;
import fdk.msg.MsgEntry;
import fdk.proto.PRO;
import fdk.proto.PRO.Type;
import fdk.proto.Prototype;

public class Weapon extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_mindmg;
    private Text                m_maxdmg;
    private Text                m_range1;
    private Text                m_ap1;
    private Text                m_range2;
    private Text                m_ap2;
    private Text                m_rounds;
    private Text                m_crit;
    private FRMPanel            m_ammo;
    private Combo               m_anim;
    private Combo               m_dmgt;
    private Combo               m_perk;
    private Combo               m_caliber;
    private Text                m_maxammo;
    private Text                m_mins;
    private LST                 m_itm_lst;
    private Combo               m_at1;
    private Combo               m_at2;

    private ProtoControlAdapter m_protoAdapter;
    private Combo               m_proj;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     * @param changeListener
     */
    public Weapon(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener changeListener) {
        super(parent, style);
        m_protoAdapter = new ProtoControlAdapter(proto, msg, changeListener);
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

        Label lblAnumCode = m_toolkit.createLabel(this, "Anim code", SWT.NONE);
        lblAnumCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_anim = new Combo(this, SWT.NONE);
        m_anim.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        m_toolkit.adapt(m_anim);
        m_toolkit.paintBordersFor(m_anim);
        m_protoAdapter.adopt(m_anim, new BasicAccessor("animCode"));

        Label label_1 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
        label_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 6));
        m_toolkit.adapt(label_1, true, true);

        Label lblRounds = m_toolkit.createLabel(this, "Rounds", SWT.NONE);
        lblRounds.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_rounds = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_rounds.setText("");
        m_rounds.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_rounds, new BasicAccessor("rounds"));

        Label lblDamage = m_toolkit.createLabel(this, "Damage", SWT.NONE);
        lblDamage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_mindmg = m_toolkit.createText(this, "New Text", SWT.NONE);
        GridData gd_mindmg = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_mindmg.widthHint = 39;
        m_mindmg.setLayoutData(gd_mindmg);
        m_mindmg.setText("000");
        m_protoAdapter.adopt(m_mindmg, new BasicAccessor("minDmg"));

        Label lblNewLabel = m_toolkit.createLabel(this, "-", SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_maxdmg = m_toolkit.createText(this, "New Text", SWT.NONE);
        GridData gd_maxdmg = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_maxdmg.widthHint = 42;
        m_maxdmg.setLayoutData(gd_maxdmg);
        m_maxdmg.setText("000");
        m_protoAdapter.adopt(m_maxdmg, new BasicAccessor("maxDmg"));

        Label lblCaliber = m_toolkit.createLabel(this, "Caliber", SWT.NONE);
        lblCaliber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_caliber = new Combo(this, SWT.NONE);
        m_caliber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_caliber);
        m_toolkit.paintBordersFor(m_caliber);
        m_protoAdapter.adopt(m_caliber, new BasicAccessor("caliber"));

        Label lblDamagetype = m_toolkit.createLabel(this, "Damage type", SWT.NONE);
        lblDamagetype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_dmgt = new Combo(this, SWT.NONE);
        m_dmgt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        m_toolkit.adapt(m_dmgt);
        m_toolkit.paintBordersFor(m_dmgt);
        m_protoAdapter.adopt(m_dmgt, new BasicAccessor("dmgType"));

        Label lblCritFail = m_toolkit.createLabel(this, "Crit fail", SWT.NONE);
        lblCritFail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_crit = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_crit.setText("");
        m_crit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_crit, new BasicAccessor("critFail"));

        Label lblPerk = m_toolkit.createLabel(this, "Perk", SWT.NONE);
        lblPerk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_perk = new Combo(this, SWT.NONE);
        m_perk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        m_toolkit.adapt(m_perk);
        m_toolkit.paintBordersFor(m_perk);
        m_protoAdapter.adopt(m_perk, new OffsetAccessor("perk", 1));

        Label lblMaxAmmo = m_toolkit.createLabel(this, "Max ammo", SWT.NONE);
        lblMaxAmmo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_maxammo = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_maxammo.setText("");
        m_maxammo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_maxammo, new BasicAccessor("maxAmmo"));

        Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        m_toolkit.adapt(label, true, true);

        m_ammo = new FRMPanel(this, SWT.NONE);
        GridData gd_ammo = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2);
        gd_ammo.heightHint = 100;
        m_ammo.setLayoutData(gd_ammo);
        m_toolkit.adapt(m_ammo);
        m_toolkit.paintBordersFor(m_ammo);

        Composite composite = new Composite(this, SWT.NONE);
        GridLayout gl_composite = new GridLayout(4, false);
        gl_composite.horizontalSpacing = 8;
        composite.setLayout(gl_composite);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        m_toolkit.adapt(composite);
        m_toolkit.paintBordersFor(composite);
        new Label(composite, SWT.NONE);

        Label lblNewLabel_1 = m_toolkit.createLabel(composite, "Attack type", SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        Label lblNewLabel_2 = m_toolkit.createLabel(composite, "Range", SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        Label lblNewLabel_3 = m_toolkit.createLabel(composite, "AP cost", SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        Label lblMode = m_toolkit.createLabel(composite, "Mode 1", SWT.NONE);
        lblMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_at1 = new Combo(composite, SWT.NONE);
        m_at1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_at1);
        m_toolkit.paintBordersFor(m_at1);
        m_protoAdapter.adopt(m_at1, new MaskShiftAccessor("flagsExt", 0xf, 0));

        m_range1 = m_toolkit.createText(composite, "New Text", SWT.NONE);
        m_range1.setText("");
        m_range1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_range1, new BasicAccessor("maxRange1"));

        m_ap1 = m_toolkit.createText(composite, "New Text", SWT.NONE);
        m_ap1.setText("");
        m_ap1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_ap1, new BasicAccessor("apCost1"));

        Label lblMode_1 = m_toolkit.createLabel(composite, "Mode 2", SWT.NONE);
        lblMode_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_at2 = new Combo(composite, SWT.NONE);
        m_at2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_at2);
        m_toolkit.paintBordersFor(m_at2);
        m_protoAdapter.adopt(m_at2, new MaskShiftAccessor("flagsExt", 0xf, 4));

        m_range2 = m_toolkit.createText(composite, "New Text", SWT.NONE);
        m_range2.setText("");
        m_range2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_range2, new BasicAccessor("maxRange2"));

        m_ap2 = m_toolkit.createText(composite, "New Text", SWT.NONE);
        m_ap2.setText("");
        m_ap2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_ap2, new BasicAccessor("apCost2"));

        Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        m_toolkit.adapt(label_2, true, true);

        Label lblMinStr = m_toolkit.createLabel(composite, "Min STR", SWT.NONE);
        lblMinStr.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_mins = m_toolkit.createText(composite, "New Text", SWT.NONE);
        m_mins.setText("");
        m_mins.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_mins, new BasicAccessor("minST"));

        m_proj = new Combo(composite, SWT.NONE);
        m_proj.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        m_toolkit.adapt(m_proj);
        m_toolkit.paintBordersFor(m_proj);
        m_protoAdapter.adopt(m_proj, new ZeroAddAccessor("projFID", 0x05000000));

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        Map<String, Integer> fields = proto.get().getFields();

        m_protoAdapter.fill();

        m_ammo.resetImage();
        if ((Integer) fields.get("ammoPID") != -1) {
            int idx = (Integer) fields.get("ammoPID");
            idx &= 0x0000ffff;
            IEntry ent = m_itm_lst.get(idx - 1);
            if (ent != null) {
                Prototype apro = new Prototype(SslPlugin.getFile(proj, PRO.getProDir(Type.ITEM) + ent.getValue())
                        .getContents());
                int fid = apro.getFields().get("invFID");
                InputStream frms = FID.getByFID(proj, fid);
                m_ammo.setImage(frms, SslPlugin.getStdPal(proj));
            }
        }

    }

    @Override
    public void setup(IProject proj) throws Exception {
        String[] anims = { "None", "Knife (D)", "Club (E)", "2Hnd Club (F)", "Spear (G)", "Pistol (H)", "Uzi (I)",
                "Rifle (J)", "Laser (K)", "Minigun (L)", "Rocket Launcher (M)" };
        for (String a : anims)
            m_anim.add(a);

        Charset cs = Charset.forName(proj.getDefaultCharset());
        MSG proto_msg = new MSG(SslPlugin.getFile(proj, "text/english/game/proto.msg").getContents(), cs);
        for (int i = 250; i <= 256; i++)
            m_dmgt.add(proto_msg.get(i).getMsg());

        MSG perk_msg = new MSG(SslPlugin.getFile(proj, "text/english/game/perk.msg").getContents(), cs);
        m_perk.add("None");
        for (int i = 101; i <= 219; i++)
            m_perk.add(perk_msg.get(i).getMsg());

        for (int i = 300; i <= 318; i++)
            m_caliber.add(proto_msg.get(i).getMsg());

        m_itm_lst = new LST(SslPlugin.getFile(proj, PRO.getLst(Type.ITEM)).getContents(), cs, new BasicEntryMaker());

        String[] atts = { "Stand", "Throw punch", "Kick leg", "Swing", "Thrust", "Throw", "Single", "Burst",
                "Continous" };
        for (String at : atts) {
            m_at1.add(at);
            m_at2.add(at);
        }

        MSG projMsg = new MSG(SslPlugin.getFile(proj, PRO.getMsg(PRO.Type.MISC)).getContents(), cs);
        m_proj.add("None");
        MsgEntry ent;
        int i = 100;
        while ((ent = projMsg.get(i)) != null) {
            m_proj.add(ent.getMsg());
            i += 100;
        }
    }

    @Override
    public Control toControl() {
        return this;
    }

}
