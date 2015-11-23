package fdt.editors.rix;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

import fdk.rix.RIXImage;
import fdt.util.FID;

public class RIXViewer extends EditorPart {

	public static final String ID = "ssl.editors.ssl.rix.RIXViewer"; //$NON-NLS-1$
	private Text m_text;
	private Image m_img = null;

	public RIXViewer() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);

		Canvas canvas = new Canvas(container, SWT.NONE);

		try {
			InputStream rixStream = ((FileEditorInput) getEditorInput()).getFile().getContents();
			RIXImage rixImg = new RIXImage(rixStream);
			rixStream.close();
			m_img = FID.getImage(rixImg.getWidth(), rixImg.getHeight(), rixImg.getData(), rixImg.getPallete());
			canvas.setBackgroundImage(m_img);
			canvas.setSize(rixImg.getWidth(), rixImg.getHeight());

			m_text = new Text(container, SWT.BORDER);
			m_text.setBounds(0, 0, 76, 21);
			setPartName(((FileEditorInput) getEditorInput()).getFile().getName());

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose() {
		super.dispose();
		if (m_img != null)
			m_img.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public void setFocus() {
		m_text.setFocus();
	}
}
