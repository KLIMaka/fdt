package fdt.editors.ssl;

import org.eclipse.core.resources.*;
import org.eclipse.jface.text.*;
import org.eclipse.ui.texteditor.*;

public class SSLMarkerAnnotationModel extends ResourceMarkerAnnotationModel {

	public SSLMarkerAnnotationModel(IResource resource) {
		super(resource);
	}

	protected Position correctPos(Position pos, IMarker marker) {
		if (pos.length == 0) {
			try {
				pos.length = fDocument.getLineLength(fDocument.getLineOfOffset(pos.offset));
				char c = fDocument.getChar(pos.offset);
				while (c == ' ' || c == '\t') {
					pos.offset++;
					pos.length--;
					c = fDocument.getChar(pos.offset);
				}
			} catch (BadLocationException e) {
			}
		}

		return pos;
	}

	@Override
	protected Position createPositionFromMarker(IMarker marker) {
		Position pos = super.createPositionFromMarker(marker);
		if (pos != null) {
			pos = correctPos(pos, marker);
		} else {
			int start = MarkerUtilities.getCharStart(marker);
			int end = MarkerUtilities.getCharEnd(marker);
			if (start == -1 && end == -1) {
				// marker line number is 1-based
				int line = MarkerUtilities.getLineNumber(marker);
				if (line > 0 && fDocument != null) {
					try {
						start = fDocument.getLineOffset(line - 1);
						end = start;
					} catch (BadLocationException x) {
						start = end = 1;
					}
				} else {
					start = end = 0;
				}
			}
			pos = correctPos(new Position(start, 0), marker);
		}
		return pos;
	}
}
