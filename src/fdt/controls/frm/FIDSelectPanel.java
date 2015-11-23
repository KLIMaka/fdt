package fdt.controls.frm;

import java.io.InputStream;

import org.eclipse.jface.action.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import com.swtdesigner.SWTResourceManager;

import fdk.frm.*;
import fdk.lst.IEntry;
import fdt.Fdt;
import fdt.controls.frm.list.selectors.FidSelector;
import fdt.editors.proto.accessor.BasicAccessor;
import fdt.editors.proto.adaptors.ProtoAdaptorsFactory;
import fdt.util.FID;

class MyMouse implements MouseListener, MouseMoveListener {
	private int xOff = 0;
	private int yOff = 0;

	private boolean down = false;
	private int xDown;
	private int yDown;

	private boolean needRedraw;
	private int oldXoff;
	private int oldYoff;

	@Override
	public void mouseMove(MouseEvent e) {
		if (down) {
			int dx = e.x - xDown;
			int dy = e.y - yDown;

			xOff = oldXoff + dx;
			yOff = oldYoff + dy;
			needRedraw = true;
		}
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			down = true;
			xDown = e.x;
			yDown = e.y;
			oldXoff = xOff;
			oldYoff = yOff;
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (leftButton(e)) {
			down = false;
		}
	}

	private boolean leftButton(MouseEvent e) {
		return (e.stateMask & SWT.BUTTON1) == SWT.BUTTON1;
	}

	public int getX_off() {
		return xOff;
	}

	public int getY_off() {
		return yOff;
	}

	public boolean needRedraw() {
		boolean tmp = needRedraw;
		needRedraw = false;
		return tmp;
	}

	public void center() {
		xOff = yOff = 0;
		needRedraw = true;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}
}

public class FIDSelectPanel extends Composite {

	private Image images[];
	private Control canvas;
	private boolean play;
	private int frame;
	private int side = 3;

	private ToolBarManager mgr;

	private FRMImage frm;
	private int dx, dy;

	private Action playAction;
	private Action left;
	private Action right;
	private Action change;
	private Action center;

	private PaletteData pal;
	private MyMouse mouse = new MyMouse();
	private int fidType;
	private ProtoAdaptorsFactory factory;
	private BasicAccessor accessor;

	public FIDSelectPanel(Composite parent, ProtoAdaptorsFactory fact, int field, int fType) {

		super(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		fidType = fType;
		factory = fact;
		accessor = new BasicAccessor(field);

		createActions();

		ToolBar toolBar_1 = new ToolBar(this, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		toolBar_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

		mgr = new ToolBarManager(toolBar_1);
		mgr.update(true);

		canvas = new Canvas(this, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(65, 110, 155));
		canvas.addMouseListener(mouse);
		canvas.addMouseMoveListener(mouse);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		canvas.setCursor(new Cursor(getDisplay(), SWT.CURSOR_SIZEALL));

		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				final Image backBuff = new Image(getDisplay(), canvas.getBounds());
				final GC backGC = new GC(backBuff);

				backGC.setBackground(canvas.getBackground());
				backGC.fillRectangle(0, 0, canvas.getBounds().width, canvas.getBounds().height);

				if (images != null) {
					if (images.length == 1) {
						Point center = canvas.getSize();
						Image img = images[frame];
						int x = (center.x - img.getBounds().width) / 2 + mouse.getX_off();
						int y = (center.y - img.getBounds().height) / 2 + mouse.getY_off();
						backGC.drawImage(img, x, y);

					} else {
						Point center = canvas.getSize();
						center.y += images[0].getBounds().height;
						Image img = images[frame];
						center.x = center.x / 2 - img.getBounds().width / 2 + dx + mouse.getX_off();
						center.y = center.y / 2 - img.getBounds().height + dy + mouse.getY_off();
						backGC.drawImage(img, center.x, center.y);
					}
				}

				e.gc.drawImage(backBuff, 0, 0);
			}
		});

