package fdt.editors.ssl;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

public class SSLValidator implements IContextInformationValidator, IContextInformationPresenter {

	protected int m_installOffset;
	protected String m_info;
	protected ITextViewer m_viewer;
	protected int m_index;

	public boolean isContextInformationValid(int offset) {
		try {
			IDocument doc = m_viewer.getDocument();
			String input = doc.get(m_installOffset, offset - m_installOffset);

			if (input.contains(")"))
				return false;

			m_index = (input + " ").split(",").length - 1;

			return true;
		} catch (BadLocationException e) {
			return false;
		}
	}

	public void install(IContextInformation info, ITextViewer viewer, int offset) {
		m_installOffset = offset;
		m_info = info.getInformationDisplayString();
		m_viewer = viewer;
	}

	public boolean updatePresentation(int documentPosition, TextPresentation presentation) {
		presentation.clear();
		String[] parts = m_info.split(",");

		if (m_index < parts.length) {
			int start = m_info.indexOf(parts[m_index]);
			int length = parts[m_index].length();

			if (m_index == 0)
				start++;
			if (m_index == parts.length - 1)
				length--;

			presentation.addStyleRange(new StyleRange(start, length, null, null, SWT.BOLD));
		}

		return true;
	}
}
