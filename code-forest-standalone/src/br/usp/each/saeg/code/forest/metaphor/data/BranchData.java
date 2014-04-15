package br.usp.each.saeg.code.forest.metaphor.data;

import java.util.*;

import br.usp.each.saeg.code.forest.util.*;

public class BranchData implements Comparable<BranchData> {

    private List<LeafData> leafData = new ArrayList<LeafData>();
    private int loc;
    private String value;
    private float score;

    public List<LeafData> getLeafData() {
        return leafData;
    }
    public void setLeafData(List<LeafData> leafData) {
        if (null != leafData) {
            this.leafData = leafData;
        }
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

    public List<Float> getScoreOfEvenLoc() {
        List<LeafData> partial = CollectionUtils.getEvenIndexedValuesFrom(leafData);
        return from(partial);
    }

    public List<Float> getScoreOfOddLoc() {
        return from(CollectionUtils.getOddIndexedValuesFrom(leafData));
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

    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc) {
        this.loc = loc;
    }

    @Override
    public int compareTo(BranchData o) {
        return Float.compare(o.getScore(), score);
    }
}
