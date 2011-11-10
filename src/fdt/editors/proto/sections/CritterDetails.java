package fdt.editors.proto.sections;

import java.util.Map;
import java.util.TreeMap;

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

import fdk.cfg.CFG;
import fdk.msg.MSG;
import fdk.proto.Prototype;
import fdt.FDT;
import fdt.editors.proto.accessor.BasicAccessor;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.editors.proto.adaptors.ProtoControlAdapter;
import fdt.util.Ref;

public class CritterDetails extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_exp;
    private Text                m_team;
    private Combo               m_ai;
    private Combo               m_bodyt;
    private Combo               m_killt;
    private Combo               m_dmgt;

    private ProtoControlAdapter m_protoAdapter;
    private IProject            m_proj;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @throws Exception
     */
    public CritterDetails(Composite parent, ProtoAdaptorsFactory fact) {
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
        setLayout(new GridLayout(2, false));

        Label lblAiPacket = m_toolkit.createLabel(this, "AI Packet", SWT.NONE);
        lblAiPacket.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_ai = new Combo(this, SWT.NONE);
        m_ai.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_ai);
        m_toolkit.paintBordersFor(m_ai);
        m_protoAdapter.adopt(m_ai, new BasicAccessor(Prototype.AI_PACKET));

        Label lblNewLabel = m_toolkit.createLabel(this, "Team Num", SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_team = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_team.setText("");
        m_team.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_team, new BasicAccessor(Prototype.TEAM_NUM));

        Label lblBodyType = m_toolkit.createLabel(this, "Body Type", SWT.NONE);
        lblBodyType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_bodyt = new Combo(this, SWT.NONE);
        m_bodyt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_bodyt);
        m_toolkit.paintBordersFor(m_bodyt);
        m_protoAdapter.adopt(m_bodyt, new BasicAccessor(Prototype.BODY_TYPE));

        Label lblKilltype = m_toolkit.createLabel(this, "Kill Type", SWT.NONE);
        lblKilltype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_killt = new Combo(this, SWT.NONE);
        m_killt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_killt);
        m_toolkit.paintBordersFor(m_killt);
        m_protoAdapter.adopt(m_killt, new BasicAccessor(Prototype.KILL_TYPE));

        Label lblDamageType = m_toolkit.createLabel(this, "Damage Type", SWT.NONE);
        lblDamageType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_dmgt = new Combo(this, SWT.NONE);
        m_dmgt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_dmgt);
        m_toolkit.paintBordersFor(m_dmgt);
        m_protoAdapter.adopt(m_dmgt, new BasicAccessor(Prototype.C_DMG_TYPE));

        Label lblNewLabel_1 = m_toolkit.createLabel(this, "Exp", SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_exp = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_exp.setText("");
        m_exp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_exp, new BasicAccessor(Prototype.EXP));

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_protoAdapter.fill();
    }

    @Override
    public Control toControl() {
        return this;
    }

    public void setup() throws Exception {
        CFG ai_txt = new CFG(FDT.getFile(m_proj, "data/ai.txt").getContents());
        Map<Integer, String> map = new TreeMap<Integer, String>();
        for (String aipacket : ai_txt.getCategories())
            map.put(Integer.parseInt(ai_txt.get(aipacket, "packet_num").getValue()), aipacket);
        for (int i : map.keySet())
            m_ai.add(map.get(i));

        MSG proto_msg = FDT.getCachedMsg(m_proj, "text/english/game/proto.msg");

        for (int i = 400; i <= 402; i++)
            m_bodyt.add(proto_msg.get(i).getMsg());

        for (int i = 1450; i <= 1468; i++)
            m_killt.add(proto_msg.get(i).getMsg());

        for (int i = 250; i <= 256; i++)
            m_dmgt.add(proto_msg.get(i).getMsg());
    }
}
