package ssl.editors.frm;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import ssl.SslPlugin;
import fdk.frm.FRMFrame;
import fdk.lst.BasicEntryMaker;
import fdk.lst.IEntry;
import fdk.lst.LST;
import fdk.msg.MSG;
import fdk.proto.PRO;
import fdk.proto.PRO.Type;
import fdk.proto.Prototype;

public class ProtoProvider implements IFrmListContentProvider, IFrmListLabelProvider {

    private static class ProtoEntry {
        public String   name;
        public FRMFrame frame;
    }

    private IProject                m_proj;
    private Type                    m_type;
    private Map<Object, ProtoEntry> m_cache = new HashMap<Object, ProtoProvider.ProtoEntry>();

    public ProtoProvider(IProject proj) {
        m_proj = proj;
    }

    private ProtoEntry get(Object o) {
        ProtoEntry ent = m_cache.get(o);
        if (ent == null) {
            try {
                ent = new ProtoEntry();
                IEntry lstent = (IEntry) o;
                Prototype pro = new Prototype(SslPlugin.getFile(m_proj, PRO.getProDir(m_type) + lstent.getValue())
                        .getContents());
                int id = (pro.getFields().get("protoID") & 0x0000ffff) * 100;

                int fid = pro.getFields().get(
                        m_type == Type.ITEM && pro.getFields().get("objSubType") != 1 ? "invFID" : "gndFID");
                MSG msg = SslPlugin.getCachedMsg(m_proj, PRO.getMsg(m_type));
                if (msg.get(id) != null) {
                    ent.name = msg.get(id).getMsg();
                } else {
                    ent.name = "";
                }
                ent.frame = SslPlugin.getFRM(m_proj, FID.getFileNameByFID(m_proj, fid));
                m_cache.put(o, ent);

            } catch (Exception e) {
                ent.name = "ERROR";
                ent.frame = SslPlugin.getFRM(m_proj, "art/items/reserved.frm");
            }
        }
        return ent;
    }

    @Override
    public String getText(Object o) {
        return get(o).name;
    }

    @Override
    public FRMFrame getImage(Object o) {
        return get(o).frame;
    }

    @Override
    public void inputChanged(FRMList list, Object newInput, Object oldInput) {
        m_type = (Type) newInput;
    }

    @Override
    public Object[] getElements() {
        try {
            Charset cs = Charset.forName(m_proj.getDefaultCharset());
            LST lst = new LST(SslPlugin.getFile(m_proj, PRO.getLst(m_type)).getContents(), cs, new BasicEntryMaker());
            return lst.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
