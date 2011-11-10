package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class MaskOffsetAccessor extends MaskAccessor {

    protected int m_offset;
    protected int m_mult;

    public MaskOffsetAccessor(int field, int mask, int mul, int off) {
        super(field, mask);
        m_offset = off;
        m_mult = mul;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return ((Integer) proto.get().get(m_field) & m_mask) * m_mult + m_offset;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        proto.get().set(m_field, (val / m_mult - m_offset) & m_mask);
    }
}
