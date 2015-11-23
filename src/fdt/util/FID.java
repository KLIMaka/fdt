package fdt.util;

import static fdt.Fdt.PLUGIN_ID;
import static org.eclipse.core.runtime.IStatus.ERROR;

import java.io.InputStream;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import fdk.lst.LST;
import fdt.Fdt;

public class FID {
	public static PaletteData getPalette(byte[] data) {
		RGB rgb[] = new RGB[256];
		for (int i = 0; i < 256; i++)
			rgb[i] = new RGB((data[i * 3] << 2) & 0xff, (data[i * 3 + 1] << 2) & 0xff, (data[i * 3 + 2] << 2) & 0xff);

		return new PaletteData(rgb);
	}

	public static Image getImage(int w, int h, byte[] data, byte[] rawpal) {
		PaletteData pal = getPalette(rawpal);
		return getImage(w, h, data, pal);
	}

	public static Image getImage(int w, int h, byte[] data, PaletteData pal) {
		ImageData id = new ImageData(w, h, 8, pal);
		id.transparentPixel = 0;
		id.setPixels(0, 0, w * h, data, 0);
		return new Image(Display.getCurrent(), id);
	}

	public static final String types[] = { "items", "critters", "scenery", "walls", "tiles", "misc", "intrface", "inven", "heads", "backgrnd", "skilldex" };

	public static final int ITEMS = 0;
	public static final int CRITTERS = 1;
	public static final int SCENERY = 2;
	public static final int WALLS = 3;
	public static final int TILES = 4;
	public static final int MISK = 5;
	public static final int INTERFACE = 6;
	public static final int INVEN = 7;
	public static final int HEADS = 8;
	public static final int BACKGRND = 9;
	public static final int SKILLDEX = 10;

	public static String getFileNameByFID(IProject proj, int fid) {
		if (fid != -1) {
			int type = (fid >> 24) & 0x0f;
			int index = fid & 0x00000fff;
			try {
				LST ilst = Fdt.getCachedLST(proj, "art/" + types[type] + "/" + types[type] + ".lst");
				String sfrm = ilst.get(index != 0xfff ? index : 0).getValue();
				return "art/" + types[type] + "/" + sfrm;
			} catch (Exception e) {
				return "art/items/reserved.frm";
			}
		}
		return "art/items/reserved.frm";
	}

	public static InputStream getByFID(IProject proj, int fid) throws CoreException {
		if (fid != -1) {
			int type = (fid >> 24) & 0x0f;
			int index = fid & 0x00000fff;

			LST ilst = Fdt.getCachedLST(proj, "art/" + types[type] + "/" + types[type] + ".lst");

			String sfrm = ilst.get(index != 0xfff ? index : 0).getValue();
			String path = "art/" + types[type] + "/" + sfrm;
			IFile file = Fdt.getFile(proj, path);

			if (file == null) {
				throw new CoreException(new Status(ERROR, PLUGIN_ID, "File not found"));
			}

			InputStream frms = file.getContents();

			return frms;
		}

		return Fdt.getFile(proj, "art/items/reserved.frm").getContents();
	}
}
