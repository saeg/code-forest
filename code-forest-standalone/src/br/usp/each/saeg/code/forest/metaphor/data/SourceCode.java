package br.usp.each.saeg.code.forest.metaphor.data;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.apache.commons.lang3.*;

import br.usp.each.saeg.code.forest.metaphor.*;

public class SourceCode {

    private String[] source;
    private Color[] scores;
    private TreeData data;

    public SourceCode(Trunk trunk) {
        data = trunk.getData();
        source = new String[data.getLines().size()];
        scores = new Color[data.getLines().size()];
        populate();
    }

    public String getText() {
        return StringUtils.join(source, "\n");
    }

    public List<Color> getScores() {
        return Arrays.asList(scores);
    }

    public int getLinePosition(CodeGeometry selected) {
        int loc;
        if (selected instanceof Leaf) {
            loc = ((Leaf) selected).getData().getLoc();

        } else if (selected instanceof Branch){
            loc = ((Branch) selected).getData().getLoc();

        } else {
            loc = data.getLoc();
        }
        int caret = 0;
        for (int i = 0; i < loc; i++) {
            caret += StringUtils.length(source[i]) + 1;
        }
        return caret;
    }

    private void populate() {
        source[data.getLoc()] = data.getLines().get(data.getLoc());
        scores[data.getLoc()] = createColor(data.getScore());
        for (BranchData branch : data.getBranchData()) {
            source[branch.getLoc()] = data.getLines().get(branch.getLoc());
            scores[branch.getLoc()] = createColor(branch.getScore());
            for (LeafData leaf : branch.getLeafData()) {
                source[leaf.getLoc()] = data.getLines().get(leaf.getLoc());
                scores[leaf.getLoc()] = createColor(leaf.getScore());
            }
        }
        for (int i = 0; i < data.getLines().size(); i++) {
            if (null == scores[i]) {
                source[i] = data.getLines().get(i);
                scores[i] = Color.white;
            }
        }
    }

    private static Color createColor(float score) {
        Color start = Color.red;
        Color end = Color.green;

        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 6;

        float hueMax = 0;
        float hueMin = 0;
        if (startHSB[0] > endHSB[0]) {
            hueMax = startHSB[0];
            hueMin = endHSB[0];
        } else {
            hueMin = startHSB[0];
            hueMax = endHSB[0];
        }

        float hue = ((hueMax - hueMin) * (1 - score)) + hueMin;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
