package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public class MaskShiftAccessor extends BasicAccessor {

    protected int m_offset;
    protected int m_mask;

    public MaskShiftAccessor(String field, int mask, int offset) {
        super(field);
        m_offset = offset;
        m_mask = mask;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        int val = proto.get().getFields().get(m_field) >> m_offset;
        return val & m_mask;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        int pval = (Integer) proto.get().getFields().get(m_field);
        pval &= ~(m_mask << m_offset);
        val = (val & m_mask) << m_offset;
        proto.get().getFields().put(m_field, val | pval);
    }

}
