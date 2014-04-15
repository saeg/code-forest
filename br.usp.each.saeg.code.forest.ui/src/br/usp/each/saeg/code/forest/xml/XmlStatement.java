package br.usp.each.saeg.code.forest.xml;

import java.math.*;
import javax.xml.bind.annotation.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlStatement {

    private int loc;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private int start;
    private int end;
    private String content;

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

    @XmlTransient
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
        this.loc = start;
    }

    @XmlTransient
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }

    @XmlTransient
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
