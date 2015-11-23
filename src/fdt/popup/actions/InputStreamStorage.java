package fdt.popup.actions;

import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.*;

class InputStreamStorage implements IStorage {
	private InputStream m_input;
	private String m_name;

	InputStreamStorage(InputStream input, String name) {
		m_input = input;
		m_name = name;
	}

	public InputStream getContents() throws CoreException {
		return m_input;
	}

	public IPath getFullPath() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getName() {
		return m_name;
	}

	public boolean isReadOnly() {
		return true;
	}
}
