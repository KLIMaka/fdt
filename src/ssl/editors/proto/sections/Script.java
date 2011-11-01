package ssl.editors.proto.sections;

import java.nio.charset.Charset;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import fdk.lst.LST;
import fdk.lst.ScriptLstMaker;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class Script extends Composite implements IFillSection {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Text                m_descr;
    private Text                m_id;
    private LST                 m_scriptLst;

    private ProtoControlAdapter m_protoAdapter;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     */
    public Script(Composite parent, int style, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
        super(parent, style);
        m_protoAdapter = new ProtoControlAdapter(proto, msg, cl);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(3, false));

        Label lblScriptId = m_toolkit.createLabel(this, "Script ID", SWT.NONE);
        lblScriptId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

        m_id = new Text(this, SWT.NONE);
        m_id.setEditable(false);
        GridData gd_id = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_id.widthHint = 70;
        m_id.setLayoutData(gd_id);
        m_protoAdapter.adoptHex(m_id, new BasicAccessor("scriptID"));

        m_descr = m_toolkit.createText(this, "New Text", SWT.READ_ONLY);
        m_descr.setText("");
        m_descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    }

    @Override
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
        int sid = (Integer) proto.get().getFields().get("scriptID");
        m_protoAdapter.fill();
        if (sid != -1)
            m_descr.setText(((ScriptLstMaker.Entry) m_scriptLst.get(sid & 0x0000ffff)).getComment());
        else m_descr.setText("None");

    }

    @Override
    public void setup(IProject proj) throws Exception {
        Charset cs = Charset.forName(proj.getDefaultCharset());
        m_scriptLst = new LST(SslPlugin.getFile(proj, "scripts/scripts.lst").getContents(), cs, new ScriptLstMaker());
    }

    @Override
    public Control toControl() {
        return this;
    }

}
