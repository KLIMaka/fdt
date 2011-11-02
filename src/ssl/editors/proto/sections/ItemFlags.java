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

import ssl.editors.proto.ProtoAdaptorsFactory;
import ssl.editors.proto.Ref;
import ssl.editors.proto.accessor.MaskAccessor;
import ssl.editors.proto.accessor.MaskShiftOffsetValueAccessor;
import ssl.editors.proto.accessor.ProtoControlAdapter;
import fdk.proto.Prototype;

public class ItemFlags extends Composite {

    private final FormToolkit   m_toolkit = new FormToolkit(Display.getCurrent());
    private Button              m_flat;
    private Button              m_noblock;
    private Button              m_multihex;
    private Button              m_transnone;
    private Button              m_transwall;
    private Button              m_transglass;
    private Button              m_transsteam;
    private Button              m_transenergy;
    private Button              m_transred;
    private Button              m_shootthru;
    private Button              m_lightthru;
    private Button              m_lighting;
    private Button              m_nohightlight;
    private Button              m_hiddenitem;
    private Button              m_useon;
    private Button              m_look;
    private Button              m_talk;
    private Button              m_pickup;
    private Button              m_use;
    private Button              m_biggun;
    private Button              m_2hnd;
    private Button              m_pstand;
    private Button              m_pthrowpunch;
    private Button              m_pkickleg;
    private Button              m_pswing;
    private Button              m_pthrust;
    private Button              m_pthrow;
    private Button              m_psingle;
    private Button              m_pburst;
    private Button              m_pcontin;
    private Button              m_sstand;
    private Button              m_sthrowpunch;
    private Button              m_skickleg;
    private Button              m_sswing;
    private Button              m_sthrust;
    private Button              m_sthrow;
    private Button              m_ssingle;
    private Button              m_sburst;
    private Button              m_scontin;

