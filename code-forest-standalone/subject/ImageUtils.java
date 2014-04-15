package image;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


/**
 * @author dmutti@gmail.com
 */
public class ImageUtils {

    public static TexturePaint getBarkTexture() {
        return from("Bark_Texture.png");
    }

    public static TexturePaint getBranchTexture() {
        return from("Branch_Texture.png");
    }

    private static TexturePaint from(String arg) {
        BufferedImage body = loadFromClassPath(arg);
        return new TexturePaint(body, new Rectangle(body.getWidth(), body.getHeight()));
    }

    public static BufferedImage loadFromClassPath(String name) {
        InputStream is = null;
        try {
            is = ImageUtils.class.getResourceAsStream("/image/" + name);
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
