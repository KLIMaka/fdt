package ssl.editors.ssl;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class MyCompletionProposal implements ICompletionProposal, ICompletionProposalExtension6,
        ICompletionProposalExtension5 {

    /** The string to be displayed in the completion proposal popup. */
    private String              fDisplayString;
    /** The replacement string. */
    private String              fReplacementString;
    /** The replacement offset. */
    private int                 fReplacementOffset;
    /** The replacement length. */
    private int                 fReplacementLength;
    /** The cursor position after this proposal has been applied. */
    private int                 fCursorPosition;
    /** The image to be displayed in the completion proposal popup. */
    private Image               fImage;
    /** The context information of this proposal. */
    private IContextInformation fContextInformation;
    /** The additional info of this proposal. */
    private String              fAdditionalProposalInfo;

    public MyCompletionProposal(String replacementString, int replacementOffset, int replacementLength,
            int cursorPosition, Image image, String displayString, IContextInformation contextInformation,
            String additionalProposalInfo) {
        Assert.isNotNull(replacementString);
        Assert.isTrue(replacementOffset >= 0);
        Assert.isTrue(replacementLength >= 0);
        Assert.isTrue(cursorPosition >= 0);

        fReplacementString = replacementString;
        fReplacementOffset = replacementOffset;
        fReplacementLength = replacementLength;
        fCursorPosition = cursorPosition;
        fImage = image;
        fDisplayString = displayString;
        fContextInformation = contextInformation;
        fAdditionalProposalInfo = additionalProposalInfo;
    }

    /*
     * @see ICompletionProposal#apply(IDocument)
     */
    public void apply(IDocument document) {
        try {
            document.replace(fReplacementOffset, fReplacementLength, fReplacementString);
        } catch (BadLocationException x) {
            // ignore
        }
    }

    /*
     * @see ICompletionProposal#getSelection(IDocument)
     */
    public Point getSelection(IDocument document) {
        return new Point(fReplacementOffset + fCursorPosition, 0);
    }

    /*
     * @see ICompletionProposal#getContextInformation()
     */
    public IContextInformation getContextInformation() {
        return fContextInformation;
    }

    /*
     * @see ICompletionProposal#getImage()
     */
    public Image getImage() {
        return fImage;
    }

    /*
     * @see ICompletionProposal#getDisplayString()
     */
    public String getDisplayString() {
        if (fDisplayString != null) return fDisplayString;
        return fReplacementString;
    }

    /*
     * @see ICompletionProposal#getAdditionalProposalInfo()
     */
    public String getAdditionalProposalInfo() {
        return fAdditionalProposalInfo;
    }

    @Override
    public StyledString getStyledDisplayString() {
        StyledString string = new StyledString();
        string.append(fDisplayString.toCharArray(), new Styler() {

            @Override
            public void applyStyles(TextStyle textStyle) {
                textStyle.foreground = new Color(Display.getCurrent(), new RGB(255, 100, 100));
                textStyle.underline = true;
            }
        });

        string.append(fAdditionalProposalInfo.toCharArray(), new Styler() {

            @Override
            public void applyStyles(TextStyle textStyle) {
                textStyle.foreground = new Color(Display.getCurrent(), new RGB(100, 100, 100));
            }
        });

        return string;
    }

    @Override
    public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
        return fAdditionalProposalInfo;
    }
}
