package br.usp.each.saeg.code.forest.domain;

import java.util.*;
import org.apache.commons.lang3.builder.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.text.*;
import br.usp.each.saeg.code.forest.ui.markers.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class LeafData implements Comparable<LeafData> {

    private float score;
    private int line;
    private Position position;
    private String value;
    private IMarker marker;
    private IResource resource;
    private String logLine;

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }

    public int getLine() {
        return line;
    }
    public void setLine(int line) {
        this.line = line;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public IMarker getMarker() {
        if (marker == null) {
            marker = CodeMarkerFactory.findMarker(resource, score, line);
        }
        return marker;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    public String getLogLine() {
        return logLine;
    }
    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

    public boolean isScoreBetween(float min, float max) {
        return Float.compare(score, max) <= 0 && Float.compare(score, min) >= 0;
    }

    @Override
    public int compareTo(LeafData o) {
        return new CompareToBuilder()
        .append(o.getScore(), score)
        .toComparison();
    }

    public static List<LeafData> covered(List<LeafData> arg) {
        List<LeafData> result = new ArrayList<LeafData>();
        for (LeafData data : arg) {
            if (data.getScore() < 0) {
                continue;
            }
            result.add(data);
        }

        return result;
    }
}
