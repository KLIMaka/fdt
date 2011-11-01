package ssl.popup.actions;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import ssl.adapters.PackedFileAdapter;

public class ExctractAction implements IObjectActionDelegate {

    private Shell      m_shell;
    private ISelection m_selection;

    /**
     * Constructor for Action1.
     * 
     * @wbp.parser.entryPoint
     */
    public ExctractAction() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        m_shell = targetPart.getSite().getShell();
    }

    protected void copyContent(IResource src, IResource dst) throws CoreException {
        if (dst.exists()) {
            if (dst.getType() == IResource.FILE || src.getType() == IResource.FILE) return;

            IFolder dstFolder = (IFolder) dst;
            IFolder srcFolder = (IFolder) src;

            for (IResource res : srcFolder.members()) {
                if (res.getType() == IResource.FOLDER) {
                    copyContent(res, dstFolder.getFolder(res.getName()));
                } else if (res.getType() == IResource.FILE) {
                    copyContent(res, dstFolder.getFile(res.getName()));
                }
            }

        }
        try {
            src.copy(dst.getFullPath(), true, null);
        } catch (CoreException e) {
            if (e.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                IResourceStatus s = (IResourceStatus) e.getStatus();
                IResource r = src.getProject().findMember(s.getPath().removeFirstSegments(1));
                copyContent(src, r);
            }
        }
    }

    protected IResource createFolderStructure(IPath fullPath, IFolder folder, IResource res)
            throws UnsupportedEncodingException, CoreException {
        IFolder current = folder;
        for (int i = 0; i < fullPath.segmentCount() - 1; i++) {
            current = current.getFolder(fullPath.segment(i));
            if (!current.exists()) {
                try {
                    current.create(true, false, null);
                } catch (CoreException e) {
                    if (e.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                        IResourceStatus s = (IResourceStatus) e.getStatus();
                        current = (IFolder) current.getParent();
                        current = current.getFolder(s.getPath().lastSegment());
                    } else if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED) {
                        throw e;
                    }
                }
            }

        }

        if (res instanceof IContainer) {
            return current.getFolder(fullPath.lastSegment());
        } else {
            return current.getFile(fullPath.lastSegment());
        }
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        if (!(m_selection instanceof StructuredSelection)) return;

        StructuredSelection tree = (StructuredSelection) m_selection;
        Iterator<?> it = tree.iterator();
        while (it.hasNext()) {

            IResource src = ((PackedFileAdapter) it.next()).getRes();
            IFolder patches = src.getProject().getFolder("Patches");
            URI uri = src.getLocationURI();

            if (!uri.getScheme().equals("dat")) continue;

            try {
                String fragment = uri.getFragment();

                if (fragment == null) continue;

                IPath fullPath = new Path(URLDecoder.decode(fragment, Charset.defaultCharset().name()));

                IResource dst = createFolderStructure(fullPath, patches, src);
                copyContent(src, dst);

            } catch (CoreException e) {
                ErrorDialog.openError(m_shell, "Extraction error", e.getMessage(), e.getStatus());
            } catch (UnsupportedEncodingException e) {}
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        m_selection = selection;
    }
}
