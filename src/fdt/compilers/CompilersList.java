package fdt.compilers;

import java.util.Map;
import java.util.TreeMap;

public class CompilersList {
    private Map<String, ISSLCompiler> m_compilers = new TreeMap<String, ISSLCompiler>();

    private static class Holder {
        private final static CompilersList m_instance = new CompilersList();
    }

    private CompilersList() {
        addCompiler(new BISCompiler());
        addCompiler(new SfallCompiler());
    }

    public static CompilersList getInstance() {
        return Holder.m_instance;
    }

    public String[] getSupportedCompilers() {
        String ret[] = new String[m_compilers.size()];
        m_compilers.keySet().toArray(ret);
        return ret;
    }

    public ISSLCompiler getCompiler(String comp) {
        return m_compilers.get(comp);
    }

    public void addCompiler(ISSLCompiler comp) {
        m_compilers.put(comp.getName(), comp);
    }

    public String getCompilerByPath(String path) {
        for (String comp : getSupportedCompilers()) {
            if (getCompiler(comp).check(path) == true) {
                return comp;
            }
        }
        return null;
    }
}
