package br.usp.each.saeg.code.forest.xml;

import java.math.*;
import java.util.*;

import javax.xml.bind.annotation.*;

public class XmlPackage {

    private String name;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private List<XmlClass> classes = new ArrayList<XmlClass>();
    private int number;

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name="suspicious-value")
    public BigDecimal getScore() {
        return score;
    }
    public void setScore(BigDecimal score) {
        if (null != score) {
            this.score = score;
        }
    }

    @XmlElement(name="class")
    public List<XmlClass> getClasses() {
        return classes;
    }
    public void setClasses(List<XmlClass> classes) {
        this.classes = classes;
    }

    @XmlAttribute(name="number")
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
