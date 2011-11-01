package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public class MaskShiftOffsetValueAccessor extends MaskShiftAccessor {

    protected int m_value;

    public MaskShiftOffsetValueAccessor(String field, int mask, int offset, int val) {
        super(field, mask, offset);
        m_value = val;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return super.get(proto) == m_value ? 1 : 0;
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        if (val == 1) super.set(proto, m_value);
    }

}
