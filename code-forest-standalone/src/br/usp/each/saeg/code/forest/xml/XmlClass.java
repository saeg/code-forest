package br.usp.each.saeg.code.forest.xml;

import java.math.*;
import java.util.*;

import javax.xml.bind.annotation.*;

public class XmlClass {

    private String name;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private int loc;
    private List<XmlMethod> methods = new ArrayList<XmlMethod>();
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

    @XmlElement(name="method")
    public List<XmlMethod> getMethods() {
        return methods;
    }
    public void setMethods(List<XmlMethod> methods) {
        this.methods = methods;
    }

    @XmlAttribute(name="location")
    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc) {
        this.loc = loc;
    }

    @XmlAttribute(name="number")
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
