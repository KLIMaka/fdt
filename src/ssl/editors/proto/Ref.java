package ssl.editors.proto;

public class Ref<T> {

    private T m_ref;

    public T get() {
        return m_ref;
    }

    public void set(T nref) {
        m_ref = nref;
    }
}
