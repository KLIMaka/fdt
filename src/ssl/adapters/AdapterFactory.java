package ssl.adapters;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import fdk.lst.ScriptLstMaker;

public class AdapterFactory implements IAdapterFactory {

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof IFile && adapterType == ScriptAdapter.class) {
            IFile file = (IFile) adaptableObject;
            if (file.getFileExtension().equalsIgnoreCase("int"))
                return new ScriptAdapter(((IFile) adaptableObject).getProject(), ((IFile) adaptableObject).getName());
        }

        if (adaptableObject instanceof ScriptLstMaker.Entry && adapterType == ScriptAdapter.class) {
            return new ScriptAdapter(null, ((ScriptLstMaker.Entry) adaptableObject).getName());
        }

        if (adaptableObject instanceof IResource && adapterType == PackedFileAdapter.class) {
            IResource res = (IResource) adaptableObject;
            URI uri = res.getLocationURI();
            if (uri.getScheme().equals("dat") && uri.getFragment() != null) {
                return new PackedFileAdapter((IResource) adaptableObject);
            }
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class[] getAdapterList() {
        return new Class[] { ScriptAdapter.class, PackedFileAdapter.class };
    }

}
