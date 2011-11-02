package fdt.compilers;

import java.io.File;

public class Mark {

    public static final int ERROR   = 1;
    public static final int WARNING = 2;

    private File            m_file;
    private String          m_message;
    private int             m_severity;
    private int             m_line;

    public Mark(File file, String message, int severity, int line) {
        m_file = file;
        m_message = message;
        m_severity = severity;
        m_line = line;
    }

    public Mark(String file, String message, int severity, int line) {
        this(new File(file), message, severity, line);
    }

    public File getFile() {
        return m_file;
    }

    public File getCanonicalFile(File root) {
        try {
            if (m_file.isAbsolute()) {
                return m_file.getCanonicalFile();
            }

            return new File(root, m_file.toString()).getCanonicalFile();
        } catch (Exception e) {
            return null;
        }
    }

    public String getMessage() {
        return m_message;
    }

    public int getSeverity() {
        return m_severity;
    }

    public int getLine() {
        return m_line;
    }

}
