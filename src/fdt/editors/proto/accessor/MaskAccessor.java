package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class MaskAccessor extends BasicAccessor {

    protected int m_mask;

    public MaskAccessor(int field, int mask) {
        super(field);
        m_mask = mask;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return (((Integer) proto.get().get(m_field) & m_mask) == 0) ? 0 : 1;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        int pval = (Integer) proto.get().get(m_field);
        if (val == 0)
            proto.get().set(m_field, pval & ~m_mask);
        else proto.get().set(m_field, pval | m_mask);
    }
}
