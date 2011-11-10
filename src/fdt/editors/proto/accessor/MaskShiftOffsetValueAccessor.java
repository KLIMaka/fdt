package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class MaskShiftOffsetValueAccessor extends MaskShiftAccessor {

    protected int m_value;

    public MaskShiftOffsetValueAccessor(int field, int mask, int offset, int val) {
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
