package ssl.editors.proto.sections;

import java.nio.charset.Charset;
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

import ssl.SslPlugin;
import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.BasicAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.cfg.CFG;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class CritterDetails extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_exp;
    private Text                m_team;
    private Combo               m_ai;
    private Combo               m_bodyt;
    private Combo               m_killt;
    private Combo               m_dmgt;

    private ProtoControlAdapter m_protoAdapter;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @throws Exception
     */
    public CritterDetails(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg,
            IChangeListener changeListener) {
        super(parent, style);
        m_protoAdapter = new ProtoControlAdapter(proto, msg, changeListener);
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
        m_protoAdapter.adopt(m_ai, new BasicAccessor("aiPacket"));

        Label lblNewLabel = m_toolkit.createLabel(this, "Team Num", SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_team = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_team.setText("");
        m_team.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_team, new BasicAccessor("teamNum"));

        Label lblBodyType = m_toolkit.createLabel(this, "Body Type", SWT.NONE);
        lblBodyType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_bodyt = new Combo(this, SWT.NONE);
        m_bodyt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_bodyt);
        m_toolkit.paintBordersFor(m_bodyt);
        m_protoAdapter.adopt(m_bodyt, new BasicAccessor("bodyType"));

        Label lblKilltype = m_toolkit.createLabel(this, "Kill Type", SWT.NONE);
        lblKilltype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_killt = new Combo(this, SWT.NONE);
        m_killt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_killt);
        m_toolkit.paintBordersFor(m_killt);
        m_protoAdapter.adopt(m_killt, new BasicAccessor("killType"));

        Label lblDamageType = m_toolkit.createLabel(this, "Damage Type", SWT.NONE);
        lblDamageType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_dmgt = new Combo(this, SWT.NONE);
        m_dmgt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_toolkit.adapt(m_dmgt);
        m_toolkit.paintBordersFor(m_dmgt);
        m_protoAdapter.adopt(m_dmgt, new BasicAccessor("damageType"));

        Label lblNewLabel_1 = m_toolkit.createLabel(this, "Exp", SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_exp = m_toolkit.createText(this, "New Text", SWT.NONE);
        m_exp.setText("");
        m_exp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        m_protoAdapter.adopt(m_exp, new BasicAccessor("exp"));

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        m_protoAdapter.fill();
    }

    @Override
    public Control toControl() {
        return this;
    }

    public void setup(IProject proj) throws Exception {
        CFG ai_txt = new CFG(SslPlugin.getFile(proj, "data/ai.txt").getContents());
        Map<Integer, String> map = new TreeMap<Integer, String>();
        for (String aipacket : ai_txt.getCategories())
            map.put(Integer.parseInt(ai_txt.get(aipacket, "packet_num").getValue()), aipacket);
        for (int i : map.keySet())
            m_ai.add(map.get(i));

        Charset cs = Charset.forName(proj.getDefaultCharset());
        MSG proto_msg = new MSG(SslPlugin.getFile(proj, "text/english/game/proto.msg").getContents(), cs);

        for (int i = 400; i <= 402; i++)
            m_bodyt.add(proto_msg.get(i).getMsg());

        for (int i = 1450; i <= 1468; i++)
            m_killt.add(proto_msg.get(i).getMsg());

        for (int i = 250; i <= 256; i++)
            m_dmgt.add(proto_msg.get(i).getMsg());
    }
}