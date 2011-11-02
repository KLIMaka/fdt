package fdt.compilers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.osgi.service.prefs.BackingStoreException;

import fdt.ContextSettings;
import fdt.preferences.PreferenceConstants;

public class Preprocessor {

    IFile       m_file;
    InputStream m_stream;
    File        m_pp;

    public Preprocessor(IFile file) throws BackingStoreException {
        m_file = file;

        ContextSettings ctx = new ContextSettings(m_file.getProject());
        m_pp = new File(ctx.getString(PreferenceConstants.P_PREPROCESSOR));
        ctx.flush();
    }

    public void clean() {
        try {
            m_stream.close();
            getPreprocessFile().delete();
        } catch (IOException e) {}
    }

    protected File getPreprocessFile() {
        String fileName = m_file.getLocation().lastSegment();
        File workDir = m_file.getLocation().removeLastSegments(1).toFile();
        return new File(workDir, fileName + "~");
    }

    protected int collectMarks(Process proc, List<Mark> marks) throws IOException {
        Pattern p = Pattern.compile("(.*):(\\d*): (.*): (.*)");

        BufferedReader rpp = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
            Thread.sleep(100); // XXX hack for mcpp
        } catch (Exception e) {}

        while (rpp.ready() && rpp.readLine() != null) {}

        rpp = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String line;
        while (rpp.ready() && (line = rpp.readLine()) != null) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                String fileName = m.group(1);
                String lineNum = m.group(2);
                String type = m.group(3);
                String meassage = m.group(4);

                if (type.equals("warning")) {
                    marks.add(new Mark(fileName, meassage, Mark.WARNING, Integer.parseInt(lineNum)));
                } else if (type.equals("error")) {
                    marks.add(new Mark(fileName, meassage, Mark.ERROR, Integer.parseInt(lineNum)));
                }
            }
        }

        return 0;
    }

    public InputStream preprocess(List<Mark> marks) throws IOException, InterruptedException {
        File ppFile = getPreprocessFile();
        String fileName = m_file.getLocation().lastSegment();
        ContextSettings ctx = new ContextSettings(m_file.getProject());
        String additions = ctx.getString(PreferenceConstants.P_PP_OPTIONS);

        String ppCmd = MessageFormat.format("{0} {1} \"{2}\" \"{2}~\"", m_pp.toString(), additions, fileName);
        Process pp = Runtime.getRuntime().exec(ppCmd, null, ppFile.getParentFile());

        collectMarks(pp, marks);

        pp.waitFor();

        m_stream = new FileInputStream(ppFile);
        return m_stream;
    }

}
