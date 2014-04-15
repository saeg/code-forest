package br.usp.each.saeg.code.forest.util;

import java.awt.*;


public class DisplayUtils {

    public static float SIZE_PROPORTION = .8f;

    public static Dimension getScreenResolution() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Dimension getProportionalDimension() {
        return new Dimension((int) (getScreenResolution().getWidth() * SIZE_PROPORTION), (int) (getScreenResolution().getHeight() * SIZE_PROPORTION));
    }
}
