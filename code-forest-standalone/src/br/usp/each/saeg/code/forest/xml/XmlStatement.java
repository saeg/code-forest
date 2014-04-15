package br.usp.each.saeg.code.forest.xml;

import java.math.*;

import javax.xml.bind.annotation.*;

public class XmlStatement {

    private int loc;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());

    @XmlAttribute(name="location")
    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc) {
        this.loc = loc;
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
}
