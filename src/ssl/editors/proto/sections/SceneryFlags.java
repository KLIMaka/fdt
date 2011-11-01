package ssl.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import fdk.proto.Prototype;

public class SceneryFlags extends Composite {

    private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
    private Button            m_flat;
    private Button            m_noblock;
    private Button            m_multihex;
    private Button            m_transnone;
    private Button            m_transwall;
    private Button            m_transglass;
    private Button            m_transsteam;
    private Button            m_transenergy;
    private Button            m_transred;
    private Button            m_shootthru;
    private Button            m_lightthru;
    private Button            m_lighting;
    private Button            m_walltransend;
    private Button            m_useon;
    private Button            m_look;
    private Button            m_talk;
    private Button            m_pickup;
    private Button            m_use;
    private Group             m_group;
    private Button            m_ns;
    private Button            m_w;
    private Button            m_e;
    private Button            m_s;
    private Button            m_n;
    private Button            m_ew;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public SceneryFlags(Composite parent, int style) {
        super(parent, style);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        {
            TableWrapLayout tableWrapLayout = new TableWrapLayout();
            tableWrapLayout.numColumns = 2;
            setLayout(tableWrapLayout);
        }

        Group grpFlags = new Group(this, SWT.NONE);
        grpFlags.setText("Base");
        grpFlags.setLayout(new GridLayout(1, false));
        grpFlags.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 2, 1));
        m_toolkit.adapt(grpFlags);
        m_toolkit.paintBordersFor(grpFlags);

        m_flat = new Button(grpFlags, SWT.CHECK);
        m_flat.setText("Flat");
        m_toolkit.adapt(m_flat, true, true);

        m_noblock = new Button(grpFlags, SWT.CHECK);
        m_noblock.setText("No Block");
        m_toolkit.adapt(m_noblock, true, true);

        m_multihex = new Button(grpFlags, SWT.CHECK);
        m_multihex.setText("Multi Hex");
        m_toolkit.adapt(m_multihex, true, true);

        m_transnone = new Button(grpFlags, SWT.CHECK);
        m_transnone.setText("TransNone");
        m_toolkit.adapt(m_transnone, true, true);

        m_transwall = new Button(grpFlags, SWT.CHECK);
        m_transwall.setText("TransWall");
        m_toolkit.adapt(m_transwall, true, true);

        m_transglass = new Button(grpFlags, SWT.CHECK);
        m_transglass.setText("TransGlass");
        m_toolkit.adapt(m_transglass, true, true);

        m_transsteam = new Button(grpFlags, SWT.CHECK);
        m_transsteam.setText("TransSteam");
        m_toolkit.adapt(m_transsteam, true, true);

        m_transenergy = new Button(grpFlags, SWT.CHECK);
        m_transenergy.setText("TransEnergy");
        m_toolkit.adapt(m_transenergy, true, true);

        m_transred = new Button(grpFlags, SWT.CHECK);
        m_transred.setText("TransRed");
        m_toolkit.adapt(m_transred, true, true);

        m_shootthru = new Button(grpFlags, SWT.CHECK);
        m_shootthru.setText("ShootThru");
        m_toolkit.adapt(m_shootthru, true, true);

        m_lightthru = new Button(grpFlags, SWT.CHECK);
        m_lightthru.setText("LightThru");
        m_toolkit.adapt(m_lightthru, true, true);

        m_lighting = new Button(grpFlags, SWT.CHECK);
        m_lighting.setText("Lighting");
        m_toolkit.adapt(m_lighting, true, true);

        m_walltransend = new Button(grpFlags, SWT.CHECK);
        m_walltransend.setText("WallTransEnd");
        m_toolkit.adapt(m_walltransend, true, true);

        Group grpAction = new Group(this, SWT.NONE);
        grpAction.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        grpAction.setText("Action");
        m_toolkit.adapt(grpAction);
        m_toolkit.paintBordersFor(grpAction);
        grpAction.setLayout(new GridLayout(1, false));

        m_useon = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_useon, true, true);
        m_useon.setText("Use On");

        m_look = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_look, true, true);
        m_look.setText("Look");

        m_talk = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_talk, true, true);
        m_talk.setText("Talk");

        m_pickup = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_pickup, true, true);
        m_pickup.setText("Pick Up");

        m_use = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_use, true, true);
        m_use.setText("Use");

        m_group = new Group(this, SWT.NONE);
        m_group.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        m_group.setText("Wall Light Type");
        m_toolkit.adapt(m_group);
        m_toolkit.paintBordersFor(m_group);
        m_group.setLayout(new GridLayout(1, false));

        m_ns = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_ns, true, true);
        m_ns.setText("North/South");

        m_w = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_w, true, true);
        m_w.setText("West Corner");

        m_e = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_e, true, true);
        m_e.setText("East Corner");

        m_s = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_s, true, true);
        m_s.setText("South Corner");

        m_n = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_n, true, true);
        m_n.setText("North Corner");

        m_ew = new Button(m_group, SWT.CHECK);
        m_toolkit.adapt(m_ew, true, true);
        m_ew.setText("East/West");
    }

    public void fill(Prototype proto, IProject proj) {
        int flags = (Integer) proto.getFields().get("flags");
        int extflags = (Integer) proto.getFields().get("flagsExt");

        m_flat.setSelection((flags & 0x00000008) != 0);
        m_noblock.setSelection((flags & 0x00000010) != 0);
        m_multihex.setSelection((flags & 0x00000800) != 0);
        m_transnone.setSelection((flags & 0x00008000) != 0);
        m_transwall.setSelection((flags & 0x00010000) != 0);
        m_transglass.setSelection((flags & 0x00020000) != 0);
        m_transsteam.setSelection((flags & 0x00040000) != 0);
        m_transenergy.setSelection((flags & 0x00080000) != 0);
        m_transred.setSelection((flags & 0x00004000) != 0);
        m_shootthru.setSelection((flags & 0x80000000) != 0);
        m_lightthru.setSelection((flags & 0x20000000) != 0);
        m_lighting.setSelection((flags & 0x00000020) != 0);
        m_walltransend.setSelection((flags & 0x10000000) != 0);

        m_useon.setSelection((extflags & 0x00001000) != 0);
        m_look.setSelection((extflags & 0x00002000) != 0);
        m_talk.setSelection((extflags & 0x00004000) != 0);
        m_pickup.setSelection((extflags & 0x00008000) != 0);
        m_use.setSelection((extflags & 0x00000800) != 0);

        m_ns.setSelection((extflags & 0x00000000) != 0);
        m_w.setSelection((extflags & 0x80000000) != 0);
        m_e.setSelection((extflags & 0x40000000) != 0);
        m_s.setSelection((extflags & 0x20000000) != 0);
        m_n.setSelection((extflags & 0x10000000) != 0);
        m_ew.setSelection((extflags & 0x08000000) != 0);

    }

}
