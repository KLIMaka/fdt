package fdt.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Control;

import fdk.proto.Prototype;
import fdt.util.Ref;

public interface IFillSection {
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception;

    public void setup() throws Exception;

    public Control toControl();
}
