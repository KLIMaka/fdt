package ssl.adapters;

import org.eclipse.core.resources.IResource;

public class PackedFileAdapter {
    private IResource m_res;

    PackedFileAdapter(IResource res) {
        m_res = res;
    }

    public IResource getRes() {
        return m_res;
    }
}
