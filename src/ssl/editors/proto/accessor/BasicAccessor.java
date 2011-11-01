package ssl.editors.proto.accessor;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public class BasicAccessor implements IProtoAccessor {

    protected String m_field;

    public BasicAccessor(String field) {
        m_field = field;
    }

    @Override
    public int get(Ref<Prototype> proto) {
        return (Integer) proto.get().getFields().get(m_field);
    }

    @Override
    public void set(Ref<Prototype> proto, int val) {
        proto.get().getFields().put(m_field, val);
    }

}
