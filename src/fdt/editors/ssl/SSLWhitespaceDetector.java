package fdt.editors.ssl;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class SSLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
