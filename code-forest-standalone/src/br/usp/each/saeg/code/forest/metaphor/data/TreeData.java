package br.usp.each.saeg.code.forest.metaphor.data;

import java.util.*;

public class TreeData implements Comparable<TreeData> {

    private List<String> lines = new ArrayList<String>();
    private String value;
    private float score;
    private int loc;
    private List<BranchData> branchData = new ArrayList<BranchData>();

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

    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc) {
        this.loc = loc;
    }

    public List<String> getLines() {
        return lines;
    }
    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<BranchData> getBranchData() {
        return branchData;
    }
    public void setBranchData(List<BranchData> branchData) {
        if (null != branchData) {
            this.branchData = branchData;
        }
    }

    private int leftSide() {
        return (int) Math.ceil((double) (getNotEmptyBranchesCollectionSize())/2);
    }

    private int rightSide() {
        return Math.max(getNotEmptyBranchesCollectionSize() - leftSide(), 0);
    }

    public int getHeight() {
        return Math.max(leftSide(), rightSide());
    }

    public int getNotEmptyBranchesCollectionSize() {
        int result = 0;
        for (BranchData branch : branchData) {
            if (!branch.getLeafData().isEmpty()) {
                result++;
            }
        }
        return result;
    }

    public List<BranchData> getNotEmptyBranches() {
        List<BranchData> result = new ArrayList<BranchData>();
        for (BranchData desc : branchData) {
            if (!desc.getLeafData().isEmpty()) {
                result.add(desc);
            }
        }
        return result;
    }

    public boolean isScoreBetween(float min, float max) {
        return Float.compare(score, max) <= 0 && Float.compare(score, min) >= 0;
    }

    @Override
    public int compareTo(TreeData o) {
        return Float.compare(score, o.getScore());
    }
}
