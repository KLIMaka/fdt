package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class OffsetAccessor extends BasicAccessor {

	private int m_offset;

	public OffsetAccessor(int field, int off) {
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
