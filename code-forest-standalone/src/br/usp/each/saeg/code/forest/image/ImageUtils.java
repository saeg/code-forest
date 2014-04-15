package br.usp.each.saeg.code.forest.image;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import com.sun.j3d.utils.image.*;


/**
 * @author dmutti@gmail.com
 */
public class ImageUtils {

    //http://www.colorschemer.com/online.html
    private static final Color3f terrainColor = new Color3f(new Color(112,72,7));

    public static Appearance getTrunkAppearance(Material material) {
        Appearance ap = from("Bark_Texture.jpg", Texture.WRAP);
        ap.setMaterial(material);
        //ap.setTransparencyAttributes(createTransparency());
        return ap;
    }

    public static ImageIcon getImageIcon() {
        return new ImageIcon(loadFromClassPath("forward.png"));
    }

    public static Appearance getBranchAppearance(Material material) {
        Appearance ap = from("Branch_Texture.jpg", Texture.WRAP);
        ap.setMaterial(material);
        //ap.setTransparencyAttributes(createTransparency());
        return ap;
    }

    public static Background getBackground() {
        TextureLoader loader = new TextureLoader(loadFromClassPath("background.jpg"), new Container());
        ImageComponent2D textImage = loader.getImage();
        Background bg = new Background();
        bg.setImage(textImage);
        bg.setImageScaleMode(Background.SCALE_FIT_ALL);
        return bg;
    }

    public static Appearance getLeafAppearance(Color3f color) {
        Material material = new Material(color, color, color, color, 100);  // specular, 100
        material.setLightingEnable(true);
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        //ap.setTransparencyAttributes(createTransparency());
        return ap;
    }

    public static TransparencyAttributes createTransparency() {
        TransparencyAttributes ta = new TransparencyAttributes ();
        ta.setTransparencyMode(TransparencyAttributes.NONE);
        ta.setTransparency(0);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
        ta.setCapability(TransparencyAttributes.ALLOW_MODE_READ);
        ta.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
        return ta;
    }

    public static Appearance getTerrainAppearance() {
        Material material = new Material(terrainColor, terrainColor, terrainColor, terrainColor, 1);  // specular, 100
        material.setLightingEnable(true);

        Appearance ap = new Appearance();
        ap.setMaterial(material);
        return ap;
    }

    private static Appearance from(String arg, int boundaryMode) {
        TextureLoader loader = new TextureLoader(loadFromClassPath(arg), new Container());
        ImageComponent2D textImage = loader.getImage();
        Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture.RGB, textImage.getWidth(), textImage.getHeight());
        texture.setBoundaryModeS(boundaryMode);
        texture.setBoundaryModeT(boundaryMode);
        texture.setMagFilter(Texture.FASTEST);
        texture.setMinFilter(Texture.FASTEST);

        texture.setImage(0, textImage);
        texture.setEnable(true);
        TextureAttributes textAttr = new TextureAttributes();
        textAttr.setTextureMode(TextureAttributes.BLEND);

        Appearance ap = new Appearance();
        ap.setTexture(texture);
        ap.setTextureAttributes(textAttr);
        return ap;
    }

    public static BufferedImage loadFromClassPath(String name) {
        InputStream is = null;
        try {
            is = ImageUtils.class.getResourceAsStream("./" + name);
            return ImageIO.read(is);

        } catch (Exception e) {
            return null;

        } finally {
            try {
                is.close();
            } catch (Exception ignored) {}
        }
    }
}
