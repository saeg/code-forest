package br.usp.each.saeg.code.forest.domain;

import br.usp.each.saeg.code.forest.ui.*;

public class ScriptEligibility {

    public static boolean isEligible(BranchData branch) {
        if (branch.getScriptPosition() == 0) {
            return false;
        }

        float baseScore = branch.getScriptScore();
        if (Float.compare(baseScore, 0) == 0) {
            return false;
        }

        for (LeafData leaf : branch.getLeafData()) {
            if (Float.compare(leaf.getScore(), (Math.max(0, baseScore - (baseScore * Configuration.SCORE_THRESHOLD)))) >= 0) {
                return true;
            }
        }

        return false;
    }
}
