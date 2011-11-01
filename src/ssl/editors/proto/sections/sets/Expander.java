package ssl.editors.proto.sections.sets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;

public class Expander implements IExpansionListener {

    private Composite         m_comp;
    private ScrolledComposite m_scroll;

    public Expander(Composite composite, ScrolledComposite scroll) {
        m_comp = composite;
        m_scroll = scroll;
    }

    @Override
    public void expansionStateChanging(ExpansionEvent e) {

    }

    @Override
    public void expansionStateChanged(ExpansionEvent e) {
        m_comp.layout();
        m_scroll.setMinSize(m_comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        m_scroll.layout();
    }

}
