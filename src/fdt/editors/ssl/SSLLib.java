package fdt.editors.ssl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSLLib {
    private static class Holder {
        private final static SSLLib m_instance = new SSLLib();
    }

    public static SSLLib getInstance() {
        return Holder.m_instance;
    }

    class FunctionInfo {
        public String name;
        public String args;
        public String help = null;

        public FunctionInfo(String name, String args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public String toString() {
            return name + " -> " + args + " " + help;
        }
    }

    private Map<String, FunctionInfo> m_functions = new TreeMap<String, FunctionInfo>(String.CASE_INSENSITIVE_ORDER);

    private SSLLib() {
        readList();
        readInfos();
    }

    private void readInfos() {
        try {
            InputStream listStream = getClass().getResourceAsStream("info.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(listStream));

            String line;

            while ((line = r.readLine()) != null) {
                String[] info = line.split("\\|");
                FunctionInfo fi = m_functions.get(info[0]);
                if (fi != null) {
                    m_functions.get(info[0]).help = info[1];
                }
            }
        } catch (Exception e) {}
    }

    private void readList() {
        try {
            InputStream listStream = getClass().getResourceAsStream("list.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(listStream));

            Pattern p = Pattern.compile("([^\\( ;]*)(\\((.*)\\))?;?");
            String line;
            while ((line = r.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    m_functions.put(m.group(1), new FunctionInfo(m.group(1), m.group(2)));
                } else {
                    System.out.println(line);
                }
            }

            r.close();
            listStream.close();
        } catch (Exception e) {}

    }

    public Map<String, FunctionInfo> getFunctions() {
        return m_functions;
    }
}
