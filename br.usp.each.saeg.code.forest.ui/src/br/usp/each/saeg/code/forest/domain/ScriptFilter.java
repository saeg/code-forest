package br.usp.each.saeg.code.forest.domain;

import org.apache.commons.lang3.*;
import br.usp.each.saeg.code.forest.ui.*;

public class ScriptFilter {

    private String searchString;
    private float baseScore;

    public ScriptFilter(ScriptData data) {
        searchString = StringUtils.substringBefore(data.getMethodName(), "(");
        baseScore = data.getScore();
    }

    public float getMinimumScore() {
        return Math.max(0, baseScore - (baseScore * Configuration.SCORE_THRESHOLD));
    }

    public float getMaximumScore() {
        return baseScore; //Math.max(0, baseScore - (baseScore * Configuration.SCORE_THRESHOLD));
    }

    public String getSearchString() {
        return searchString;
    }
}
