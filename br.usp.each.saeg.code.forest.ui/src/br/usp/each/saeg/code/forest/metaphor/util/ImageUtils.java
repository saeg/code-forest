package br.usp.each.saeg.code.forest.metaphor.util;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ImageUtils {

    // http://www.colorschemer.com/online.html
    private static final Color3f terrainColor = new Color3f(new Color(180, 103, 67));

    public static Appearance getTrunkAppearance(Material material) {
        if (Configuration.RENDER_WIREFRAME) {
            return WireframeImageUtils.getTrunkAppearance(material);
        }
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }

    public static Appearance getBranchAppearance(Material material) {
        if (Configuration.RENDER_WIREFRAME) {
            return WireframeImageUtils.getBranchAppearance(material);
        }
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }

    public static Background getBackground() {
        if (Configuration.RENDER_WIREFRAME) {
            return WireframeImageUtils.getBackground();
        }
        Background bg = new Background();
        bg.setColor(new Color3f(new Color(203, 215, 232)));
        return bg;
    }

    public static Appearance getLeafAppearance(Color3f color) {
        if (Configuration.RENDER_WIREFRAME) {
            return WireframeImageUtils.getLeafAppearance(color);
        }

        Material material = new Material(color, color, color, color, 100); // specular, 100
        material.setLightingEnable(true);
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }

    public static TransparencyAttributes createTransparency() {
        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparencyMode(TransparencyAttributes.NONE);
        ta.setTransparency(0);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
        ta.setCapability(TransparencyAttributes.ALLOW_MODE_READ);
        ta.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
        return ta;
    }

    public static Appearance getTerrainAppearance() {
        Material material = new Material(terrainColor, terrainColor, terrainColor, terrainColor, 1); // specular, 100
        material.setLightingEnable(true);

        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }
}
