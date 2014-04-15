package br.usp.each.saeg.code.forest.domain;

import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.text.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.markers.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class TreeData implements Comparable<TreeData> {

    private Position packagePosition;
    private float packageScore;
    private Position openPosition;
    private Position closePosition;
    private IMarker openMarker;
    private IMarker closeMarker;
    private int startLine;
    private int endLine;
    private int closeLine;

    private String value;
    private float score;
    private List<BranchData> branchData = new ArrayList<BranchData>();
    private int occurrences;
    private boolean anonymous;

    private String name;

    private IResource resource;
    private String logLine;

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

    public List<BranchData> getBranchData() {
        return branchData;
    }
    public void setBranchData(List<BranchData> branchData) {
        if (null != branchData) {
            this.branchData = branchData;
        }
    }

    public int getOccurrences() {
        return occurrences;
    }
    public void setOcurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public Size getSize() {
        return Size.from(score);
    }

    private int leftSide() {
        return (int) Math.ceil((double) (BranchData.covered(branchData).size())/2);
    }

    private int rightSide() {
        return Math.max(BranchData.covered(branchData).size() - leftSide(), 0);
    }

    public int getHeight() {
        return Math.max(leftSide(), rightSide());
    }

    public boolean isAnonymous() {
        return anonymous;
    }
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Position getPackagePosition() {
        return packagePosition;
    }
    public void setPackagePosition(Position packagePosition) {
        this.packagePosition = packagePosition;
    }

    public float getPackageScore() {
        return packageScore;
    }
    public void setPackageScore(float packageScore) {
        this.packageScore = packageScore;
    }

    public Position getOpenPosition() {
        return openPosition;
    }
    public void setOpenPosition(Position openPosition) {
        this.openPosition = openPosition;
    }

    public Position getClosePosition() {
        return closePosition;
    }
    public void setClosePosition(Position closePosition) {
        this.closePosition = closePosition;
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

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    public boolean isScoreBetween(float min, float max) {
        return Float.compare(score, max) <= 0 && Float.compare(score, min) >= 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLogLine() {
        return logLine;
    }
    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

    public int getTotalLoCs() {
        int result = 0;
        for (BranchData branch : branchData) {
            result += branch.getTotalLoCs();
        }
        return result;
    }

    @Override
    public int compareTo(TreeData o) {
        return Configuration.TREE_DATA_COMPARE_STRATEGY.compare(this, o);
    }

    public static List<TreeData> covered(List<TreeData> arg) {
        List<TreeData> result = new ArrayList<TreeData>();
        for (TreeData data : arg) {
            if (data.getScore() < 0) {
                continue;
            }
            result.add(data);
        }

        return result;
    }
}
