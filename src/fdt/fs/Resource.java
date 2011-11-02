package fdt.fs;

import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;

import fdk.fs.IFileSource;

public class Resource {

    private IFileSource m_source    = null;
    private IFileStore  m_rootStore = null;
    private URI         m_uri       = null;
    private IFileSystem m_fs        = null;

    void setFs(DatFS fs) {
        m_fs = fs;
    }

    IFileSystem getFs() {
        return m_fs;
    }

    void setUri(URI uri) {
        m_uri = uri;
    }

    URI getUri() {
        return m_uri;
    }

    void setRootStore(DatDir rootStore) {
        m_rootStore = rootStore;
    }

    IFileStore getRootStore() {
        return m_rootStore;
    }

    void setSource(IFileSource source) {
        m_source = source;
    }

    IFileSource getSource() {
        return m_source;
    }

}
