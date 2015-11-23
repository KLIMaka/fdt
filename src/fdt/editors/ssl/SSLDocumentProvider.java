package fdt.editors.ssl;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class SSLDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new SSLPartitionScanner(), new String[] { SSLPartitionScanner.SSL_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		// TODO Auto-generated method stub
		super.doSaveDocument(monitor, element, document, overwrite);
	}

	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) element;
			return new SSLMarkerAnnotationModel(input.getFile());
		}
		return super.createAnnotationModel(element);
	}
}
