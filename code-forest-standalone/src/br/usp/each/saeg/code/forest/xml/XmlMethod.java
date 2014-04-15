package br.usp.each.saeg.code.forest.xml;

import java.math.*;
import java.util.*;

import javax.xml.bind.annotation.*;

public class XmlMethod {

    private String name;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private int loc;
    private List<XmlStatement> statements = new ArrayList<XmlStatement>();
    private int number;

    @XmlAttribute(name="suspicious-value")
    public BigDecimal getScore() {
        return score;
    }
    public void setScore(BigDecimal score) {
        if (null != score) {
            this.score = score;
        }
    }

    @XmlElement(name="requirement")
    public List<XmlStatement> getStatements() {
        return statements;
    }
    public void setStatements(List<XmlStatement> statements) {
        this.statements = statements;
    }

    @XmlAttribute(name="location")
    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc) {
        this.loc = loc;
    }

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name="number")
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
