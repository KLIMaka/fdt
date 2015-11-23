package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public class ZeroAddAccessor extends BasicAccessor {

	protected int m_add;

	public ZeroAddAccessor(int field, int add) {
		super(field);
		m_add = add;
	}

	@Override
	public int get(Ref<Prototype> proto) {
		int val = super.get(proto);
		return val == -1 ? 0 : val - m_add;
	}

	@Override
	public void set(Ref<Prototype> proto, int val) {
		val = val == 0 ? -1 : val + m_add;
		super.set(proto, val);
	}
}
