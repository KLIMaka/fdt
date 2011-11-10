package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class MaskShiftAccessor extends BasicAccessor {

    protected int m_offset;
    protected int m_mask;

    public MaskShiftAccessor(int field, int mask, int offset) {
        super(field);
        m_offset = offset;
        m_mask = mask;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        int val = proto.get().get(m_field) >> m_offset;
        return val & m_mask;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        int pval = (Integer) proto.get().get(m_field);
        pval &= ~(m_mask << m_offset);
        val = (val & m_mask) << m_offset;
        proto.get().set(m_field, val | pval);
    }

}
