package fdt.controls.frm.list;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import fdk.frm.FRMFrame;
import fdt.controls.frm.list.providers.*;
import fdt.util.FID;

public class FRMList extends Composite {

	private ArrayList<Image> m_cachedImages = new ArrayList<Image>();
	// private IFrmDescriptor[] m_descs;
	private int cellW;
	private int cellH;
	private int topLeftCell;
	private int visibleCols;
	private int visibleRows;
	private int cellSpacingW = 10;
	private int cellSpacingH = 20;
	private PaletteData pal;
	private Slider slider;
	private Canvas canvas;

	private IFrmListLabelProvider labelProvider;
	private IFrmListContentProvider contentProvider;
	private Object[] elements;
	private Object selected;

	private ArrayList<ISelectionListener> selectionListeners = new ArrayList<ISelectionListener>();
	private Thread preloader;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 */
	public FRMList(Composite parent) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		canvas = new Canvas(this, SWT.NO_BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		canvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				recalcSize();
			}
		});
		canvas.setBackground(new Color(getDisplay(), new RGB(90, 90, 90)));
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				final Color bg = new Color(getDisplay(), 90, 90, 90);
				final Image image = new Image(getDisplay(), canvas.getBounds());
				final GC gcImage = new GC(image);

				gcImage.setBackground(bg);
				gcImage.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);

				for (int i = 0; i < m_cachedImages.size(); i++) {
					boolean selected = elements[topLeftCell + i] == FRMList.this.selected ? true : false;
					redrawCell(i, gcImage, selected);
				}
				e.gc.drawImage(image, 0, 0);
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				slider.setSelection(slider.getSelection() - e.count);
				scroll();
			}
		});

		addSelectionListener(new ISelectionListener() {
			@Override
			public void select(Object selected) {
				FRMList.this.selected = selected;
				canvas.redraw();
			}
		});

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int x = e.x / (cellW + cellSpacingW);
				int y = e.y / (cellH + cellSpacingH);

				if (x >= visibleCols || y >= visibleRows) {
					return;
				}

				int id = topLeftCell + x + y * visibleCols;
				if (id >= elements.length) {
					return;
				}

				Object o = elements[id];
				for (ISelectionListener sl : selectionListeners) {
					sl.select(o);
				}
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					slider.setSelection(slider.getSelection() + 1);
				}
				if (e.keyCode == SWT.ARROW_UP) {
					slider.setSelection(slider.getSelection() - 1);
				}
				if (e.keyCode == SWT.ARROW_RIGHT) {
					slider.setSelection(slider.getSelection() + visibleRows);
				}
				if (e.keyCode == SWT.ARROW_LEFT) {
					slider.setSelection(slider.getSelection() - visibleRows);
				}
				scroll();
			}
		});

		slider = new Slider(this, SWT.VERTICAL);
		slider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scroll();
			}
		});
	}

	protected void scroll() {
		if ((topLeftCell - slider.getSelection() * visibleCols) != 0) {
			topLeftCell = slider.getSelection() * visibleCols;
			int ns = elements.length - topLeftCell;
			int vc = visibleCols * visibleRows;
			ns = ns < 0 ? 0 : ns;
			ns = ns > vc ? vc : ns;
			cacheImages(ns);
			canvas.redraw();
		}
	}

	@Override
	public void dispose() {
		for (Image img : m_cachedImages) {
			img.dispose();
		}
		if (preloader != null) {
			preloader.interrupt();
		}
		super.dispose();
	}

	public void setLabelProvider(IFrmListLabelProvider lp) {
		labelProvider = lp;
	}

	public void setContentProvider(IFrmListContentProvider cp) {
		contentProvider = cp;
	}

	private void preloadImages(final Object[] lst) {
		if (preloader != null) {
			preloader.interrupt();
		}
		preloader = new Thread(new Runnable() {
			@Override
			public void run() {
				for (Object o : lst) {
					if (Thread.interrupted())
						return;
					labelProvider.getImage(o);
				}
			}
		}, "FRMListPreloader");
	}

	public void setInput(Object input) {
		contentProvider.inputChanged(this, input, input);
		elements = contentProvider.getElements();
		preloadImages(elements);
		recalcSize();
	}

	@Override
	protected void checkSubclass() {
	}

	public void setPal(PaletteData pal) {
		this.pal = pal;
	}

	public void setCellSize(int w, int h) {
		cellW = w;
		cellH = h;
	}

	@Override
	public void layout() {
		super.layout();
		recalcSize();
	}

	protected void recalcSize() {
		int w = getBounds().width;
		int h = getBounds().height;
		visibleCols = w / (cellW + cellSpacingW);
		visibleRows = h / (cellH + cellSpacingH);
		int visibleCells = visibleCols * visibleRows;

		int rest = elements.length - topLeftCell;
		visibleCells = rest < visibleCells ? rest : visibleCells;

		cacheImages(visibleCells);

		if (visibleCells != 0) {
			int rows = (int) Math.ceil((float) elements.length / (float) visibleCols);
			slider.setMaximum(rows + visibleRows + 2);
		}
	}

	protected void redrawCell(int idx, GC gc, boolean selected) {
		int x = cellSpacingW + (idx % visibleCols) * (cellW + cellSpacingW);
		int y = cellSpacingH + (idx / visibleCols) * (cellH + cellSpacingH);
		int w = m_cachedImages.get(idx).getBounds().width;
		int h = m_cachedImages.get(idx).getBounds().height;
		float imgAspect = (float) w / (float) h;

		int drawW = w;
		int drawH = h;
		if (drawW > cellW) {
			drawW = cellW;
			drawH = (int) (drawW / imgAspect);
		}
		if (drawH > cellW) {
			drawH = cellH;
			drawW = (int) (drawH * imgAspect);
		}

		int drawX = x + (cellW - drawW) / 2;
		int drawY = y + (cellH - drawH) / 2;

		final Color bg = new Color(getDisplay(), 65, 110, 155);
		final Color bg1 = new Color(getDisplay(), 90, 90, 90);
		final Color font = new Color(getDisplay(), 251, 255, 125);
		final Color black = new Color(getDisplay(), 0, 0, 0);

		gc.setBackground(bg);
		gc.setForeground(black);
		gc.fillRectangle(x, y, cellW, cellH);
		gc.drawRectangle(x, y, cellW, cellH);
		if (selected) {
			gc.setForeground(font);
			gc.drawRectangle(x - 1, y - 1, cellW + 2, cellH + 2);
		}

		if (drawW != w || drawH != h) {
			gc.setInterpolation(SWT.LOW);
		}
		gc.drawImage(m_cachedImages.get(idx), 0, 0, w, h, drawX, drawY, drawW, drawH);
		gc.setInterpolation(SWT.DEFAULT);

		gc.setBackground(bg1);
		gc.setForeground(black);
		String text = labelProvider.getText(elements[topLeftCell + idx]);
		gc.setClipping(x, y + cellH, cellW, 18);
		gc.drawText(text, x + 1 + 5, y + cellH + 2);
		gc.setForeground(font);
		gc.drawText(text, x + 5, y + cellH + 1, true);
		gc.setClipping((Rectangle) null);
		gc.setBackground(bg);
	}

	protected void cacheImages(int size) {
		if (m_cachedImages != null) {
			for (Image img : m_cachedImages) {
				img.dispose();
			}
		}
		m_cachedImages.clear();
		for (int i = 0; i < size; i++) {
			FRMFrame frm = labelProvider.getImage(elements[topLeftCell + i]);
			m_cachedImages.add(FID.getImage(frm.getWidth(), frm.getHeight(), frm.getData(), pal));
		}
	}

	public void addSelectionListener(ISelectionListener sl) {
		selectionListeners.add(sl);
	}

	public Object getSelection() {
		return selected;
	}

}
