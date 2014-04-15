package br.usp.each.saeg.code.forest.metaphor;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.media.j3d.*;
import javax.vecmath.*;
import br.usp.each.saeg.code.forest.metaphor.building.blocks.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public abstract class CodeGeometry {

    protected Vector3d translation;
    private boolean enabled = true;
    protected Appearance appearance;
    protected List<GroupDataItem> groupData = new ArrayList<GroupDataItem>();

    protected Material getMaterial(Color3f arg) {
        Material result = new Material(arg, arg, arg, arg, 100);  // specular, 100
        result.setLightingEnable(true);
        return result;
    }

    public abstract void changeStatus();

    public void enable() {
        if (enabled) {
            return;
        }
        enabled = true;
        for (GroupDataItem data : groupData) {
            data.enable();
        }
    }

    public void disable() {
        if (!enabled) {
            return;
        }
        enabled = false;
        for (GroupDataItem data : groupData) {
            data.disable();
        }
    }

    public abstract void refresh();

    public static Color createColor(float score) {
        if (score < 0) {
            return Color.WHITE;
        }
        return createColor(Color.red, Color.green, score);
    }

    public static Color createColor(Color start, Color end, float score) {
        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 2;

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

    public boolean isEnabled() {
        return enabled;
    }

    public abstract Trunk getTrunk();

}
