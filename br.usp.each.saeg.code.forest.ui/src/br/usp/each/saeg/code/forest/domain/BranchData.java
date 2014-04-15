package br.usp.each.saeg.code.forest.domain;

import java.util.*;
import org.apache.commons.lang3.builder.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.text.*;
import br.usp.each.saeg.code.forest.ui.markers.*;
import br.usp.each.saeg.code.forest.util.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class BranchData implements Comparable<BranchData> {

    private List<LeafData> leafData = new ArrayList<LeafData>();
    private String name;
    private String value;
    private float score;
    private int ocurrences;
    private Position position;
    private Position closePosition;
    private IMarker openMarker;
    private IMarker closeMarker;
    private TreeData parent;
    private IResource resource;
    private int scriptPosition;
    private float scriptScore;
    private String logLine;

    private int startLine;
    private int endLine;
    private int closeLine;

    public BranchData(TreeData parent) {
        this.parent = parent;
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getClosePosition() {
        return closePosition;
    }
    public void setClosePosition(Position closePosition) {
        this.closePosition = closePosition;
    }

    public List<LeafData> getLeafData() {
        return leafData;
    }
    public List<LeafData> getCoveredLeafData() {
        return LeafData.covered(leafData);
    }
    public void setLeafData(List<LeafData> leafData) {
        if (null != leafData) {
            this.leafData = leafData;
        }
        Collections.sort(this.leafData);
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }

    public int getOcurrences() {
        return ocurrences;
    }
    public void setOcurrences(int ocurrences) {
        this.ocurrences = ocurrences;
    }

    public List<Float> getScoreOfCoveredEvenLoc() {
        return from(CollectionUtils.getEvenIndexedValuesFrom(LeafData.covered(leafData)));
    }

    public List<Float> getScoreOfCoveredOddLoc() {
        return from(CollectionUtils.getOddIndexedValuesFrom(LeafData.covered(leafData)));
    }

    private List<Float> from(List<LeafData> partial) {
        List<Float> result = new ArrayList<Float>();
        for (LeafData data : partial) {
            result.add(data.getScore());
        }
        return result;
    }

    public boolean isScoreBetween(float min, float max) {
        return Float.compare(score, max) <= 0 && Float.compare(score, min) >= 0;
    }

    public IMarker getOpenMarker() {
        if (openMarker == null) {
            openMarker = CodeMarkerFactory.findMarker(resource, score, startLine, endLine);
        }
        return openMarker;
    }

    public IMarker getCloseMarker() {
        if (closeMarker == null) {
            closeMarker = CodeMarkerFactory.findMarker(resource, score, closeLine);
        }
        return closeMarker;
    }

    public int getStartLine() {
        return startLine;
    }
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getCloseLine() {
        return closeLine;
    }
    public void setCloseLine(int closeLine) {
        this.closeLine = closeLine;
    }

    public int getProportionOfCoveredLoCs() {
        if (getTotalLoCs() == 0) {
            return 0;
        }
        if (parent.getTotalLoCs() == 0) {
            return 0;
        }
        return (int) ((getTotalLoCs() / (float) parent.getTotalLoCs()) * 100);
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getScriptPosition() {
        return scriptPosition;
    }
    public void setScriptPosition(Integer scriptPosition) {
        if (scriptPosition != null) {
            this.scriptPosition = scriptPosition;
        }
    }

    public float getScriptScore() {
        return scriptScore;
    }
    public void setScriptScore(Float scriptScore) {
        if (scriptScore != null) {
            this.scriptScore = scriptScore;
        }
    }

    public String getLogLine() {
        return logLine;
    }
    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

    @Override
    public int compareTo(BranchData o) {
        return new CompareToBuilder()
        .append(o.getScore(), score)
        .append(o.getOcurrences(), ocurrences)
        .append(CollectionUtils.size(o.getLeafData()), CollectionUtils.size(leafData))
        .toComparison();
    }

    public int getTotalLoCs() {
        return LeafData.covered(leafData).size();
    }

    public static List<BranchData> covered(List<BranchData> arg) {
        List<BranchData> result = new ArrayList<BranchData>();
        for (BranchData data : arg) {
            if (data.getScore() < 0 || data.getCoveredLeafData().size() == 0) {
                continue;
            }
            result.add(data);
        }

        return result;
    }
}
