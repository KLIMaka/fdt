package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public class OffsetAccessor extends BasicAccessor {

    private int m_offset;

    public OffsetAccessor(String field, int off) {
        super(field);
        m_offset = off;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return super.get(proto) + m_offset;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        super.set(proto, val - m_offset);
    }

}
