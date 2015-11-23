package fdt.compilers;

import static fdt.SslSettingsContext.getSslSetting;
import static fdt.preferences.PreferenceConstants.P_PREPROCESSOR;
import static org.eclipse.core.resources.IMarker.*;

import java.io.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.*;

import org.eclipse.core.resources.IFile;

import fdt.*;
import fdt.preferences.PreferenceConstants;

public class Preprocessor {

	private static final Pattern MESSAGE = Pattern.compile("(.*):(\\d*): (.*): (.*)");

	IFile file;
	InputStream stream;
	File pp;

	public Preprocessor(IFile file) {
		this.file = file;
		String preprocessor = getSslSetting(file.getProject(), P_PREPROCESSOR);
		pp = new File(preprocessor);
		if (!pp.isFile())
			throw new IllegalStateException("Invalid preprocessor <" + preprocessor + ">");
	}

	public void clean() {
		try {
			stream.close();
			getPreprocessFile().delete();
		} catch (IOException e) {
			Fdt.getDefault().handleException(e);
		}
	}

	protected File getPreprocessFile() {
		String fileName = file.getLocation().lastSegment();
		File workDir = file.getLocation().removeLastSegments(1).toFile();
		return new File(workDir, fileName + "~");
	}

	protected int collectMarks(Process proc, List<SslProblemMark> marks) throws IOException {

		BufferedReader rpp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		try {
			Thread.sleep(100); // XXX hack for mcpp
		} catch (Exception e) {
		}

		while (rpp.ready() && rpp.readLine() != null) {
		}

		rpp = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		String line;
		while (rpp.ready() && (line = rpp.readLine()) != null) {
			Matcher m = MESSAGE.matcher(line);
			if (m.find()) {
				String fileName = m.group(1);
				String lineNum = m.group(2);
				String type = m.group(3);
				String meassage = m.group(4);

				if (type.equals("warning")) {
					marks.add(new SslProblemMark(fileName, meassage, SEVERITY_WARNING, Integer.parseInt(lineNum)));
				} else if (type.equals("error")) {
					marks.add(new SslProblemMark(fileName, meassage, SEVERITY_ERROR, Integer.parseInt(lineNum)));
				}
			}
		}

		return 0;
	}

	public InputStream preprocess(List<SslProblemMark> marks) throws IOException, InterruptedException {
		File ppFile = getPreprocessFile();
		String fileName = file.getLocation().lastSegment();
		SslSettingsContext ctx = new SslSettingsContext(file.getProject());
		String additions = ctx.get(PreferenceConstants.P_PP_OPTIONS);

		String ppCmd = MessageFormat.format("{0} {1} \"{2}\" \"{2}~\"", pp.toString(), additions, fileName);
		Process pp = Runtime.getRuntime().exec(ppCmd, null, ppFile.getParentFile());

		collectMarks(pp, marks);

		pp.waitFor();

		stream = new FileInputStream(ppFile);
		return stream;
	}

}
