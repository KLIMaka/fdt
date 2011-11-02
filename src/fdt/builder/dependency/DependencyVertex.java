package fdt.builder.dependency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;

public class DependencyVertex implements Comparable<DependencyVertex> {
    File                  m_file;
    DependencyGraph       m_graph;
    Set<DependencyVertex> m_parents = new TreeSet<DependencyVertex>();
    Set<DependencyVertex> m_childs  = new TreeSet<DependencyVertex>();

    public DependencyVertex(File file, DependencyGraph graph) throws IOException {
        Assert.isTrue(file.isAbsolute());
        m_file = file;
        m_graph = graph;
        readParents();
    }

    protected void readParents() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(m_file)));
        Pattern p = Pattern.compile("^([^/])*#include (\\\"|<)(.*)(\\\"|>)");
        String line;

        while ((line = r.readLine()) != null) {
            Matcher m = p.matcher(line);
            if (m.find() == true) {
                File file = new File(m.group(3));
                if (!file.isAbsolute()) {
                    file = new File(m_file.getParentFile(), file.toString());
                }

                DependencyVertex vtx = m_graph.getVertex(file);
                if (vtx != null) {
                    connect(m_graph.getVertex(file));
                }
            }

        }
    }

    public void addParent(DependencyVertex vtx) {
        m_parents.add(vtx);
    }

    public void addChild(DependencyVertex vtx) {
        m_childs.add(vtx);
    }

    public void connect(DependencyVertex vtx) {
        vtx.addChild(this);
        addParent(vtx);
    }

    @Override
    public int compareTo(DependencyVertex o) {
        return m_file.compareTo(o.m_file);
    }

    public File getFile() {
        return m_file;
    }

    public Set<DependencyVertex> getParents() {
        return m_parents;
    }

    public Set<DependencyVertex> getChilds() {
        return m_childs;
    }

}
