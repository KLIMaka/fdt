package ssl.editors.ssl;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class SSLCodeScanner extends RuleBasedScanner {

    static class JavaWordDetector implements IWordDetector {
        public boolean isWordPart(char character) {
            return Character.isJavaIdentifierPart(character);
        }

        public boolean isWordStart(char character) {
            return Character.isJavaIdentifierStart(character) || character == '#';
        }
    }

    private static final String sslKeyWords[] = new String[] { "PROCEDURE", "VARIABLE", "IMPORT", "EXPORT", "IN",
            "WHEN", "BEGIN", "END", "CALL", "CRITICAL", "IF", "THEN", "ELSE", "WHILE", "DO", "AND", "OR", "BWAND",
            "BWOR", "BWXOR", "FLOOR", "NOT", "BWNOT", "RETURN" };
    private static final String sslPP[]       = new String[] { "#include", "#define", "#ifdef", "#ifndef", "#endif",
            "#undef"                         };

    public SSLCodeScanner(ColorManager manager) {
        IToken comment = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.COMMENT)));
        IToken string = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.STRING)));
        IToken keyword = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.KEYWORD), null, SWT.BOLD));
        IToken pp = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.PP), null, SWT.BOLD));
        IToken function = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.FUNCTION)));
        IToken other = new Token(new TextAttribute(manager.getColor(ISSLColorConstants.DEFAULT)));

        ArrayList<IRule> rules = new ArrayList<IRule>();

        rules.add(new EndOfLineRule("//", comment));

        rules.add(new MultiLineRule("\"", "\"", string, '\\', false));
        rules.add(new SingleLineRule("'", "'", string, '\\'));

        rules.add(new WhitespaceRule(new SSLWhitespaceDetector()));

        WordRule keywords = new WordRule(new JavaWordDetector(), other, true);
        for (String word : sslKeyWords) {
            keywords.addWord(word, keyword);
        }
        for (String word : sslPP) {
            keywords.addWord(word, pp);
        }
        for (String foo : SSLLib.getInstance().getFunctions().keySet()) {
            keywords.addWord(foo, function);
        }

        rules.add(keywords);

        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }
}
