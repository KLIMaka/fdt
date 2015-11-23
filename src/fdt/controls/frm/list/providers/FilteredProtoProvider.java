package fdt.controls.frm.list.providers;

import static fdk.proto.Prototype.SUB_TYPE;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import fdk.lst.IEntry;
import fdk.proto.*;
import fdt.Fdt;

public class FilteredProtoProvider extends ProtoProvider {

	private int typeFilter;

	public FilteredProtoProvider(IProject proj, int type) {
		super(proj);
		typeFilter = type;
	}

	@Override
	public Object[] getElements() {
		Object[] elems = super.getElements();
		ArrayList<Object> filtered = new ArrayList<Object>();
		for (Object o : elems) {
			if (o == noneEnt) {
				filtered.add(o);
			} else {
				IEntry ent = (IEntry) o;
				try {
					Prototype pro = new Prototype(Fdt.getFile(proj, PRO.getProDir(type) + ent.getValue()).getContents());
					if (pro.get(SUB_TYPE) == typeFilter) {
						filtered.add(ent);
					}
				} catch (Exception e) {
					Fdt.getDefault().handleException(e);
				}
			}
		}
		return filtered.toArray();
	}
}
