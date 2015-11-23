package fdt.editors.ssl;

import java.util.*;
import java.util.regex.*;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.swt.graphics.Image;

import fdt.Fdt;
import fdt.editors.ssl.SSLLib.FunctionInfo;

public class SSLCompletionProcessor implements IContentAssistProcessor {

	private SSLValidator m_validator = new SSLValidator();
	private Image m_imageFunc;
	private Image m_imageKeyWord;

	private static final String sslKeyWords[] = new String[] { "PROCEDURE", "VARIABLE", "IMPORT", "EXPORT", "IN", "WHEN", "BEGIN", "END", "CALL", "CRITICAL",
			"IF", "THEN", "ELSE", "WHILE", "DO", "AND", "OR", "BWAND", "BWOR", "BWXOR", "FLOOR", "NOT", "BWNOT", "RETURN" };

	public SSLCompletionProcessor() {
		m_imageFunc = Fdt.getImageDescriptor("icons/methpub_obj.gif").createImage();
		m_imageKeyWord = Fdt.getImageDescriptor("icons/methpro_obj.gif").createImage();
	}

	protected String getWord(IDocument doc, int offset) {
		StringBuilder s = new StringBuilder();
		offset--;
		try {
			while (Character.isJavaIdentifierPart(doc.getChar(offset))) {
				s.insert(0, doc.getChar(offset));
				offset--;
			}
		} catch (BadLocationException e) {
		}
		return s.toString();
	}

	protected void findKeyWords(List<ICompletionProposal> proposals, String word, int documentOffset) {
		Pattern p = Pattern.compile("^" + word + ".*", Pattern.CASE_INSENSITIVE);
		for (String keyWord : sslKeyWords) {
			keyWord = keyWord.toLowerCase();
			Matcher m = p.matcher(keyWord);
			if (m.find()) {
				String replacementString = keyWord;
				int replacementOffset = documentOffset - word.length();
				int replacementLength = word.length();
				int cursorPosition = keyWord.length();
				Image image = m_imageKeyWord;
				String displayString = keyWord;

				proposals.add(new CompletionProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, null, null));
			}
		}
	}

	protected void findFunctions(List<ICompletionProposal> proposals, String word, int documentOffset) {
		Map<String, FunctionInfo> funcs = SSLLib.getInstance().getFunctions();

		Pattern p = Pattern.compile("^" + word + ".*", Pattern.CASE_INSENSITIVE);
		for (String functionName : funcs.keySet()) {
			Matcher m = p.matcher(functionName);
			if (m.find()) {

				String parenthesis = "";
				String args = "";
				IContextInformation info = null;

				if (funcs.get(functionName).args != null) {
					Image image1 = m_imageFunc;
					String contextDisplayString = functionName;
					String informationDisplayString = funcs.get(functionName).args;

					info = new ContextInformation(image1, contextDisplayString, informationDisplayString);
					parenthesis = "(";
					args = funcs.get(functionName).args;
				}

				String replacementString = functionName + parenthesis;
				int replacementOffset = documentOffset - word.length();
				int replacementLength = word.length();
				int cursorPosition = functionName.length() + parenthesis.length();
				Image image = m_imageFunc;
				String displayString = functionName + args;
				String additionalProposalInfo = funcs.get(functionName).help;

				proposals.add(new CompletionProposal(replacementString, replacementOffset, replacementLength, cursorPosition, image, displayString, info,
						additionalProposalInfo));
			}
		}

	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {

		String word = getWord(viewer.getDocument(), documentOffset);

		ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();

		findKeyWords(proposals, word, documentOffset);
		findFunctions(proposals, word, documentOffset);

		ICompletionProposal result[] = new CompletionProposal[proposals.size()];
		proposals.toArray(result);
		return result;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return m_validator;
	}

}
