package ssl.editors.proto;

import org.eclipse.core.resources.IProject;

import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class ProtoAdaptorsFactory {

    private IProject        m_proj;
    private Ref<Prototype>  m_pro;
    private Ref<MSG>        m_msg;
    private IChangeListener m_cl;

    public ProtoAdaptorsFactory(IProject proj, Ref<Prototype> pro, Ref<MSG> msg, IChangeListener cl) {
        m_proj = proj;
        m_pro = pro;
        m_msg = msg;
        m_cl = cl;
    }

    public ProtoControlAdapter create() {
        ProtoControlAdapter adapter = new ProtoControlAdapter(m_pro, m_msg, m_cl);
        return adapter;
    }

    public IProject getProject() {
        return m_proj;
    }

    public Ref<MSG> getMsg() {
        return m_msg;
    }

    public Ref<Prototype> getPro() {
        return m_pro;
    }

    public IChangeListener getChangeListener() {
        return m_cl;
    }
}
