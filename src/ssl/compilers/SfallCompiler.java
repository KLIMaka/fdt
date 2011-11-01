package ssl.compilers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.console.MessageConsoleStream;

import ssl.ContextSettings;
import ssl.preferences.PreferenceConstants;
import fdk.util.Utils;

public class SfallCompiler extends SignedCompiler {

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

    protected int collectMarks(Process proc, List<Mark> marks, MessageConsoleStream msg) throws NumberFormatException,
            IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        int errors = 0;
        String line;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}

        while ((line = r.readLine()) != null) {
            if (msg != null) {
                msg.println(line);
            }

            Pattern p = Pattern.compile("\\[(.*)\\] <(.*)>:(-?\\d*): (.*)");
            Matcher m = p.matcher(line);
            if (m.find() == true) {
                String type = m.group(1);
                String file = m.group(2);
                String lineN = m.group(3);
                String message = m.group(4);

                if (type.equals("Warning")) {
                    marks.add(new Mark(file, message, Mark.WARNING, Integer.parseInt(lineN)));
                }
                if (type.equals("Error")) {
                    errors++;
                    marks.add(new Mark(file, message, Mark.ERROR, Integer.parseInt(lineN)));
                }
            }
        }
        return errors;
    }

    public Mark[] compile(File compPath, IFile file, File outFile, MessageConsoleStream msg) throws Exception {
        ArrayList<Mark> marks = new ArrayList<Mark>();
        try {

            File workDir = file.getLocation().removeLastSegments(1).toFile();
            String fileName = file.getLocation().lastSegment();

            Preprocessor pp = new Preprocessor(file);
            InputStream is = pp.preprocess(marks);
            File ppFile = new File(workDir, fileName + "p");
            FileOutputStream preprocessed = new FileOutputStream(ppFile);
            Utils.copyStrream(is, preprocessed);
            preprocessed.close();

            ContextSettings ctx = new ContextSettings(file.getProject());
            String additions = ctx.getString(PreferenceConstants.P_COMPILER_OPTIONS);
            additions = additions == null ? "" : additions;

            Process proc = Runtime.getRuntime().exec(
                    compPath.toString() + " " + additions + " \"" + fileName + "p\"" + " -o " + "\"" + outFile + "\"",
                    null, workDir);

            collectMarks(proc, marks, msg);

            pp.clean();
            ppFile.delete();
        }

        catch (Exception e) {
            marks.add(new Mark(file.getLocation().toString(), e.getMessage(), Mark.ERROR, -1));
        }

        Mark ret[] = new Mark[marks.size()];
        marks.toArray(ret);
        return ret;
    }
}
