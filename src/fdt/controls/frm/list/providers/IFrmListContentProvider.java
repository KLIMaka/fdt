package fdt.controls.frm.list.providers;

import fdt.controls.frm.list.FRMList;

public interface IFrmListContentProvider {

	public void inputChanged(FRMList list, Object newInput, Object oldInput);

	public Object[] getElements();

}
