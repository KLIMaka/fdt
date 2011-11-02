package fdt.editors.ssl;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class SSLEditor extends TextEditor {

    private ColorManager colorManager;

    public SSLEditor() {
        super();
        colorManager = new ColorManager();
        setDocumentProvider(new SSLDocumentProvider());
        setSourceViewerConfiguration(new SSLConfiguration(colorManager));
    }

    public final static String EDITOR_MATCHING_BRACKETS       = "matchingBrackets";
    public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

    @Override
    protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
        super.configureSourceViewerDecorationSupport(support);

        char[] matchChars = { '(', ')', '[', ']' }; // which brackets to match
        ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(matchChars,
                IDocumentExtension3.DEFAULT_PARTITIONING);
        support.setCharacterPairMatcher(matcher);
        support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);

        // Enable bracket highlighting in the preference store
        IPreferenceStore store = getPreferenceStore();
        store.setDefault(EDITOR_MATCHING_BRACKETS, true);
        store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");
    }

    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
}
