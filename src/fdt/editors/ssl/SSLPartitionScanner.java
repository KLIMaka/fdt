package fdt.editors.ssl;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class SSLPartitionScanner extends RuleBasedPartitionScanner {
    public final static String SSL_COMMENT = "__ssl_comment";

    public SSLPartitionScanner() {
        IToken sslComment = new Token(SSL_COMMENT);

        ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();

        rules.add(new MultiLineRule("/*", "*/", sslComment, (char) 0, true));

        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
}
