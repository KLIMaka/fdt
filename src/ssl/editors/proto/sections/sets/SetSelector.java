package ssl.editors.proto.sections.sets;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ssl.editors.proto.IChangeListener;
import ssl.editors.proto.Ref;
import ssl.editors.proto.sections.IFillSection;
import fdk.msg.MSG;
import fdk.proto.Prototype;

public class SetSelector {
    Map<String, IFillSection> m_set = new TreeMap<String, IFillSection>();

    public void generateSets(Composite parent, FormToolkit toolkit, IProject proj, Ref<Prototype> proto, Ref<MSG> msg,
            IChangeListener cl) throws Exception {
        add(toolkit, new DefaultSet(parent, SWT.NONE, proto, msg, cl), "default").setup(proj);
        add(toolkit, new CritterSet(parent, SWT.NONE, proto, msg, cl), "critter").setup(proj);
        add(toolkit, new GenericItemSet(parent, SWT.NONE, proto, msg, cl), "itmgen").setup(proj);
    }

    private IFillSection add(FormToolkit toolkit, IFillSection sel, String name) {
        toolkit.adapt((Composite) sel.toControl());
        toolkit.paintBordersFor((Composite) sel.toControl());
        m_set.put(name, sel);
        return sel;
    }

    public IFillSection getSet(Prototype proto) {
        if (proto.getFields().get("size") != null) return m_set.get("itmgen");
        if (proto.getFields().get("strength") != null) return m_set.get("critter");
        return m_set.get("default");
    }
}
