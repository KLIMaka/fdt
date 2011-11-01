package ssl.editors.frm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;

public class FGL {

    private static List<GLCanvas> contexts = new ArrayList<GLCanvas>();

    public static GLCanvas createGLCanvas(Composite parent, int style) {
        GLData gldata = new GLData();
        gldata.shareContext = contexts.size() == 0 ? null : contexts.get(0);
        GLCanvas canvas = new GLCanvas(parent, style, gldata);

        canvas.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                contexts.remove(e.widget);
            }
        });

        contexts.add(canvas);

        return canvas;
    }

}
