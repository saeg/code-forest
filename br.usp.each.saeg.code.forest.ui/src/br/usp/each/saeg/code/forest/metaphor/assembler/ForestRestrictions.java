package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.text.*;
import org.apache.commons.lang3.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ForestRestrictions {

    private float minScore = 0;
    private float maxScore = 1;
    private boolean trees = true;
    private boolean branches = true;
    private boolean leaves = true;
    private String term;
    private static final DecimalFormat df;

    static {
        df = new DecimalFormat("0.00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
    }

    public float getMinScore() {
        return minScore;
    }

    public void setMinScore(float minScore) {
        this.minScore = minScore;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public boolean isTrees() {
        return trees;
    }

    public void setTrees(boolean trees) {
        this.trees = trees;
    }

    public boolean isBranches() {
        return branches;
    }

    public void setBranches(boolean branches) {
        this.branches = branches;
    }

    public boolean isLeaves() {
        return leaves;
    }

    public void setLeaves(boolean leaves) {
        this.leaves = leaves;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean containsTerm(String str) {
        if (StringUtils.isBlank(term)) {
            return true;
        }
        String trimmed = StringUtils.trim(term);
        return StringUtils.isNotBlank(trimmed) && StringUtils.containsIgnoreCase(str, trimmed);
    }

    public String toString() {
        return new StringBuilder().append("min-score-filter=").append(df.format(minScore)).append(", ")
        .append("max-score-filter=").append(df.format(maxScore)).append(", ")
        .append("search-term=").append(StringUtils.defaultString(term)).toString();
    }

    public void reset() {
        minScore = 0;
        maxScore = 1;
        trees = true;
        branches = true;
        leaves = true;
        term = null;
    }
}
