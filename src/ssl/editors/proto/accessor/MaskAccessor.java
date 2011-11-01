package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public class MaskAccessor extends BasicAccessor {

    protected int m_mask;

    public MaskAccessor(String field, int mask) {
        super(field);
        m_mask = mask;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return (((Integer) proto.get().getFields().get(m_field) & m_mask) == 0) ? 0 : 1;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        int pval = (Integer) proto.get().getFields().get(m_field);
        if (val == 0)
            proto.get().getFields().put(m_field, pval & ~m_mask);
        else proto.get().getFields().put(m_field, pval | m_mask);
    }
}
