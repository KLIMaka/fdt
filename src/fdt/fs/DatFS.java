package fdt.fs;

import java.io.File;
import java.net.URI;
import java.util.*;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileSystem;
import org.eclipse.core.runtime.Path;

import fdk.cfg.CFGFile;
import fdk.fs.IFileSource;
import fdk.fs.dat.DatFile;
import fdk.fs.override.OverridableFS;

public class DatFS extends FileSystem {

	private Map<String, Resource> m_cache = new TreeMap<String, Resource>();

	protected IFileSource getSource(CFGFile file, String name) throws Exception {
		File src = file.getPath(name);

		if (src.isDirectory()) {
			return new fdk.fs.file.FileSystem(src);
		} else {
			return new DatFile(src.toString());
		}
	}

	protected IFileSource createFileSource(String cfgPath) throws Exception {
		CFGFile cfg = new CFGFile(cfgPath);

		OverridableFS ofs = new OverridableFS(getSource(cfg, "system.master_dat"));
		ofs.addOverride(getSource(cfg, "system.critter_dat"));

		return ofs;
	}

	protected String getActualPath(String str) {
		return str.replaceFirst(".*?/", "");
	}

	@Override
	public IFileStore getStore(URI uri) {
		Resource newRes = new Resource();
		try {
			String decodedQuery = uri.getQuery();
			Resource res = m_cache.get(decodedQuery);

			if (res == null) {
				String query = getActualPath(decodedQuery);

				newRes.setSource(createFileSource(query));
				newRes.setFs(this);
				newRes.setUri(uri);
				newRes.setRootStore(new DatDir(newRes.getSource().getRoot(), newRes, null));

				m_cache.put(decodedQuery, newRes);
				res = newRes;
			}

			if (uri.getFragment() == null) {
				return res.getRootStore();
			} else {
				String path = uri.getFragment();
				return res.getRootStore().getFileStore(new Path(path));
			}
		} catch (Exception e) {
			m_cache.put(uri.getQuery(), newRes);
		}
		return null;
	}
}
