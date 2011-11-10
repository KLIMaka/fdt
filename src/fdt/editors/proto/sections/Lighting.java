package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import fdk.proto.Prototype;
import fdt.editors.proto.IChangeListener;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.util.Ref;

public class Lighting extends Composite implements IFillSection {

    private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
    private Scale             m_sdistance;
    private Scale             m_sintens;
    private Label             m_dist;
    private Label             m_intens;

    private Ref<Prototype>    m_proto;
    private IChangeListener   m_cl;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     */
    public Lighting(Composite parent, ProtoAdaptorsFactory fact) {
        super(parent, SWT.NONE);
        m_proto = fact.getPro();
        m_cl = fact.getChangeListener();
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        setLayout(new GridLayout(1, false));

        Composite composite = m_toolkit.createComposite(this, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        composite.setBounds(0, 0, 64, 64);
        m_toolkit.paintBordersFor(composite);
        {
            TableWrapLayout twl_composite = new TableWrapLayout();
            twl_composite.numColumns = 3;
            composite.setLayout(twl_composite);
        }

        Label lblLightRadius = m_toolkit.createLabel(composite, "Light distance", SWT.NONE);
        lblLightRadius.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));

        m_sdistance = new Scale(composite, SWT.NONE);
        m_sdistance.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_dist.setText(m_sdistance.getSelection() + " hexes");
            }
        });
        m_sdistance.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_sdistance.setPageIncrement(1);
        m_sdistance.setMaximum(8);
        m_sdistance.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int val = m_sdistance.getSelection();
                if (m_proto.get().get(Prototype.LIGHT_DIST) != val) {
                    m_proto.get().set(Prototype.LIGHT_DIST, val);
                    m_cl.change();
                }
            }
        });
        m_toolkit.adapt(m_sdistance, true, true);

        m_dist = m_toolkit.createLabel(composite, "0 hexes", SWT.NONE);
        m_dist.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));

        Label lblLightIntensity = m_toolkit.createLabel(composite, "Light intensity", SWT.NONE);
        lblLightIntensity.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));

        m_sintens = new Scale(composite, SWT.NONE);
        m_sintens.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setIntensLabel(m_sintens.getSelection());
                int val = m_sintens.getSelection();
                if (m_proto.get().get(Prototype.LIGHT_INTENS) != val) {
                    m_proto.get().set(Prototype.LIGHT_INTENS, val);
                    m_cl.change();
                }
            }
        });
        m_sintens.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        m_sintens.setMaximum(65553);
        m_sintens.setPageIncrement(6555);
        m_toolkit.adapt(m_sintens, true, true);

        m_intens = m_toolkit.createLabel(composite, "000%", SWT.NONE);
        m_intens.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE, 1, 1));
    }

    public void setIntensLabel(int intens) {
        m_intens.setText((intens / 655) + "%");
    }

    public void fill(Ref<Prototype> proto, IProject proj) {
        Prototype pro = m_proto.get();
        m_dist.setText(pro.get(Prototype.LIGHT_DIST) + " hexes");
        m_sdistance.setSelection(pro.get(Prototype.LIGHT_DIST));
        m_sintens.setSelection(pro.get(Prototype.LIGHT_INTENS));
        setIntensLabel(pro.get(Prototype.LIGHT_INTENS));
    }

    @Override
    public Control toControl() {
        return this;
    }

    @Override
    public void setup() throws Exception {}
}
