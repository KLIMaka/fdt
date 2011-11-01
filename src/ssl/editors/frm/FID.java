package ssl.editors.frm;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import ssl.SslPlugin;
import fdk.lst.LST;

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

    public static final String types[]   = { "items", "critters", "scenery", "walls", "tiles", "misc", "intrface",
            "inven", "heads", "backgrnd", "skilldex" };

    public static final int    ITEMS     = 0;
    public static final int    CRITTERS  = 1;
    public static final int    SCENERY   = 2;
    public static final int    WALLS     = 3;
    public static final int    TILES     = 4;
    public static final int    MISK      = 5;
    public static final int    INTERFACE = 6;
    public static final int    INVEN     = 7;
    public static final int    HEADS     = 8;
    public static final int    BACKGRND  = 9;
    public static final int    SKILLDEX  = 10;

    public static String getFileNameByFID(IProject proj, int fid) {
        if (fid != -1) {
            int type = (fid >> 24) & 0x0f;
            int index = fid & 0x00000fff;
            try {
                LST ilst = SslPlugin.getCachedLST(proj, "art/" + types[type] + "/" + types[type] + ".lst");
                String sfrm = ilst.get(index != 0xfff ? index : 0).getValue();
                return "art/" + types[type] + "/" + sfrm;
            } catch (Exception e) {
                return "art/items/reserved.frm";
            }
        }
        return "art/items/reserved.frm";
    }

    public static InputStream getByFID(IProject proj, int fid) {
        if (fid != -1) {
            int type = (fid >> 24) & 0x0f;
            int index = fid & 0x00000fff;

            try {
                LST ilst = SslPlugin.getCachedLST(proj, "art/" + types[type] + "/" + types[type] + ".lst");

                String sfrm = ilst.get(index != 0xfff ? index : 0).getValue();
                IFile file = SslPlugin.getFile(proj, "art/" + types[type] + "/" + sfrm);

                if (file == null) {
                    System.err.println(sfrm);
                    throw new IOException();
                }

                InputStream frms = file.getContents();

                return frms;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        try {
            return SslPlugin.getFile(proj, "art/items/reserved.frm").getContents();
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
