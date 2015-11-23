package fdt.adapters;

import org.eclipse.core.resources.IResource;

public class PackedFileAdapter {
	private IResource res;

	PackedFileAdapter(IResource res) {
		this.res = res;
	}

	public IResource getRes() {
		return res;
	}
}
