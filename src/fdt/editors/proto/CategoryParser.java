package fdt.editors.proto;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CategoryParser extends DefaultHandler {

    public static class Category {
        private String   m_name;
        private Object[] m_members;

        public Category(String name, Object[] members) {
            m_name = name;
            m_members = members;
        }

        public String getName() {
            return m_name;
        }

        public Object[] getMembers() {
            return m_members;
        }
    }

    public static class Field {
        private int m_name;
        private int m_descr;

        public Field(int name, int descr) {
            m_name = name;
            m_descr = descr;
        }

        public int getName() {
            return m_name;
        }

        public int getDescr() {
            return m_descr;
        }

    }

    private Stack<ArrayList<Object>> m_stack      = new Stack<ArrayList<Object>>();
    private Stack<String>            m_name_stack = new Stack<String>();

    public CategoryParser() {
        m_stack.add(new ArrayList<Object>());
    }

    public static Map<String, Category> getCategory(InputStream is) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            CategoryParser handler = new CategoryParser();
            parser.parse(is, handler);
            return handler.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startElement(String uri, String local_name, String raw_name, Attributes amap) throws SAXException {
        if (raw_name == "category") {
            m_stack.add(new ArrayList<Object>());
            m_name_stack.add(amap.getValue("name"));
        }
        if (raw_name == "field") {
            m_stack.lastElement().add(
                    new Field(Integer.parseInt(amap.getValue("name")), Integer.parseInt(amap.getValue("id"))));
        }
    }

    public void endElement(String uri, String local_name, String raw_name) throws SAXException {
        if (raw_name == "category") {
            Category cat = new Category(m_name_stack.lastElement(), m_stack.lastElement().toArray());
            m_name_stack.pop();
            m_stack.pop();
            m_stack.lastElement().add(cat);
        }
    }

    public Map<String, Category> getResult() {
        Map<String, Category> map = new TreeMap<String, Category>();
        for (Object o : m_stack.get(0)) {
            Category cat = (Category) o;
            map.put(cat.getName(), cat);
        }
        return map;
    }
}
