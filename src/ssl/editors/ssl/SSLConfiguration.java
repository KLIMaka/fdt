package ssl.editors.ssl;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.widgets.Shell;

public class SSLConfiguration extends SourceViewerConfiguration {
    private SSLCodeScanner scanner;
    private ColorManager   colorManager;

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
            scanner.setDefaultReturnToken(new Token(
                    new TextAttribute(colorManager.getColor(ISSLColorConstants.DEFAULT))));
        }
        return scanner;
    }

    @Override
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return new DefaultAnnotationHover();
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(
                colorManager.getColor(ISSLColorConstants.COMMENT))));
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
