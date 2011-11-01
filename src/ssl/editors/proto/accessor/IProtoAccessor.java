package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public interface IProtoAccessor {
    public int get(Ref<Prototype> proto);

    public void set(Ref<Prototype> proto, int val);
}
