package ssl.editors.frm;

public interface IFrmListContentProvider {

    public void inputChanged(FRMList list, Object newInput, Object oldInput);

    public Object[] getElements();

}
