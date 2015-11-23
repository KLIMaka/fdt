package fdt.editors.ssl;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.jface.text.presentation.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.swt.widgets.Shell;

public class SSLConfiguration extends SourceViewerConfiguration {
	private SSLCodeScanner scanner;
	private ColorManager colorManager;

	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}

	public SSLConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, SSLPartitionScanner.SSL_COMMENT };
	}

	protected SSLCodeScanner getSSLCodeScanner() {
		if (scanner == null) {
			scanner = new SSLCodeScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(ISSLColorConstants.DEFAULT))));
		}
		return scanner;
	}

	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(ISSLColorConstants.COMMENT))));
		reconciler.setDamager(dr, SSLPartitionScanner.SSL_COMMENT);
		reconciler.setRepairer(dr, SSLPartitionScanner.SSL_COMMENT);

		dr = new DefaultDamagerRepairer(getSSLCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new SSLCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

		assistant.setInformationControlCreator(new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, false);
			}
		});

		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(100);
		assistant.enableColoredLabels(true);
		return assistant;

	}

}
