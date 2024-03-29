package fdt.popup.actions;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;

class InputStreamInput implements IStorageEditorInput {
	private IStorage storage;

	InputStreamInput(IStorage storage) {
		this.storage = storage;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return storage.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public IStorage getStorage() {
		return storage;
	}

	public String getToolTipText() {
		return "String-based file: " + storage.getName();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}
}
