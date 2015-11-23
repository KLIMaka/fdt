package fdt.controls.frm.list.providers;

import java.nio.charset.Charset;
import java.util.*;

import org.eclipse.core.resources.IProject;

import fdk.frm.FRMFrame;
import fdk.lst.*;
import fdk.msg.MSG;
import fdk.proto.*;
import fdt.Fdt;
import fdt.controls.frm.list.FRMList;
import fdt.util.FID;

public class ProtoProvider implements IFrmListContentProvider, IFrmListLabelProvider {

	private static class ProtoEntry {
		public String name;
		public FRMFrame frame;
	}

	private static class NoneEntry {
	};

	protected static NoneEntry noneEnt = new NoneEntry();

	protected IProject proj;
	protected int type;
	private Map<Object, ProtoEntry> cache = new HashMap<Object, ProtoProvider.ProtoEntry>();

	public ProtoProvider(IProject proj) {
		this.proj = proj;
	}

	private ProtoEntry get(Object o) {
		ProtoEntry ent = cache.get(o);
		if (ent == null) {
			try {
				ent = new ProtoEntry();

				if (o == noneEnt) {
					ent.name = "None";
					ent.frame = Fdt.getFRM(proj, "art/items/reserved.frm");
				} else {

					IEntry lstent = (IEntry) o;
					Prototype pro = new Prototype(Fdt.getFile(proj, PRO.getProDir(type) + lstent.getValue()).getContents());
					int id = (pro.get(Prototype.PROTO_ID) & 0x0000ffff) * 100;

					int fid = pro.get(type == PRO.ITEM && pro.get(Prototype.SUB_TYPE) != 1 ? Prototype.INV_FID : Prototype.FID);
					MSG msg = Fdt.getCachedMsg(proj, PRO.getMsg(type));
					if (msg.get(id) != null) {
						ent.name = msg.get(id).getMsg();
					} else {
						ent.name = "";
					}
					ent.frame = Fdt.getFRM(proj, FID.getFileNameByFID(proj, fid));
				}
				cache.put(o, ent);

			} catch (Exception e) {
				ent.name = "ERROR";
				ent.frame = Fdt.getFRM(proj, "art/items/reserved.frm");
			}
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
			Charset cs = Charset.forName(proj.getDefaultCharset());
			LST lst = new LST(Fdt.getFile(proj, PRO.getLst(type)).getContents(), cs, new BasicEntryMaker());
			Object[] ret = new Object[lst.size() + 1];
			ret[0] = noneEnt;
			System.arraycopy(lst.toArray(), 0, ret, 1, lst.size());
			return ret;
		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
			return new Object[] {};
		}
	}

}
