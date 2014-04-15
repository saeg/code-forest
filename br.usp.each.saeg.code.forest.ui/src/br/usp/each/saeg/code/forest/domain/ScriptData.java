package br.usp.each.saeg.code.forest.domain;

import org.apache.commons.lang3.builder.*;
import org.eclipse.core.resources.*;

public class ScriptData implements Comparable<ScriptData> {

    private String packageName;
    private String className;
    private String methodName;
    private int scriptPosition;
    private IMarker marker;
    private float score;

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getScriptPosition() {
        return scriptPosition;
    }
    public void setScriptPosition(int scriptPosition) {
        this.scriptPosition = scriptPosition;
    }

    public IMarker getMarker() {
        return marker;
    }
    public void setMarker(IMarker marker) {
        this.marker = marker;
    }

    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return className + ", " + methodName + ", " + scriptPosition;
    }

    @Override
    public int compareTo(ScriptData o) {
        return new CompareToBuilder()
            .append(scriptPosition, o.getScriptPosition())
            .append(o.getScore(), score)
            .toComparison();
    }
}
