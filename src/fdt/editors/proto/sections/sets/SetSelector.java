package fdt.editors.proto.sections.sets;

import java.util.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import fdk.proto.*;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.editors.proto.sections.IFillSection;

public class SetSelector {
	Map<String, IFillSection> m_set = new TreeMap<String, IFillSection>();

	public void generateSets(Composite parent, FormToolkit toolkit, ProtoAdaptorsFactory fact) throws Exception {
		add(toolkit, new DefaultSet(parent, fact), "default").setup();
		add(toolkit, new CritterSet(parent, fact), "critter").setup();
		add(toolkit, new GenericItemSet(parent, fact), "itmgen").setup();
	}

	private IFillSection add(FormToolkit toolkit, IFillSection sel, String name) {
		toolkit.adapt((Composite) sel.toControl());
		toolkit.paintBordersFor((Composite) sel.toControl());
		m_set.put(name, sel);
		return sel;
	}

	public IFillSection getSet(Prototype proto) {
		if (proto.getType() == PRO.ITEM)
			return m_set.get("itmgen");
		if (proto.getType() == PRO.CRITTER)
			return m_set.get("critter");
		return m_set.get("default");
	}
}
