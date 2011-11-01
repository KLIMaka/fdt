package ssl.editors.proto.sections;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Control;

import ssl.editors.proto.Ref;
import fdk.proto.Prototype;

public interface IFillSection {
    public void fill(Ref<Prototype> proto, IProject proj) throws Exception;

    public void setup(IProject proj) throws Exception;

    public Control toControl();
}
