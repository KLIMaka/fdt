package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.*;

import fdk.proto.Prototype;
import fdt.editors.proto.accessor.MaskAccessor;
import fdt.editors.proto.adaptors.*;
import fdt.util.Ref;

public class CritterFlags extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Button m_flat;
	private Button m_noblock;
	private Button m_multihex;
	private Button m_transnone;
	private Button m_transwall;
	private Button m_transglass;
	private Button m_transsteam;
	private Button m_transenergy;
	private Button m_transred;
	private Button m_shootthru;
	private Button m_lightthru;
	private Button m_lighting;
	private Button m_useon;
	private Button m_look;
	private Button m_talk;
	private Button m_pickup;
	private Button m_use;
	private Group m_group;
	private Button m_steal;
	private Button m_drop;
	private Button m_limbs;
	private Button m_ages;
	private Button m_heal;
	private Button m_inv;
	private Button m_flatten;
	private Button m_special;
	private Button m_range;
	private Button m_knock;

	private ProtoControlAdapter m_protoAdapter;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CritterFlags(Composite parent, ProtoAdaptorsFactory fact) {
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
			tableWrapLayout.numColumns = 3;
			setLayout(tableWrapLayout);
		}

		Group grpFlags = new Group(this, SWT.NONE);
		grpFlags.setText("Base");
		grpFlags.setLayout(new GridLayout(1, false));
		grpFlags.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
		m_toolkit.adapt(grpFlags);
		m_toolkit.paintBordersFor(grpFlags);

		m_flat = new Button(grpFlags, SWT.CHECK);
		m_flat.setText("Flat");
		m_toolkit.adapt(m_flat, true, true);
		m_protoAdapter.adopt(m_flat, new MaskAccessor(Prototype.FLAGS, 0x00000008));

		m_noblock = new Button(grpFlags, SWT.CHECK);
		m_noblock.setText("No Block");
		m_toolkit.adapt(m_noblock, true, true);
		m_protoAdapter.adopt(m_noblock, new MaskAccessor(Prototype.FLAGS, 0x00000010));

		m_multihex = new Button(grpFlags, SWT.CHECK);
		m_multihex.setText("Multi Hex");
		m_toolkit.adapt(m_multihex, true, true);
		m_protoAdapter.adopt(m_multihex, new MaskAccessor(Prototype.FLAGS, 0x00000800));

		m_transnone = new Button(grpFlags, SWT.CHECK);
		m_transnone.setText("TransNone");
		m_toolkit.adapt(m_transnone, true, true);
		m_protoAdapter.adopt(m_transnone, new MaskAccessor(Prototype.FLAGS, 0x00008000));

		m_transwall = new Button(grpFlags, SWT.CHECK);
		m_transwall.setText("TransWall");
		m_toolkit.adapt(m_transwall, true, true);
		m_protoAdapter.adopt(m_transwall, new MaskAccessor(Prototype.FLAGS, 0x00010000));

		m_transglass = new Button(grpFlags, SWT.CHECK);
		m_transglass.setText("TransGlass");
		m_toolkit.adapt(m_transglass, true, true);
		m_protoAdapter.adopt(m_transglass, new MaskAccessor(Prototype.FLAGS, 0x00020000));

		m_transsteam = new Button(grpFlags, SWT.CHECK);
		m_transsteam.setText("TransSteam");
		m_toolkit.adapt(m_transsteam, true, true);
		m_protoAdapter.adopt(m_transsteam, new MaskAccessor(Prototype.FLAGS, 0x00040000));

		m_transenergy = new Button(grpFlags, SWT.CHECK);
		m_transenergy.setText("TransEnergy");
		m_toolkit.adapt(m_transenergy, true, true);
		m_protoAdapter.adopt(m_transenergy, new MaskAccessor(Prototype.FLAGS, 0x00080000));

		m_transred = new Button(grpFlags, SWT.CHECK);
		m_transred.setText("TransRed");
		m_toolkit.adapt(m_transred, true, true);
		m_protoAdapter.adopt(m_transred, new MaskAccessor(Prototype.FLAGS, 0x00004000));

		m_shootthru = new Button(grpFlags, SWT.CHECK);
		m_shootthru.setText("ShootThru");
		m_toolkit.adapt(m_shootthru, true, true);
		m_protoAdapter.adopt(m_shootthru, new MaskAccessor(Prototype.FLAGS, 0x80000000));

		m_lightthru = new Button(grpFlags, SWT.CHECK);
		m_lightthru.setText("LightThru");
		m_toolkit.adapt(m_lightthru, true, true);
		m_protoAdapter.adopt(m_lightthru, new MaskAccessor(Prototype.FLAGS, 0x20000000));

		m_lighting = new Button(grpFlags, SWT.CHECK);
		m_lighting.setText("Lighting");
		m_toolkit.adapt(m_lighting, true, true);
		m_protoAdapter.adopt(m_lighting, new MaskAccessor(Prototype.FLAGS, 0x00000020));

		Group grpAction = new Group(this, SWT.NONE);
		grpAction.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
		grpAction.setText("Action");
		m_toolkit.adapt(grpAction);
		m_toolkit.paintBordersFor(grpAction);
		grpAction.setLayout(new GridLayout(1, false));

		m_useon = new Button(grpAction, SWT.CHECK);
		m_toolkit.adapt(m_useon, true, true);
		m_useon.setText("Use On");
		m_protoAdapter.adopt(m_useon, new MaskAccessor(Prototype.FLAGS_EXT, 0x00001000));

		m_look = new Button(grpAction, SWT.CHECK);
		m_toolkit.adapt(m_look, true, true);
		m_look.setText("Look");
		m_protoAdapter.adopt(m_look, new MaskAccessor(Prototype.FLAGS_EXT, 0x00002000));

		m_talk = new Button(grpAction, SWT.CHECK);
		m_toolkit.adapt(m_talk, true, true);
		m_talk.setText("Talk");
		m_protoAdapter.adopt(m_talk, new MaskAccessor(Prototype.FLAGS_EXT, 0x00004000));

		m_pickup = new Button(grpAction, SWT.CHECK);
		m_toolkit.adapt(m_pickup, true, true);
		m_pickup.setText("Pick Up");
		m_protoAdapter.adopt(m_pickup, new MaskAccessor(Prototype.FLAGS_EXT, 0x00008000));

		m_use = new Button(grpAction, SWT.CHECK);
		m_toolkit.adapt(m_use, true, true);
		m_use.setText("Use");
		m_protoAdapter.adopt(m_use, new MaskAccessor(Prototype.FLAGS_EXT, 0x00000800));

		m_group = new Group(this, SWT.NONE);
		m_group.setLayout(new GridLayout(1, false));
		m_group.setLayoutData(new TableWrapData(TableWrapData.RIGHT, TableWrapData.TOP, 1, 1));
		m_group.setText("Critter");
		m_toolkit.adapt(m_group);
		m_toolkit.paintBordersFor(m_group);

		m_steal = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_steal, true, true);
		m_steal.setText("Steal");
		m_protoAdapter.adopt(m_steal, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000020));

		m_drop = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_drop, true, true);
		m_drop.setText("Drop");
		m_protoAdapter.adopt(m_drop, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000040));

		m_limbs = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_limbs, true, true);
		m_limbs.setText("Limbs");
		m_protoAdapter.adopt(m_limbs, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000080));

		m_ages = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_ages, true, true);
		m_ages.setText("Ages");
		m_protoAdapter.adopt(m_ages, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000100));

		m_heal = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_heal, true, true);
		m_heal.setText("Heal");
		m_protoAdapter.adopt(m_heal, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000200));

		m_inv = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_inv, true, true);
		m_inv.setText("Invulnerable");
		m_protoAdapter.adopt(m_inv, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000400));

		m_flatten = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_flatten, true, true);
		m_flatten.setText("Flatten");
		m_protoAdapter.adopt(m_flatten, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00000800));

		m_special = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_special, true, true);
		m_special.setText("Special");
		m_protoAdapter.adopt(m_special, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00001000));

		m_range = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_range, true, true);
		m_range.setText("Range");
		m_protoAdapter.adopt(m_range, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00002000));

		m_knock = new Button(m_group, SWT.CHECK);
		m_toolkit.adapt(m_knock, true, true);
		m_knock.setText("Knock");
		m_protoAdapter.adopt(m_knock, new MaskAccessor(Prototype.CRITTER_FLAGS, 0x00004000));
	}

	public void fill(Prototype proto, IProject proj) {

	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		m_protoAdapter.fill();
	}

	@Override
	public void setup() throws Exception {
	}

	@Override
	public Control toControl() {
		return this;
	}

}
