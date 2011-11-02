package fdt.editors.proto.accessor;

import fdk.proto.Prototype;
import fdt.util.Ref;

public interface IProtoAccessor {
    public int get(Ref<Prototype> proto);

    public void set(Ref<Prototype> proto, int val);
}
