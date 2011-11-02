package ssl.editors.frm;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

import ssl.SslPlugin;

import com.swtdesigner.SWTResourceManager;

import fdk.frm.FRMFrame;
import fdk.frm.FRMImage;

class MyMouse implements MouseListener, MouseMoveListener {
    private int     x_off  = 0;
    private int     y_off  = 0;

    private boolean m_down = false;
    private int     x_down;
    private int     y_down;

    private boolean m_needRedraw;
    private int     old_xoff;
    private int     old_yoff;

    @Override
    public void mouseMove(MouseEvent e) {
        if (m_down) {
            int dx = e.x - x_down;
            int dy = e.y - y_down;

            x_off = old_xoff + dx;
            y_off = old_yoff + dy;
            m_needRedraw = true;
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (e.button == 1) {
            m_down = true;
            x_down = e.x;
            y_down = e.y;
            old_xoff = x_off;
            old_yoff = y_off;
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (leftButton(e)) {
            m_down = false;
        }
    }

    private boolean leftButton(MouseEvent e) {
        return (e.stateMask & SWT.BUTTON1) == SWT.BUTTON1;
    }

    public int getX_off() {
        return x_off;
    }

    public int getY_off() {
        return y_off;
    }

    public boolean needRedraw() {
        boolean tmp = m_needRedraw;
        m_needRedraw = false;
        return tmp;
    }

    public void center() {
        x_off = y_off = 0;
        m_needRedraw = true;
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {}
}

public class FRMPanel extends Composite {

    private Image          m_images[] = null;
    private Control        m_canvas;
    private boolean        m_play     = false;
    private int            m_frame    = 0;
    private int            m_side     = 3;

    private Action         m_playAction;
    private ToolBarManager m_mgr;

    private FRMImage       m_frm      = null;
    private int            m_dx, m_dy;
    private Action         m_left;
    private Action         m_right;
    private PaletteData    m_pal;

    private MyMouse        m_mouse    = new MyMouse();
    private Action         m_center;
    private int            m_fidType;
    private IProject       m_proj;

    // private Texture tex;
    //
    // private static boolean first = false;
    // private static GLCanvas share = null;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public FRMPanel(Composite parent, IProject proj, int fidtype) {

        super(parent, SWT.BORDER);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);

        m_fidType = fidtype;
        m_proj = proj;

        createActions();

        ToolBar toolBar_1 = new ToolBar(this, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
        toolBar_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

        m_mgr = new ToolBarManager(toolBar_1);
        m_mgr.update(true);

        m_canvas = new Canvas(this, SWT.NONE);
        m_canvas.setBackground(SWTResourceManager.getColor(65, 110, 155));
        m_canvas.addMouseListener(m_mouse);
        m_canvas.addMouseMoveListener(m_mouse);
        m_canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        m_canvas.setCursor(new Cursor(getDisplay(), SWT.CURSOR_SIZEALL));

        m_canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                final Image backBuff = new Image(getDisplay(), m_canvas.getBounds());
                final GC backGC = new GC(backBuff);

                backGC.setBackground(m_canvas.getBackground());
                backGC.fillRectangle(0, 0, m_canvas.getBounds().width, m_canvas.getBounds().height);

                if (m_images != null) {
                    if (m_images.length == 1) {
                        Point center = m_canvas.getSize();
                        Image img = m_images[m_frame];
                        int x = (center.x - img.getBounds().width) / 2 + m_mouse.getX_off();
                        int y = (center.y - img.getBounds().height) / 2 + m_mouse.getY_off();
                        backGC.drawImage(img, x, y);

                    } else {
                        Point center = m_canvas.getSize();
                        center.y += m_images[0].getBounds().height;
                        Image img = m_images[m_frame];
                        center.x = center.x / 2 - img.getBounds().width / 2 + m_dx + m_mouse.getX_off();
                        center.y = center.y / 2 - img.getBounds().height + m_dy + m_mouse.getY_off();
                        backGC.drawImage(img, center.x, center.y);
                    }
                }

                e.gc.drawImage(backBuff, 0, 0);
            }
        });

