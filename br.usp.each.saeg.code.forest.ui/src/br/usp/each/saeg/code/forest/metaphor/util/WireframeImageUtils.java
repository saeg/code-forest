package br.usp.each.saeg.code.forest.metaphor.util;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;


/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class WireframeImageUtils {

    //http://www.colorschemer.com/online.html
    private static final Color3f terrainColor = new Color3f(new Color(112,72,7));
    private static PolygonAttributes polyAttribs = new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_FRONT, 0 );

    public static Appearance getTrunkAppearance(Material material) {
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        ap.setPolygonAttributes(polyAttribs);
        return ap;
    }

    public static Appearance getBranchAppearance(Material material) {
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        ap.setPolygonAttributes(polyAttribs);
        return ap;
    }

    public static Background getBackground() {
        Background bg = new Background();
        bg.setColor(new Color3f(Color.black));
        return bg;
    }

    public static Appearance getLeafAppearance(Color3f color) {
        Material material = new Material(color, color, color, color, 100);  // specular, 100
        material.setLightingEnable(true);
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        ap.setPolygonAttributes(polyAttribs);
        return ap;
    }

    public static Appearance getTerrainAppearance() {
        Material material = new Material(terrainColor, terrainColor, terrainColor, terrainColor, 1);  // specular, 100
        material.setLightingEnable(true);

        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }
}
