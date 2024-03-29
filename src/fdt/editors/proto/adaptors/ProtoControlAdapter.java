package fdt.editors.proto.adaptors;

import java.util.ArrayList;
import java.util.regex.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import fdk.lst.IEntry;
import fdk.msg.*;
import fdk.proto.*;
import fdt.Fdt;
import fdt.controls.frm.list.selectors.PidSelector;
import fdt.editors.proto.IChangeListener;
import fdt.editors.proto.accessor.IProtoAccessor;
import fdt.util.Ref;

public class ProtoControlAdapter {

	interface IVerifier {
		public int verify(String str) throws Exception;
	}

	private IProject m_proj;
	private Ref<Prototype> m_proto;
	private Ref<MSG> m_msg;
	private IChangeListener m_changeListener;

	private ArrayList<Runnable> m_fillers = new ArrayList<Runnable>();

	public ProtoControlAdapter(IProject proj, Ref<Prototype> proto, Ref<MSG> msg, IChangeListener cl) {
		m_proj = proj;
		m_proto = proto;
		m_msg = msg;
		m_changeListener = cl;
	}

	public void adoptMsg(final Text text, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {
			@Override
			public void run() {
				int index = ac.get(m_proto);
				MsgEntry msg = m_msg.get().get(index);
				text.setText(msg == null ? "" : msg.getMsg());
			}
		});

		text.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				int index = ac.get(m_proto);
				MsgEntry msg = m_msg.get().get(index);
				if (msg == null) {
					msg = new MsgEntry(index, "", "", "", "");
					m_msg.get().put(msg);
				}
				if (!msg.getMsg().equals(text.getText())) {
					msg.setMsg(text.getText().replace('{', ' ').replace('}', ' '));
					m_changeListener.change();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

	}

	public void adopt(final Text text, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {
			@Override
			public void run() {
				text.setText(String.valueOf(ac.get(m_proto)));
			}
		});
		addListeners(text, ac, new IVerifier() {
			@Override
			public int verify(String str) {
				return Integer.valueOf(str);
			}
		});
	}

	public void adoptPIDSelector(final Button btn, final IProtoAccessor ac, final int pidType, final int filter) {
		final Runnable filler = new Runnable() {
			@Override
			public void run() {
				int pid = ac.get(m_proto);
				if (pid == -1) {
					btn.setText("None");
				} else {
					int type = (pid >> 24) & 0x0f;
					int id = pid & 0x0000ffff;
					MSG msg = Fdt.getCachedMsg(m_proj, PRO.getMsg(type));
					if (msg.get(id * 100) == null) {
						btn.setText("<empty>");
					} else {
						btn.setText(msg.get(id * 100).getMsg());
					}
				}
			}
		};
		m_fillers.add(filler);

		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PidSelector pidselect = new PidSelector(btn.getShell(), m_proj, pidType, filter);
				pidselect.create();
				if (pidselect.open() == Window.OK && pidselect.getSelection() != null) {
					int pid;
					if (!(pidselect.getSelection() instanceof IEntry)) {
						pid = -1;
					} else {
						pid = (((IEntry) pidselect.getSelection()).getIndex()) | (pidType << 24);
					}
					if (pid != ac.get(m_proto)) {
						ac.set(m_proto, pid);
						filler.run();
						m_changeListener.change();
					}
				}
			}
		});
	}

	public void adopt(final Button btn, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {
			@Override
			public void run() {
				btn.setSelection(ac.get(m_proto) == 0 ? false : true);
			}
		});

		btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int sel = btn.getSelection() ? 1 : 0;
				if (ac.get(m_proto) != sel) {
					ac.set(m_proto, sel);
					m_changeListener.change();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	public void adoptHex(final Text text, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {
			@Override
			public void run() {
				text.setText("0x" + Integer.toHexString(ac.get(m_proto)).toUpperCase());
			}
		});
		addListeners(text, ac, new IVerifier() {
			@Override
			public int verify(String str) throws Exception {
				str = str.replaceFirst("^0x", "");
				return Integer.parseInt(str, 16);
			}
		});
	}

	public void adopt(final Combo combo, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {
			@Override
			public void run() {
				combo.select(ac.get(m_proto));
			}
		});
		addListeners(combo, ac);
	}

	protected void addListeners(final Combo combo, final IProtoAccessor ac) {
		combo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (ac.get(m_proto) != combo.getSelectionIndex()) {
					m_changeListener.change();
					ac.set(m_proto, combo.getSelectionIndex());
				}
			}
		});
	}

	protected void addListeners(final Text text, final IProtoAccessor ac, final IVerifier ver) {

		final ControlDecoration decorator = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		Image img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR);
		decorator.hide();
		decorator.setImage(img);
		decorator.setShowOnlyOnFocus(true);

		text.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.CR) {
					try {
						int val = ver.verify(text.getText());
						if (val != ac.get(m_proto)) {
							m_changeListener.change();
							ac.set(m_proto, val);
						}
						decorator.hide();
					} catch (Exception ex) {
						decorator.show();
						decorator.showHoverText("Error input");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					int val = ver.verify(text.getText());
					if (val != ac.get(m_proto)) {
						m_changeListener.change();
						ac.set(m_proto, val);
					}
					decorator.hide();
				} catch (Exception ex) {
					decorator.show();
					decorator.showHoverText("Error input");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}

	private static String m2h(int m) {
		int h = m / 60;
		int mm = m % 60;
		return mm == 0 ? h + "h" : h + "h " + mm + "m";
	}

	private static int h2m(String h) throws Exception {
		final Pattern p = Pattern.compile("^((\\d+)|(\\d+)h|(\\d+)h (\\d+)m|(\\d+)m)$");
		Matcher m = p.matcher(h.trim());
		if (m.find()) {
			if (m.group(2) != null) {
				return Integer.valueOf(m.group(2));
			} else if (m.group(3) != null) {
				return Integer.valueOf(m.group(3)) * 60;
			} else if (m.group(4) != null) {
				return Integer.valueOf(m.group(4)) * 60 + Integer.valueOf(m.group(5));
			} else {
				return Integer.valueOf(m.group(6));
			}
		} else {
			throw new Exception();
		}
	}

	public void adoptM2H(final Text text, final IProtoAccessor ac) {
		m_fillers.add(new Runnable() {

			@Override
			public void run() {
				text.setText(m2h(ac.get(m_proto)));
			}
		});
		addListeners(text, ac, new IVerifier() {
			@Override
			public int verify(String str) throws Exception {
				return h2m(str);
			}
		});
	}

	public void fill() {
		for (Runnable f : m_fillers) {
			f.run();
		}
	}
}
