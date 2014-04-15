package br.usp.each.saeg.code.forest.metaphor.assembler;


public class ForestRestrictions {

    private float minScore = 0;
    private float maxScore = 1;
    private boolean trees = true;
    private boolean branches = true;
    private boolean leaves = true;
    private String term;

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
}
