package br.usp.each.saeg.code.forest.domain;

import org.apache.commons.lang3.builder.*;
import br.usp.each.saeg.code.forest.util.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public enum TreeDataComparator {

    SCORE_ONLY, SCORE_AND_NUMBER, SCORE_AND_NUMBER_AND_METHODS;

    public int compare(TreeData one, TreeData other) {
        switch (this) {
            case SCORE_ONLY : return scoreOnly(one, other);
            case SCORE_AND_NUMBER : return scoreAndNumber(one, other);
            case SCORE_AND_NUMBER_AND_METHODS : return scoreAndNumberAndMethods(one, other);
            default: throw new IllegalStateException("estrategia invalida");
        }
    }

    private static int scoreOnly(TreeData one, TreeData other) {
        return new CompareToBuilder()
            .append(one.getScore(), other.getScore())
            .append(one.getValue(), other.getValue())//desempate
            .toComparison();
    }

    private static int scoreAndNumber(TreeData one, TreeData other) {
        return new CompareToBuilder()
            .append(one.getScore(), other.getScore())
            .append(other.getOccurrences(), one.getOccurrences())
            .append(one.getValue(), other.getValue())//desempate
            .toComparison();
    }

    private static int scoreAndNumberAndMethods(TreeData one, TreeData other) {
        return new CompareToBuilder()
            .append(one.getScore(), other.getScore())
            .append(other.getOccurrences(), one.getOccurrences())
            .append(CollectionUtils.size(other.getBranchData()), CollectionUtils.size(one.getBranchData()))
            .append(one.getValue(), other.getValue())//desempate
            .toComparison();
    }
}
