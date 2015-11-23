package fdt.controls.frm.list.providers;

import java.util.*;

import org.eclipse.core.resources.IProject;

import fdk.frm.FRMFrame;
import fdk.lst.*;
import fdt.Fdt;
import fdt.controls.frm.list.FRMList;
import fdt.util.FID;

public class FrmProvider implements IFrmListContentProvider, IFrmListLabelProvider {

	private static class CacheEntry {
		public String name;
		public FRMFrame frame;
	}

	private IProject proj;
	private int type;
	private Map<Object, CacheEntry> cache = new HashMap<Object, CacheEntry>();

	public FrmProvider(IProject proj) {
		this.proj = proj;
	}

	private CacheEntry get(Object o) {
		CacheEntry ent = cache.get(o);
		if (ent == null) {
			ent = new CacheEntry();
			cache.put(o, ent);

			IEntry e = (IEntry) o;
			ent.name = e.getValue();
			int fid = (e.getIndex() - 1) | (type << 24);
			ent.frame = Fdt.getFRM(proj, FID.getFileNameByFID(proj, fid));
		}
		return ent;
	}

	@Override
	public String getText(Object o) {
		return get(o).name;
	}

	@Override
	public FRMFrame getImage(Object o) {
		return get(o).frame;
	}

	@Override
	public void inputChanged(FRMList list, Object newInput, Object oldInput) {
		type = (Integer) newInput;
	}

	@Override
	public Object[] getElements() {
		try {
			LST lst = Fdt.getCachedLST(proj, "art/" + FID.types[type] + "/" + FID.types[type] + ".lst");
			return lst.toArray();
		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
			return null;
		}
	}

}