    private ProtoControlAdapter m_protoAdapter;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     * @param msg
     * @param proto
     */
    public ItemFlags(Composite parent, ProtoAdaptorsFactory fact) {
        super(parent, SWT.NONE);
        m_protoAdapter = fact.create();
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                m_toolkit.dispose();
            }
        });
        m_toolkit.adapt(this);
        m_toolkit.paintBordersFor(this);
        {
            TableWrapLayout tableWrapLayout = new TableWrapLayout();
            tableWrapLayout.numColumns = 4;
            setLayout(tableWrapLayout);
        }

        Group grpFlags = new Group(this, SWT.NONE);
        grpFlags.setText("Base");
        grpFlags.setLayout(new GridLayout(1, false));
        grpFlags.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 3, 1));
        m_toolkit.adapt(grpFlags);
        m_toolkit.paintBordersFor(grpFlags);

        m_flat = new Button(grpFlags, SWT.CHECK);
        m_flat.setText("Flat");
        m_toolkit.adapt(m_flat, true, true);
        m_protoAdapter.adopt(m_flat, new MaskAccessor("flags", 0x00000008));

        m_noblock = new Button(grpFlags, SWT.CHECK);
        m_noblock.setText("No Block");
        m_toolkit.adapt(m_noblock, true, true);
        m_protoAdapter.adopt(m_noblock, new MaskAccessor("flags", 0x00000010));

        m_multihex = new Button(grpFlags, SWT.CHECK);
        m_multihex.setText("Multi Hex");
        m_toolkit.adapt(m_multihex, true, true);
        m_protoAdapter.adopt(m_multihex, new MaskAccessor("flags", 0x00000800));

        m_transnone = new Button(grpFlags, SWT.CHECK);
        m_transnone.setText("TransNone");
        m_toolkit.adapt(m_transnone, true, true);
        m_protoAdapter.adopt(m_transnone, new MaskAccessor("flags", 0x00008000));

        m_transwall = new Button(grpFlags, SWT.CHECK);
        m_transwall.setText("TransWall");
        m_toolkit.adapt(m_transwall, true, true);
        m_protoAdapter.adopt(m_transwall, new MaskAccessor("flags", 0x000010000));

        m_transglass = new Button(grpFlags, SWT.CHECK);
        m_transglass.setText("TransGlass");
        m_toolkit.adapt(m_transglass, true, true);
        m_protoAdapter.adopt(m_transglass, new MaskAccessor("flags", 0x00020000));

        m_transsteam = new Button(grpFlags, SWT.CHECK);
        m_transsteam.setText("TransSteam");
        m_toolkit.adapt(m_transsteam, true, true);
        m_protoAdapter.adopt(m_transsteam, new MaskAccessor("flags", 0x00040000));

        m_transenergy = new Button(grpFlags, SWT.CHECK);
        m_transenergy.setText("TransEnergy");
        m_toolkit.adapt(m_transenergy, true, true);
        m_protoAdapter.adopt(m_transenergy, new MaskAccessor("flags", 0x00080000));

        m_transred = new Button(grpFlags, SWT.CHECK);
        m_transred.setText("TransRed");
        m_toolkit.adapt(m_transred, true, true);
        m_protoAdapter.adopt(m_transred, new MaskAccessor("flags", 0x00004000));

        m_shootthru = new Button(grpFlags, SWT.CHECK);
        m_shootthru.setText("ShootThru");
        m_toolkit.adapt(m_shootthru, true, true);
        m_protoAdapter.adopt(m_shootthru, new MaskAccessor("flags", 0x80000000));

        m_lightthru = new Button(grpFlags, SWT.CHECK);
        m_lightthru.setText("LightThru");
        m_toolkit.adapt(m_lightthru, true, true);
        m_protoAdapter.adopt(m_lightthru, new MaskAccessor("flags", 0x20000000));

        m_lighting = new Button(grpFlags, SWT.CHECK);
        m_lighting.setText("Lighting");
        m_toolkit.adapt(m_lighting, true, true);
        m_protoAdapter.adopt(m_lighting, new MaskAccessor("flags", 0x00000020));

        m_nohightlight = new Button(grpFlags, SWT.CHECK);
        m_nohightlight.setText("NoHighlight");
        m_toolkit.adapt(m_nohightlight, true, true);
        m_protoAdapter.adopt(m_nohightlight, new MaskAccessor("flags", 0x00001000));

        Group grpItem = new Group(this, SWT.NONE);
        grpItem.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        grpItem.setText("Item");
        m_toolkit.adapt(grpItem);
        m_toolkit.paintBordersFor(grpItem);
        grpItem.setLayout(new GridLayout(1, false));

        m_hiddenitem = new Button(grpItem, SWT.CHECK);
        m_toolkit.adapt(m_hiddenitem, true, true);
        m_hiddenitem.setText("Hidden Item");
        m_protoAdapter.adopt(m_hiddenitem, new MaskAccessor("flagsExt", 0x08000000));

        Group grpPrimaryAttackType = new Group(this, SWT.NONE);
        grpPrimaryAttackType.setLayout(new GridLayout(1, false));
        grpPrimaryAttackType.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 3, 1));
        grpPrimaryAttackType.setText("Primary Attack Type");
        m_toolkit.adapt(grpPrimaryAttackType);
        m_toolkit.paintBordersFor(grpPrimaryAttackType);

        m_pstand = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pstand, true, true);
        m_pstand.setText("Stand");
        m_protoAdapter.adopt(m_pstand, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 0));

        m_pthrowpunch = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pthrowpunch, true, true);
        m_pthrowpunch.setText("Throw punch");
        m_protoAdapter.adopt(m_pthrowpunch, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 1));

        m_pkickleg = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pkickleg, true, true);
        m_pkickleg.setText("Kick leg");
        m_protoAdapter.adopt(m_pkickleg, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 2));

        m_pswing = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pswing, true, true);
        m_pswing.setText("Swing");
        m_protoAdapter.adopt(m_pswing, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 3));

        m_pthrust = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pthrust, true, true);
        m_pthrust.setText("Thrust");
        m_protoAdapter.adopt(m_pthrust, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 4));

        m_pthrow = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pthrow, true, true);
        m_pthrow.setText("Throw");
        m_protoAdapter.adopt(m_pthrow, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 5));

        m_psingle = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_psingle, true, true);
        m_psingle.setText("Fire single");
        m_protoAdapter.adopt(m_psingle, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 6));

        m_pburst = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pburst, true, true);
        m_pburst.setText("Fire burst");
        m_protoAdapter.adopt(m_pburst, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 7));

        m_pcontin = new Button(grpPrimaryAttackType, SWT.RADIO);
        m_toolkit.adapt(m_pcontin, true, true);
        m_pcontin.setText("Fire continuous");
        m_protoAdapter.adopt(m_pcontin, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 0, 8));

        Group grpSecondaryAttackType = new Group(this, SWT.NONE);
        grpSecondaryAttackType.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.TOP, 3, 1));
        grpSecondaryAttackType.setText("Secondary Attack Type");
        m_toolkit.adapt(grpSecondaryAttackType);
        m_toolkit.paintBordersFor(grpSecondaryAttackType);
        grpSecondaryAttackType.setLayout(new GridLayout(1, false));

        m_sstand = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sstand.setText("Stand");
        m_toolkit.adapt(m_sstand, true, true);
        m_protoAdapter.adopt(m_sstand, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 0));

        m_sthrowpunch = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sthrowpunch.setText("Throw punch");
        m_toolkit.adapt(m_sthrowpunch, true, true);
        m_protoAdapter.adopt(m_sthrowpunch, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 1));

        m_skickleg = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_skickleg.setText("Kick leg");
        m_toolkit.adapt(m_skickleg, true, true);
        m_protoAdapter.adopt(m_skickleg, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 2));

        m_sswing = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sswing.setText("Swing");
        m_toolkit.adapt(m_sswing, true, true);
        m_protoAdapter.adopt(m_sswing, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 3));

        m_sthrust = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sthrust.setText("Thrust");
        m_toolkit.adapt(m_sthrust, true, true);
        m_protoAdapter.adopt(m_sthrust, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 4));

        m_sthrow = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sthrow.setText("Throw");
        m_toolkit.adapt(m_sthrow, true, true);
        m_protoAdapter.adopt(m_sthrow, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 5));

        m_ssingle = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_ssingle.setText("Fire single");
        m_toolkit.adapt(m_ssingle, true, true);
        m_protoAdapter.adopt(m_ssingle, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 6));

        m_sburst = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_sburst.setText("Fire burst");
        m_toolkit.adapt(m_sburst, true, true);
        m_protoAdapter.adopt(m_sburst, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 7));

        m_scontin = new Button(grpSecondaryAttackType, SWT.RADIO);
        m_scontin.setText("Fire continuous");
        m_toolkit.adapt(m_scontin, true, true);
        m_protoAdapter.adopt(m_scontin, new MaskShiftOffsetValueAccessor("flagsExt", 0x0f, 4, 8));

        Group grpAction = new Group(this, SWT.NONE);
        grpAction.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        grpAction.setText("Action");
        m_toolkit.adapt(grpAction);
        m_toolkit.paintBordersFor(grpAction);
        grpAction.setLayout(new GridLayout(1, false));

        m_useon = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_useon, true, true);
        m_useon.setText("Use On");
        m_protoAdapter.adopt(m_useon, new MaskAccessor("flagsExt", 0x00001000));

        m_look = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_look, true, true);
        m_look.setText("Look");
        m_protoAdapter.adopt(m_look, new MaskAccessor("flagsExt", 0x00002000));

        m_talk = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_talk, true, true);
        m_protoAdapter.adopt(m_talk, new MaskAccessor("flagsExt", 0x00004000));
        m_talk.setText("Talk");

        m_pickup = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_pickup, true, true);
        m_pickup.setText("Pick Up");
        m_protoAdapter.adopt(m_pickup, new MaskAccessor("flagsExt", 0x00008000));

        m_use = new Button(grpAction, SWT.CHECK);
        m_toolkit.adapt(m_use, true, true);
        m_use.setText("Use");
        m_protoAdapter.adopt(m_use, new MaskAccessor("flagsExt", 0x00000800));

        Group grpWeapon = new Group(this, SWT.NONE);
        grpWeapon.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        grpWeapon.setText("Weapon");
        m_toolkit.adapt(grpWeapon);
        m_toolkit.paintBordersFor(grpWeapon);
        grpWeapon.setLayout(new GridLayout(1, false));

        m_biggun = new Button(grpWeapon, SWT.CHECK);
        m_toolkit.adapt(m_biggun, true, true);
        m_biggun.setText("BigGun");
        m_protoAdapter.adopt(m_biggun, new MaskAccessor("flagsExt", 0x00000100));

        m_2hnd = new Button(grpWeapon, SWT.CHECK);
        m_toolkit.adapt(m_2hnd, true, true);
        m_2hnd.setText("2Hnd");
        m_protoAdapter.adopt(m_2hnd, new MaskAccessor("flagsExt", 0x00000200));
    }

    public void fill(Ref<Prototype> proto, IProject proj) {
        m_protoAdapter.fill();
    }

}
