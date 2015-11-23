package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;

import fdk.msg.MSG;
import fdk.proto.Prototype;
import fdt.Fdt;
import fdt.editors.proto.accessor.*;
import fdt.editors.proto.adaptors.*;
import fdt.util.Ref;

public class Drugs extends Composite implements IFillSection {

	private final FormToolkit m_toolkit = new FormToolkit(Display.getCurrent());
	private Text m_a00;
	private Text m_a01;
	private Text m_a02;
	private Text m_a10;
	private Text m_a11;
	private Text m_a12;
	private Text m_d1;
	private Text m_a20;
	private Text m_a21;
	private Text m_a22;
	private Text m_d2;
	private Text m_rate;
	private Text m_time;
	private Combo m_stat0;
	private Combo m_stat1;
	private Combo m_stat2;
	private Combo m_effect;

	private ProtoControlAdapter m_protoAdapter;
	private IProject m_proj;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param msg
	 * @param proto
	 * @param changeListener
	 */
	public Drugs(Composite parent, ProtoAdaptorsFactory fact) {
		super(parent, SWT.NONE);
		m_protoAdapter = fact.create();
		m_proj = fact.getProject();
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				m_toolkit.dispose();
			}
		});
		m_toolkit.adapt(this);
		m_toolkit.paintBordersFor(this);
		setLayout(new GridLayout(4, false));

		Group grpStats = new Group(this, SWT.NONE);
		grpStats.setLayout(new GridLayout(2, false));
		grpStats.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpStats.setText("Stats");
		m_toolkit.adapt(grpStats);
		m_toolkit.paintBordersFor(grpStats);

		Label lblStat = m_toolkit.createLabel(grpStats, "Stat 0", SWT.NONE);
		lblStat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_stat0 = new Combo(grpStats, SWT.NONE);
		GridData gd_stat0 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_stat0.widthHint = 147;
		m_stat0.setLayoutData(gd_stat0);
		m_toolkit.adapt(m_stat0);
		m_toolkit.paintBordersFor(m_stat0);
		m_protoAdapter.adopt(m_stat0, new OffsetAccessor(Prototype.STAT_0, 2));

		Label lblStat_1 = m_toolkit.createLabel(grpStats, "Stat 1", SWT.NONE);
		lblStat_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_stat1 = new Combo(grpStats, SWT.NONE);
		m_stat1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(m_stat1);
		m_toolkit.paintBordersFor(m_stat1);
		m_protoAdapter.adopt(m_stat1, new OffsetAccessor(Prototype.STAT_1, 2));

		Label lblStat_2 = m_toolkit.createLabel(grpStats, "Stat 2", SWT.NONE);
		lblStat_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_stat2 = new Combo(grpStats, SWT.NONE);
		m_stat2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_toolkit.adapt(m_stat2);
		m_toolkit.paintBordersFor(m_stat2);
		m_protoAdapter.adopt(m_stat2, new OffsetAccessor(Prototype.STAT_2, 2));

		Group grpEffect = new Group(this, SWT.NONE);
		grpEffect.setLayout(new GridLayout(1, false));
		grpEffect.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEffect.setText("Effect 0");
		m_toolkit.adapt(grpEffect);
		m_toolkit.paintBordersFor(grpEffect);

		m_a00 = m_toolkit.createText(grpEffect, "New Text", SWT.NONE);
		m_a00.setText("");
		m_a00.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a00, new BasicAccessor(Prototype.AMOUNT00));

		m_a01 = m_toolkit.createText(grpEffect, "New Text", SWT.NONE);
		m_a01.setText("");
		m_a01.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a01, new BasicAccessor(Prototype.AMOUNT01));

		m_a02 = m_toolkit.createText(grpEffect, "New Text", SWT.NONE);
		m_a02.setText("");
		m_a02.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a02, new BasicAccessor(Prototype.AMOUNT02));

		Group grpEffect_1 = new Group(this, SWT.NONE);
		grpEffect_1.setLayout(new GridLayout(1, false));
		grpEffect_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEffect_1.setText("Effect 1");
		m_toolkit.adapt(grpEffect_1);
		m_toolkit.paintBordersFor(grpEffect_1);

		m_a10 = m_toolkit.createText(grpEffect_1, "New Text", SWT.NONE);
		m_a10.setText("");
		m_a10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a10, new BasicAccessor(Prototype.AMOUNT10));

		m_a11 = m_toolkit.createText(grpEffect_1, "New Text", SWT.NONE);
		m_a11.setText("");
		m_a11.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a11, new BasicAccessor(Prototype.AMOUNT11));

		m_a12 = m_toolkit.createText(grpEffect_1, "New Text", SWT.NONE);
		m_a12.setText("");
		m_a12.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a12, new BasicAccessor(Prototype.AMOUNT12));

		@SuppressWarnings("unused")
		Label lblDealy = m_toolkit.createLabel(grpEffect_1, "Dealy", SWT.NONE);

		m_d1 = m_toolkit.createText(grpEffect_1, "New Text", SWT.NONE);
		m_d1.setText("");
		m_d1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adoptM2H(m_d1, new BasicAccessor(Prototype.DURATION1));

		Group grpEffect_2 = new Group(this, SWT.NONE);
		grpEffect_2.setLayout(new GridLayout(1, false));
		grpEffect_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEffect_2.setText("Effect 2");
		m_toolkit.adapt(grpEffect_2);
		m_toolkit.paintBordersFor(grpEffect_2);

		m_a20 = m_toolkit.createText(grpEffect_2, "New Text", SWT.NONE);
		m_a20.setText("");
		m_a20.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a20, new BasicAccessor(Prototype.AMOUNT20));

		m_a21 = m_toolkit.createText(grpEffect_2, "New Text", SWT.NONE);
		m_a21.setText("");
		m_a21.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a21, new BasicAccessor(Prototype.AMOUNT21));

		m_a22 = m_toolkit.createText(grpEffect_2, "New Text", SWT.NONE);
		m_a22.setText("");
		m_a22.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_a22, new BasicAccessor(Prototype.AMOUNT22));

		@SuppressWarnings("unused")
		Label lblDealy_1 = m_toolkit.createLabel(grpEffect_2, "Dealy", SWT.NONE);

		m_d2 = m_toolkit.createText(grpEffect_2, "New Text", SWT.NONE);
		m_d2.setText("");
		m_d2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adoptM2H(m_d2, new BasicAccessor(Prototype.DURATION2));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		m_toolkit.adapt(composite);
		m_toolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));

		@SuppressWarnings("unused")
		Label lblAddictionEffect = m_toolkit.createLabel(composite, "Addiction effect", SWT.NONE);

		m_effect = new Combo(composite, SWT.NONE);
		m_effect.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		m_toolkit.adapt(m_effect);
		m_toolkit.paintBordersFor(m_effect);
		m_protoAdapter.adopt(m_effect, new OffsetAccessor(Prototype.W_EFFECT, 1));

		Composite composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		m_toolkit.adapt(composite_2);
		m_toolkit.paintBordersFor(composite_2);

		Label lblAddictionRate = m_toolkit.createLabel(composite_2, "Addiction rate", SWT.NONE);
		lblAddictionRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_rate = m_toolkit.createText(composite_2, "New Text", SWT.NONE);
		m_rate.setText("");
		m_rate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adopt(m_rate, new BasicAccessor(Prototype.ADDICT_RATE));

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		m_toolkit.adapt(composite_1);
		m_toolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new GridLayout(2, false));

		Label lblAddictionTime = m_toolkit.createLabel(composite_1, "Addiction time", SWT.NONE);
		lblAddictionTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		m_time = m_toolkit.createText(composite_1, "New Text", SWT.NONE);
		m_time.setText("");
		m_time.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_protoAdapter.adoptM2H(m_time, new BasicAccessor(Prototype.W_ONSET));

	}

	@Override
	public void fill(Ref<Prototype> proto, IProject proj) throws Exception {
		m_protoAdapter.fill();
	}

	@Override
	public void setup() throws Exception {
		MSG perk_msg = Fdt.getCachedMsg(m_proj, "text/english/game/perk.msg");

		m_effect.add("None");
		for (int i = 101; i <= 219; i++)
			m_effect.add(perk_msg.get(i).getMsg());

		MSG stat_msg = Fdt.getCachedMsg(m_proj, "text/english/game/stat.msg");

		String[] hlp = { "Random", "None" };
		for (String str : hlp) {
			m_stat0.add(str);
			m_stat1.add(str);
			m_stat2.add(str);
		}

		for (int i = 100; i <= 137; i++) {
			String str = stat_msg.get(i).getMsg();
			m_stat0.add(str);
			m_stat1.add(str);
			m_stat2.add(str);
		}
	}

	@Override
	public Control toControl() {
		return this;
	}

}
