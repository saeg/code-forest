package br.usp.each.saeg.code.forest.main;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.xml.*;

public class TreeDataBuilder {

    private Map<String, TreeData> classTreeData = new LinkedHashMap<String, TreeData>();
    private List<TreeData> treeData = new ArrayList<TreeData>();
    private List<XmlPackage> packages = new ArrayList<XmlPackage>();
    private File srcRootDir;

    public TreeDataBuilder(File srcRootDir, File xmlRootDir) {
        this.srcRootDir = srcRootDir;
        buildSourceMap(srcRootDir);
        buildXmlMap(xmlRootDir);
        merge();
    }

    private void buildSourceMap(File dir) {
        Collection<File> srcFiles = FileUtils.listFiles(dir, new String[] {"java"}, true);
        for (File file : srcFiles) {
            TreeData data = new TreeData();
            data.setLines(read(file));
            classTreeData.put(StringUtils.substringBeforeLast(classNameFrom(file), ".java"), data);
        }
    }

    private void buildXmlMap(File dir) {
        Collection<File> xmlFiles = FileUtils.listFiles(dir, new String[] {"xml"}, false);
        for (File file : xmlFiles) {
            XmlInput input = XmlInput.unmarshal(file);
            packages.addAll(input.getPackages());
        }
    }

    private String classNameFrom(File arg) {
        List<String> partial = new ArrayList<String>();
        File current = arg;
        while (current != null) {
            partial.add(0, current.getName());
            current = current.getParentFile();
            if (srcRootDir.equals(current)) {
                break;
            }
        }
        return StringUtils.join(partial, ".");
    }

    private void merge() {
        for (XmlPackage pkg : packages) {
            for (XmlClass clazz : pkg.getClasses()) {
                String key;
                if (clazz.getName().contains("$")) {
                    key = StringUtils.substringBefore(clazz.getName(), "$");
                } else {
                    key = clazz.getName();
                }
                if (!classTreeData.containsKey(key)) {
                    System.out.println(key + " not found " + classTreeData.keySet());
                    continue;
                }
                TreeData tree = classTreeData.get(key);
                tree.setLoc(clazz.getLoc()-1);
                tree.setScore(clazz.getScore().floatValue());
                tree.setValue(clazz.getName().trim());

                for (XmlMethod method : clazz.getMethods()) {
                    BranchData branch = new BranchData();
                    branch.setLoc(method.getLoc()-1);
                    branch.setScore(method.getScore().floatValue());
                    branch.setValue(tree.getLines().get(method.getLoc()-1).trim());

                    tree.getBranchData().add(branch);
                    for (XmlStatement stmt : method.getStatements()) {
                        LeafData leaf = new LeafData();
                        leaf.setLoc(stmt.getLoc()-1);
                        leaf.setScore(stmt.getScore().floatValue());
                        leaf.setValue(tree.getLines().get(stmt.getLoc()-1));
                        branch.getLeafData().add(leaf);
                    }
                }
                treeData.add(tree);
            }
        }
    }

    private static List<String> read(File file) {
        try {
            return FileUtils.readLines(file);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TreeData> getTreeData() {
        return treeData;
    }
}
