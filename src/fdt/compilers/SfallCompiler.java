package fdt.compilers;

import static fdt.preferences.PreferenceConstants.P_COMPILER_OPTIONS;
import static org.eclipse.core.resources.IMarker.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.MessageConsoleStream;

import fdk.util.Utils;
import fdt.SslSettingsContext;

public class SfallCompiler extends SignedCompiler {
	public static final Pattern MESSAGE = Pattern.compile("\\[(.*)\\] <(.*)>:(-?\\d*):\\d*: (.*)");

	@Override
	public String getName() {
		return "Sfall Compiler";
	}

	protected boolean checkSignature(java.io.BufferedReader r) throws IOException {
		String sig = r.readLine();
		if (sig.startsWith("Startreck scripting language compiler (Fallout 2 sfall edition")) {
			return true;
		}
		return false;
	};

	protected int collectMarks(Process proc, List<SslProblemMark> marks, MessageConsoleStream msg, File workingDir) throws NumberFormatException, IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		int errors = 0;
		String line;

		while ((line = r.readLine()) != null) {
			if (msg != null) {
				msg.println(line);
			}

			Matcher m = MESSAGE.matcher(line);
			if (m.find() == true) {
				String type = m.group(1);
				String file = getFileName(m.group(2), workingDir);
				String lineN = m.group(3);
				String message = m.group(4);

				if (type.equals("Warning")) {
					marks.add(new SslProblemMark(file, message, SEVERITY_WARNING, Integer.parseInt(lineN)));
				}
				if (type.equals("Error")) {
					errors++;
					marks.add(new SslProblemMark(file, message, SEVERITY_ERROR, Integer.parseInt(lineN)));
				}
			}
		}
		return errors;
	}

	private String getFileName(String fileName, File workingDir) {
		File file = new File(fileName);
		if (file.isAbsolute())
			return fileName;
		return new File(workingDir, fileName).getAbsolutePath();
	}

	public Collection<SslProblemMark> compile(File compPath, IFile file, File outFile, MessageConsoleStream msg) throws Exception {
		ArrayList<SslProblemMark> marks = new ArrayList<SslProblemMark>();
		try {

			File workDir = file.getLocation().removeLastSegments(1).toFile();
			String fileName = file.getLocation().lastSegment();
			String ppFileName = fileName + "p";

			Preprocessor pp = new Preprocessor(file);
			InputStream is = pp.preprocess(marks);
			File ppFile = new File(workDir, ppFileName);
			FileOutputStream preprocessed = new FileOutputStream(ppFile);
			Utils.copyStrream(is, preprocessed);
			preprocessed.close();

			String additions = SslSettingsContext.getSslSetting(file.getProject(), P_COMPILER_OPTIONS);
			additions = additions == null ? "" : additions;

			String compiler = compPath.getAbsolutePath();
			String out = outFile.getAbsolutePath();
			ProcessBuilder builder = new ProcessBuilder(compiler, additions, ppFileName, "-o", out).directory(workDir);
			Process proc = builder.start();

			collectMarks(proc, marks, msg, workDir);

			pp.clean();
			ppFile.delete();
		}

		catch (Exception e) {
			marks.add(new SslProblemMark(file.getLocation().toString(), e.getMessage(), SEVERITY_ERROR, -1));
		}

		return marks;
	}
}
