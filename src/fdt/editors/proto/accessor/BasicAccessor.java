package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class BasicAccessor implements IProtoAccessor {

    protected int m_field;

    public BasicAccessor(int field) {
        m_field = field;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return (Integer) proto.get().get(m_field);
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        proto.get().set(m_field, val);
    }

}
