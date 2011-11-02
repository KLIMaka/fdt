package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class BasicAccessor implements IProtoAccessor {

    protected String m_field;

    public BasicAccessor(String field) {
        m_field = field;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return (Integer) proto.get().getFields().get(m_field);
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        proto.get().getFields().put(m_field, val);
    }

}
