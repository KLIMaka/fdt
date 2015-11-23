package fdt.compilers;

import static org.eclipse.core.resources.IMarker.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.MessageConsoleStream;

import fdk.util.Utils;

public class BISCompiler extends SignedCompiler {

	@Override
	public String getName() {
		return "BIS Compiler";
	}

	@Override
	protected boolean checkSignature(BufferedReader r) throws IOException {
		String sig = r.readLine();
		if (sig.equals("Startreck scripting language compiler (Fallout 2 edition)")) {
			return true;
		}
		return false;
	}

	protected int collectMarks(Process proc, List<SslProblemMark> marks, MessageConsoleStream msg, File srcFile) throws NumberFormatException, IOException {
		// collect info about errors an warnings
		BufferedReader r = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		String line;
		int errors = 0;
		Pattern p = Pattern.compile("(.*)\\((\\d*)\\): Error! (.*)");
		while ((line = r.readLine()) != null) {
			if (msg != null) {
				msg.println(line);
			}

			Matcher m = p.matcher(line);
			if (m.find() == true) {
				String file = m.group(1);
				String lineN = m.group(2);
				String message = m.group(3);

				marks.add(new SslProblemMark(file, message, SEVERITY_ERROR, Integer.parseInt(lineN)));
				errors++;
			} else if (line.matches(".*Warning.*")) {
				marks.add(new SslProblemMark(srcFile, line, SEVERITY_WARNING, -1));
			}

		}
		return errors;
	}

	public Collection<SslProblemMark> compile(File compPath, IFile file, File outFile, MessageConsoleStream msg) {
		List<SslProblemMark> marks = new ArrayList<SslProblemMark>();
		try {
			// copy preprocessed file into compiler's working dir
			String fileName = file.getLocation().lastSegment();
			File tmpSsl = new File(compPath.getParentFile(), fileName);
			FileOutputStream tmpSSLStr = new FileOutputStream(tmpSsl);
			Preprocessor pp = new Preprocessor(file);
			InputStream preprocessed = pp.preprocess(marks); // preprocess
			Utils.copyStrream(preprocessed, tmpSSLStr);
			tmpSSLStr.close();

			// compile
			Process proc = Runtime.getRuntime().exec(compPath.toString() + " \"" + fileName + "\"", null, compPath.getParentFile());

			int errors = collectMarks(proc, marks, msg, file.getFullPath().toFile());

			// copy compiled file in destination directory if build is
			// successful
			if (errors == 0) {
				// TODO может заменится не только расширение
				File tmpInt = new File(tmpSsl.getParentFile(), fileName.replaceAll("\\.[Ss][Ss][Ll]$", ".int"));
				FileInputStream inInt = new FileInputStream(tmpInt);
				FileOutputStream outInt = new FileOutputStream(outFile);
				Utils.copyStrream(inInt, outInt);
				outInt.close();
				inInt.close();
				tmpInt.delete();
			}

			// clean temporary files
			tmpSsl.delete();
			pp.clean();
		} catch (Exception e) {
			marks.add(new SslProblemMark(file.getLocation().toString(), e.getMessage(), SEVERITY_ERROR, -1));
		}

		return marks;
	}
}
