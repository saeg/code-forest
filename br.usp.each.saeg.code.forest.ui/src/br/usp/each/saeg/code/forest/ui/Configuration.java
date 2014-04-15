package br.usp.each.saeg.code.forest.ui;

import br.usp.each.saeg.code.forest.domain.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class Configuration {

    public static enum SizingAttribute {
        PACKAGE, CLASS;
    }

    public static final float SCORE_THRESHOLD = .03f;
    public static boolean CONSIDER_LINE_OCURRENCES = true;
    public static final SizingAttribute SIZING_ATTRIBUTE = SizingAttribute.CLASS;
    public static TreeDataComparator TREE_DATA_COMPARE_STRATEGY = TreeDataComparator.SCORE_AND_NUMBER_AND_METHODS;
    public static final boolean RENDER_WIREFRAME = false;
}
