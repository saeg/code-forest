package domain;

import java.util.*;

import util.*;


/**
 * @author dmutti@gmail.com
 */
public class MethodDescription {

    private LinkedHashMap<String, Float> locScore = new LinkedHashMap<String, Float>();
    private String name;
    private String className;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, Float> getLocScore() {
        return locScore;
    }

    public void setLocScore(Map<String, Float> locScore) {
        if (locScore != null) {
            this.locScore.clear();
            this.locScore.putAll(locScore);
        }
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public List<Float> getScoreOfEvenLoc() {
        return CollectionUtils.getEvenIndexedValuesFrom(locScore);
    }

    public List<Float> getScoreOfOddLoc() {
        return CollectionUtils.getOddIndexedValuesFrom(locScore);
    }
}
