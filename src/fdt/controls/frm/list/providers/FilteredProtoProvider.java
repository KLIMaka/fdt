package fdt.controls.frm.list.providers;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import fdk.lst.IEntry;
import fdk.proto.PRO;
import fdk.proto.Prototype;
import fdt.FDT;

public class FilteredProtoProvider extends ProtoProvider {

    private int m_filter;

    public FilteredProtoProvider(IProject proj, int type) {
        super(proj);
        m_filter = type;
    }

    @Override
    public Object[] getElements() {
        Object[] elems = super.getElements();
        ArrayList<Object> filtered = new ArrayList<Object>();
        for (Object o : elems) {
            if (o == noneEnt) {
                filtered.add(o);
            } else {
                IEntry ent = (IEntry) o;
                try {
                    Prototype pro = new Prototype(FDT.getFile(m_proj, PRO.getProDir(m_type) + ent.getValue())
                            .getContents());
                    if (pro.getFields().get("objSubType").equals(m_filter)) {
                        filtered.add(ent);
                    }
                } catch (Exception e) {}
            }
        }
        return filtered.toArray();
    }
}
