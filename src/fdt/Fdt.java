package fdt;

import static java.lang.System.arraycopy;
import static org.eclipse.core.resources.IResourceChangeEvent.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

import fdk.frm.*;
import fdk.lst.*;
import fdk.msg.MSG;
import fdt.util.FID;

public class Fdt extends AbstractUIPlugin {

	public static final String SSL_SETTINGS_SCOPE = "SSL";
	public static final String PLUGIN_ID = "fdt";

	private static Fdt plugin;

	public Fdt() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IResourceChangeListener listener = new FdtResourceChangeReporter();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, PRE_CLOSE | PRE_DELETE | PRE_BUILD | POST_BUILD | POST_CHANGE);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Fdt getDefault() {
		return plugin;
	}

	public void handleException(Exception e) {
		if (e instanceof CoreException)
			StatusManager.getManager().handle((CoreException) e, PLUGIN_ID);
		else
			StatusManager.getManager().handle(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private static Map<IProject, Map<String, FRMFrame>> infoFrmCache = new HashMap<IProject, Map<String, FRMFrame>>();
	private final static String RESERVED_FRM = "art/items/reserved.frm";

	public static synchronized FRMFrame getFRM(IProject proj, String name) {
		Map<String, FRMFrame> cache = infoFrmCache.get(proj);
		if (cache == null) {
			cache = new HashMap<String, FRMFrame>();
			infoFrmCache.put(proj, cache);
		}

		try {
			FRMFrame frm = cache.get(name);
			if (frm == null) {
				FRMImage img = new FRMImage(getFile(proj, name).getContents());
				if (img.isRotable()) {
					frm = img.getFrames(3)[0];
				} else {
					frm = img.getFrames(0)[0];
				}
				cache.put(name, frm);
			}
			return frm;
		} catch (Exception e) {
			FRMImage reserved;
			try {
				reserved = new FRMImage(getFile(proj, RESERVED_FRM).getContents());
				return reserved.getFrames(0)[0];
			} catch (Exception e1) {
				plugin.handleException(e1);
				return null;
			}
		}
	}

	public static FRMFrame getFRMByFID(IProject proj, int fid) {
		String fname = FID.getFileNameByFID(proj, fid);
		return getFRM(proj, fname);
	}

	private static Map<IProject, Map<String, MSG>> cachedMsg = new HashMap<IProject, Map<String, MSG>>();

	public static synchronized MSG getCachedMsg(IProject proj, String fname) {
		Map<String, MSG> cache = cachedMsg.get(proj);
		if (cache == null) {
			cache = new HashMap<String, MSG>();
			cachedMsg.put(proj, cache);
		}

		// TODO resourses changes, charset changes
		MSG msg = cache.get(fname);
		if (msg == null) {
			try {
				Charset cs = Charset.forName(proj.getDefaultCharset());
				msg = new MSG(getFile(proj, fname).getContents(), cs);
				cache.put(fname, msg);
			} catch (Exception e) {
				plugin.handleException(e);
				return new MSG();
			}
		}
		return msg;
	}

	private static Map<IProject, Map<String, LST>> cachedLst = new HashMap<IProject, Map<String, LST>>();

	public static synchronized LST getCachedLST(IProject proj, String name) {
		Map<String, LST> cache = cachedLst.get(proj);
		if (cache == null) {
			cache = new HashMap<String, LST>();
			cachedLst.put(proj, cache);
		}

		LST lst = cache.get(name);
		if (lst == null) {
			try {
				Charset cs = Charset.forName(proj.getDefaultCharset());
				IEntryMaker maker = getLstMaker(name);
				lst = new LST(getFile(proj, name).getContents(), cs, maker);
				cache.put(name, lst);
			} catch (Exception e) {
				plugin.handleException(e);
				return new LST();
			}
		}
		return lst;
	}

	private static final Pattern ART_DIR = Pattern.compile("art\\/(.*)\\/");
	private static final String SCRIPTS_LST = "scripts/scripts.lst";
	private static final String CRITTERS_LST = "proto/critters/critters.lst";

	private static IEntryMaker getLstMaker(String name) {
		Matcher m = ART_DIR.matcher(name);
		if (m.find()) {
			return new ArtMaker(m.group(1));
		} else if (name.equalsIgnoreCase(SCRIPTS_LST)) {
			return new ScriptLstMaker();
		} else if (name.equalsIgnoreCase(CRITTERS_LST)) {
			return new CrittersEntryMaker();
		}
		return new BasicEntryMaker();
	}

	private static Map<IProject, PaletteData> stdPal = new HashMap<IProject, PaletteData>();
	private static final String PAL = "color.pal";

	public static PaletteData getStdPal(IProject proj) {
		PaletteData pal = stdPal.get(proj);
		if (pal == null) {
			try {
				InputStream palStream = getFile(proj, PAL).getContents();
				byte rawpal[] = new byte[256 * 3];
				DataInputStream ds = new DataInputStream(palStream);
				ds.readFully(rawpal);
				addAnimPal(rawpal);
				pal = FID.getPalette(rawpal);
				stdPal.put(proj, pal);
			} catch (Exception e) {
				plugin.handleException(e);
				return null;
			}
		}
		return pal;
	}

	private static byte[] shr2(byte[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] >>= 2;
		}
		return arr;
	}

	private static void addAnimPal(byte[] rawpal) {
		final byte[] Slime = shr2(new byte[] { 0, 108, 0, 11, 115, 7, 27, 123, 15, 43, (byte) 131, 27 });
		final byte[] Monitors = shr2(new byte[] { 107, 107, 111, 99, 103, 127, 87, 107, (byte) 143, 0, (byte) 147, (byte) 163, 107, (byte) 187, (byte) 255 });
		final byte[] FireSlow = shr2(new byte[] { (byte) 255, 0, 0, (byte) 215, 0, 0, (byte) 147, 43, 11, (byte) 255, 119, 0, (byte) 255, 59, 0 });
		final byte[] FireFast = shr2(new byte[] { 71, 0, 0, 123, 0, 0, (byte) 179, 0, 0, 123, 0, 0, 71, 0, 0 });
		final byte[] Shoreline = shr2(new byte[] { 83, 63, 43, 75, 59, 43, 67, 55, 39, 63, 51, 39, 55, 47, 35, 51, 43, 35 });
		final byte[] BlinkRed = shr2(new byte[] { (byte) 252, 0, 0 });

		arraycopy(Slime, 0, rawpal, 229 * 3, Slime.length);
		arraycopy(Monitors, 0, rawpal, 233 * 3, Monitors.length);
		arraycopy(FireSlow, 0, rawpal, 238 * 3, FireSlow.length);
		arraycopy(FireFast, 0, rawpal, 243 * 3, FireFast.length);
		arraycopy(Shoreline, 0, rawpal, 248 * 3, Shoreline.length);
		arraycopy(BlinkRed, 0, rawpal, 254 * 3, BlinkRed.length);

	}

	private static IResource getResourceAbsolute(IProject project, String path) {
		try {
			IPath p = new Path(path);

			IContainer prev = null;
			IContainer current = project;
			for (int i = 0; i < p.segmentCount(); i++) {
				if (current == prev)
					break;

				for (IResource r : current.members()) {
					if (r.getName().equalsIgnoreCase(p.segment(i))) {
						if (r instanceof IContainer) {
							if (i == p.segmentCount() - 1)
								return r;
							else {
								prev = current;
								current = (IContainer) r;
								break;
							}
						} else if (r instanceof IFile && i == p.segmentCount() - 1) {
							IFile f = (IFile) r;
							f.refreshLocal(IResource.DEPTH_ZERO, null);
							return f;
						}
					}
				}
			}
		} catch (CoreException e) {
			plugin.handleException(e);
		}

		return null;
	}

	public static IResource getResource(IProject project, String name) {
		IResource result = getResourceAbsolute(project, "Patches/" + name);
		if (result != null)
			return result;

		return getResourceAbsolute(project, "Resources/" + name);
	}

	public static IFile getFile(IProject project, String name) {
		return (IFile) getResource(project, name);
	}

	public static IFolder getFolder(IProject project, String name) {
		return (IFolder) getResource(project, name);
	}

	private static void fcreate(IFolder fld) throws CoreException {
		if (!fld.exists()) {
			fcreate((IFolder) fld.getParent());
			fld.create(true, true, null);
		}
	}

	public static IFolder getFolderForce(IProject proj, String name) throws CoreException {
		IResource result = getResourceAbsolute(proj, "Patches/" + name);
		if (result != null)
			return (IFolder) result;
		IFolder fld = proj.getFolder("Patches/" + name);
		fcreate(fld);
		return fld;
	}
}
