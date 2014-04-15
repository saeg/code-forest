package br.usp.each.saeg.code.forest.source.parser;

import java.util.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ParsingResult {

    private final IResource file;
    private XmlPackage xmlPackage;
    private boolean declaredPackage = false;
    private Map<String, XmlClass> nameXmlClass = new HashMap<String, XmlClass>();

    public ParsingResult(IResource file) {
        this.file = file;
    }

    public IResource getFile() {
        return file;
    }

    public XmlPackage getXmlPackage() {
        return xmlPackage;
    }
    public void setXmlPackage(XmlPackage xmlPackage) {
        if (isXmlPackageNull()) {
            this.xmlPackage = xmlPackage;
        }
    }
    public boolean isXmlPackageNull() {
        return xmlPackage == null;
    }

    public void addClass(String currentClass, int start, int end, int close, XmlClass xmlClass) {
        XmlClass foundXmlClass = new XmlClass();
        foundXmlClass.setStart(start);
        foundXmlClass.setEnd(end);
        foundXmlClass.setClose(close);
        foundXmlClass.setName(currentClass);
        foundXmlClass.setNumber(xmlClass.getNumber());
        foundXmlClass.setScore(xmlClass.getScore());
        foundXmlClass.setContent(xmlClass.getContent());
        foundXmlClass.setJavaInterface(xmlClass.isJavaInterface());
        nameXmlClass.put(currentClass, foundXmlClass);
    }

    public XmlClass getClass(String name) {
        return nameXmlClass.get(name);
    }

    public Collection<XmlClass> getClasses() {
        return nameXmlClass.values();
    }

    public boolean isDeclaredPackage() {
        return declaredPackage;
    }
    public void packageDeclared() {
        this.declaredPackage = true;
    }

    public String getURI() {
        return SourceCodeUtils.asString(file);
    }
}
