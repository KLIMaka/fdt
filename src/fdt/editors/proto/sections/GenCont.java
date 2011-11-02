package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;

import fdk.proto.Prototype;
import fdt.editors.proto.accessor.IProtoAccessor;
import fdt.util.Ref;

public class GenCont extends Composite {

    private Ref<Prototype> m_ref;
    private IProject       m_project;
    private IProtoAccessor m_accesor;

    public GenCont(Composite parent, int style) {
        super(parent, style);
    }

    public void bind(Ref<Prototype> ref, IProject proj, IProtoAccessor accessor) {
        m_ref = ref;
        m_project = proj;
        m_accesor = accessor;
    }

    protected Ref<Prototype> getRef() {
        return m_ref;
    }

    protected IProject getProject() {
        return m_project;
    }

    protected IProtoAccessor getAccesor() {
        return m_accesor;
    }

}
