package fdt.adapters;

import java.net.URI;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IAdapterFactory;

import fdk.lst.ScriptLstMaker;

public class AdapterFactory implements IAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof IFile && adapterType == ScriptAdapter.class) {
			IFile file = (IFile) adaptableObject;
			if (file.getFileExtension().equalsIgnoreCase("int"))
				return (T) new ScriptAdapter(((IFile) adaptableObject).getProject(), ((IFile) adaptableObject).getName());
		}

		if (adaptableObject instanceof ScriptLstMaker.Entry && adapterType == ScriptAdapter.class) {
			return (T) new ScriptAdapter(null, ((ScriptLstMaker.Entry) adaptableObject).getName());
		}

		if (adaptableObject instanceof IResource && adapterType == PackedFileAdapter.class) {
			IResource res = (IResource) adaptableObject;
			URI uri = res.getLocationURI();
			if (uri.getScheme().equals("dat") && uri.getFragment() != null) {
				return (T) new PackedFileAdapter((IResource) adaptableObject);
			}
		}

		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { ScriptAdapter.class, PackedFileAdapter.class };
	}

}
