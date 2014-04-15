package br.usp.each.saeg.code.forest.metaphor.data;

import java.util.*;

public class LeafData {

    private String value;
    private float score;
    private int loc;

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

    public boolean isScoreBetween(float min, float max) {
        return Float.compare(score, max) <= 0 && Float.compare(score, min) >= 0;
    }

    public Map<String, Float> getValueScore() {
        return Collections.singletonMap(value, score);
    }
}
