package fdt.util;

import java.util.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.opengl.*;
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
