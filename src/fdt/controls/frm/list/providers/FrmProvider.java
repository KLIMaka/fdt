package fdt.controls.frm.list.providers;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import fdk.frm.FRMFrame;
import fdk.lst.BasicEntryMaker;
import fdk.lst.IEntry;
import fdk.lst.LST;
import fdt.FDT;
import fdt.controls.frm.list.FRMList;
import fdt.util.FID;

public class FrmProvider implements IFrmListContentProvider, IFrmListLabelProvider {

    private static class CacheEntry {
        public String   name;
        public FRMFrame frame;
    }

    private IProject                m_proj;
    private int                     m_type;
    private Map<Object, CacheEntry> m_cache = new HashMap<Object, CacheEntry>();

    public FrmProvider(IProject proj) {
        m_proj = proj;
    }

    private CacheEntry get(Object o) {
        CacheEntry ent = m_cache.get(o);
        if (ent == null) {
            ent = new CacheEntry();
            m_cache.put(o, ent);

            IEntry e = (IEntry) o;
            ent.name = e.getValue();
            int fid = (e.getIndex() - 1) | (m_type << 24);
            ent.frame = FDT.getFRM(m_proj, FID.getFileNameByFID(m_proj, fid));
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
        m_type = (Integer) newInput;
    }

    @Override
    public Object[] getElements() {
        try {
            Charset cs = Charset.forName(m_proj.getDefaultCharset());
            LST lst = new LST(FDT.getFile(m_proj, "art/" + FID.types[m_type] + "/" + FID.types[m_type] + ".lst")
                    .getContents(), cs, new BasicEntryMaker());
            return lst.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