		getDisplay().timerExec(100, new Runnable() {
			@Override
			public void run() {
				if (isDisposed())
					return;

				if (mouse.needRedraw()) {
					canvas.redraw();
				}

				if (play) {
					frame++;
					frame %= images.length;
					if (frame == 0) {
						dx = 0;
						dy = 0;
					}
					dx += frm.getFrames(side)[frame].getDx();
					dy += frm.getFrames(side)[frame].getDy();
					canvas.redraw();
				}

				getDisplay().timerExec(100, this);
			}
		});

		setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	@Override
	protected void checkSubclass() {
	}

	protected void cleanImages() {
		if (images != null)
			for (int i = 0; i < images.length; i++)
				images[i].dispose();
		images = null;
	}

	@Override
	public void dispose() {
		super.dispose();
		cleanImages();
	}

	public void resetImage() {
		frm = null;
		pal = null;
		cleanImages();
		canvas.redraw();
		mgr.removeAll();
		mgr.update(true);
	}

	public void fill() {

		try {
			InputStream frmStream = FID.getByFID(factory.getProject(), accessor.get(factory.getPro()));
			frm = new FRMImage(frmStream);
			pal = Fdt.getStdPal(factory.getProject());
		} catch (Exception e) {
			Fdt.getDefault().handleException(e);
			frm = null;
			return;
		}

		UpdateRotation();
		canvas.redraw();
		mgr.removeAll();
		if (images.length > 1) {
			mgr.add(playAction);
			if (play == true)
				playAction.run();
		}
		if (frm.isRotable()) {
			mgr.add(left);
			mgr.add(right);
		}
		mgr.add(center);
		mgr.add(change);
		center.run();
		mgr.update(true);
	}

	protected void UpdateRotation() {
		cleanImages();
		images = new Image[frm.getFrames(side).length];
		for (int i = 0; i < images.length; i++) {
			FRMFrame frame = frm.getFrames(side)[i];
			images[i] = FID.getImage(frame.getWidth(), frame.getHeight(), frame.getData(), pal);
		}
		frame = 0;
		dx = dy = 0;
	}

	public void setField(int field) {
		accessor = new BasicAccessor(field);
	}

	protected void createActions() {
		playAction = new Action("Play") {
			@Override
			public void run() {
				if (play)
					stop();
				else
					play();
			}

			public void play() {
				play = true;
				playAction.setText("Pause");
				playAction.setImageDescriptor(Fdt.getImageDescriptor("icons/suspend_co.gif"));
				mgr.update(true);
			}

			public void stop() {
				play = false;
				playAction.setText("Play");
				playAction.setImageDescriptor(Fdt.getImageDescriptor("icons/resume_co.gif"));
				mgr.update(true);
			}
		};
		playAction.setImageDescriptor(Fdt.getImageDescriptor("icons/resume_co.gif"));

		left = new Action("Left") {
			public void run() {
				side++;
				side = side > 5 ? 0 : side;
				UpdateRotation();
				canvas.redraw();
			}
		};
		left.setImageDescriptor(Fdt.getImageDescriptor("icons/undo_edit.gif"));

		right = new Action("Right") {
			public void run() {
				side--;
				side = side < 0 ? 5 : side;
				UpdateRotation();
				canvas.redraw();
			}

		};
		right.setImageDescriptor(Fdt.getImageDescriptor("icons/redo_edit.gif"));

		center = new Action() {
			public void run() {
				mouse.center();
			};
		};
		center.setImageDescriptor(Fdt.getImageDescriptor("icons/link_to_editor.gif"));

		change = new Action() {
			@Override
			public void run() {
				FidSelector fidselect = new FidSelector(getShell(), factory.getProject(), fidType);
				fidselect.create();
				if (fidselect.open() == Window.OK && fidselect.getSelection() != null) {
					IEntry ent = (IEntry) fidselect.getSelection();
					int fid = (ent.getIndex() - 1) | (fidType << 24);
					if (accessor.get(factory.getPro()) != fid) {
						accessor.set(factory.getPro(), fid);
						factory.getChangeListener().change();
						fill();
					}
				}
			}
		};
		change.setImageDescriptor(Fdt.getImageDescriptor("icons/add.gif"));

	}

}
