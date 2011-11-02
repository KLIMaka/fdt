package fdt.builder.dependency;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class DependencyGraph {
    Map<File, DependencyVertex> m_vertexes = new TreeMap<File, DependencyVertex>();

    public DependencyVertex getVertex(File file) throws IOException {
        file = file.getCanonicalFile();
        DependencyVertex vtx = m_vertexes.get(file);
        if (vtx == null) {
            try {
                vtx = new DependencyVertex(file, this);
                m_vertexes.put(file, vtx);
            } catch (Exception e) {}
        }
        return vtx;
    }

    public void buildGraph(File workDir) throws IOException {
        for (File file : workDir.listFiles()) {
            if (file.isDirectory()) {
                buildGraph(file);
                continue;
            }
            getVertex(file);
        }
    }
}