        getDisplay().timerExec(100, new Runnable() {
            @Override
            public void run() {
                if (isDisposed()) return;

                if (m_mouse.needRedraw()) {
                    m_canvas.redraw();
                }

                if (m_play) {
                    m_frame++;
                    m_frame %= m_images.length;
                    if (m_frame == 0) {
                        m_dx = 0;
                        m_dy = 0;
                    }
                    m_dx += m_frm.getFrames(m_side)[m_frame].getDx();
                    m_dy += m_frm.getFrames(m_side)[m_frame].getDy();
                    m_canvas.redraw();
                }

                getDisplay().timerExec(100, this);
            }
        });

        setBackgroundMode(SWT.INHERIT_DEFAULT);
    }

    @Override
    protected void checkSubclass() {}

    protected void cleanImages() {
        if (m_images != null) for (int i = 0; i < m_images.length; i++)
            m_images[i].dispose();
        m_images = null;
    }

    @Override
    public void dispose() {
        super.dispose();
        cleanImages();
    }

    public void resetImage() {
        m_frm = null;
        m_pal = null;
        cleanImages();
        m_canvas.redraw();
        m_mgr.removeAll();
        m_mgr.update(true);
    }

    public void setFID(int fid) {

        InputStream frmStream = FID.getByFID(m_proj, fid);
        try {
            m_frm = new FRMImage(frmStream);
            m_pal = SslPlugin.getStdPal(m_proj);
        } catch (IOException e) {
            e.printStackTrace();
            m_frm = null;
            return;
        }

        UpdateRotation();
        m_canvas.redraw();
        m_mgr.removeAll();
        if (m_images.length > 1) {
            m_mgr.add(m_playAction);
            if (m_play == true) m_playAction.run();
        }
        if (m_frm.isRotable()) {
            m_mgr.add(m_left);
            m_mgr.add(m_right);
        }
        m_mgr.add(m_center);
        m_center.run();
        m_mgr.update(true);
    }

    protected void UpdateRotation() {
        cleanImages();
        m_images = new Image[m_frm.getFrames(m_side).length];
        for (int i = 0; i < m_images.length; i++) {
            FRMFrame frm = m_frm.getFrames(m_side)[i];
            m_images[i] = FID.getImage(frm.getWidth(), frm.getHeight(), frm.getData(), m_pal);
        }
        m_frame = 0;
        m_dx = m_dy = 0;
    }

    protected void createActions() {
        m_playAction = new Action("Play") {
            @Override
            public void run() {
                if (m_play)
                    stop();
                else play();
            }

            public void play() {
                m_play = true;
                m_playAction.setText("Pause");
                m_playAction.setImageDescriptor(SslPlugin.getImageDescriptor("icons/suspend_co.gif"));
                m_mgr.update(true);
            }

            public void stop() {
                m_play = false;
                m_playAction.setText("Play");
                m_playAction.setImageDescriptor(SslPlugin.getImageDescriptor("icons/resume_co.gif"));
                m_mgr.update(true);
            }
        };
        m_playAction.setImageDescriptor(SslPlugin.getImageDescriptor("icons/resume_co.gif"));

        m_left = new Action("Left") {
            public void run() {
                m_side++;
                m_side = m_side > 5 ? 0 : m_side;
                UpdateRotation();
                m_canvas.redraw();
            }
        };
        m_left.setImageDescriptor(SslPlugin.getImageDescriptor("icons/undo_edit.gif"));

        m_right = new Action("Right") {
            public void run() {
                m_side--;
                m_side = m_side < 0 ? 5 : m_side;
                UpdateRotation();
                m_canvas.redraw();
            }

        };
        m_right.setImageDescriptor(SslPlugin.getImageDescriptor("icons/redo_edit.gif"));

        m_center = new Action() {
            public void run() {
                m_mouse.center();
            };
        };
        m_center.setImageDescriptor(SslPlugin.getImageDescriptor("icons/link_to_editor.gif"));

    }

}
