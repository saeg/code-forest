package br.usp.each.saeg.code.forest.main;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import br.usp.each.saeg.code.forest.xml.*;


public class TreeDataGenerator {

    private static final String indentSpaces = "    ";

    public static XmlInput generate(String dir) {
        XmlInput output = new XmlInput();

        Collection<File> files = FileUtils.listFiles(new File(dir), new String[] {"java"}, true);
        for (File file : files) {
            List<String> lines = read(file);
            XmlClass clazz = new XmlClass();
            findClassName(clazz, lines);
            findPackage(clazz, lines);

            List<Integer> methodLine = getMethods(lines);
            for (Integer i : methodLine) {
                clazz.getMethods().add(getMethodBody(lines, i));
            }
            XmlPackage pkg = new XmlPackage();
            pkg.getClasses().add(clazz);
            output.getPackages().add(pkg);
        }

        return output;
    }

    private static void findClassName(XmlClass arg, List<String> lines) {
        int i = 0;
        for (String line : lines) {
            i++;
            if (line.trim().startsWith("class") || line.contains(" class ")) {
                String partial = line.replaceAll("\\{", "").trim();
                arg.setName(partial.substring(partial.lastIndexOf(" ") + 1));
                arg.setLoc(i);
            }
        }
    }

    private static void findPackage(XmlClass arg, List<String> lines) {
        for (String line : lines) {
            if (StringUtils.startsWith(line, "package ")) {
                arg.setName(line.replaceFirst("package", "").replaceAll(";", "").trim() + "." + arg.getName());
            }
        }
    }

    private static List<Integer> getMethods(List<String> lines) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith(indentSpaces) && !line.startsWith(indentSpaces + indentSpaces) && line.contains("(") && line.contains(")") && lines.get(i).contains("{") && !line.contains(";") && !line.contains("=")) {
                result.add(i+1);
            }
        }
        return result;
    }

    private static XmlMethod getMethodBody(List<String> lines, int loc) {
        XmlMethod data = new XmlMethod();
        data.setLoc(loc);

        String line = "";
        int i = loc;

        do {
            i++;
            line = lines.get(i);
            if (line.startsWith(indentSpaces + "}") || line.trim().isEmpty()) {
                break;
            }

            XmlStatement stmt = new XmlStatement();
            stmt.setLoc(i);
            data.getStatements().add(stmt);

        } while (!line.startsWith(indentSpaces + "}"));
        return data;
    }

    private static List<String> read(File file) {
        try {
            List<String> lines = FileUtils.readLines(file);
            return lines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        JAXBContext context = JAXBContext.newInstance(XmlInput.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(generate("d:/Devel/bugforest/src"), System.out);
    }
}
