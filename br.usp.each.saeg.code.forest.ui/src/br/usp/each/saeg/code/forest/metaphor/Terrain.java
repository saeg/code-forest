package br.usp.each.saeg.code.forest.metaphor;

import javax.media.j3d.*;
import br.usp.each.saeg.code.forest.metaphor.util.*;
import com.sun.j3d.utils.geometry.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class Terrain {

    public static TransformGroup getFullTerrain() {
        Transform3D transform = new Transform3D();
        TransformGroup bg = new TransformGroup(transform);
        Box box = new Box(1000, 0, 1000, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, ImageUtils.getTerrainAppearance());
        bg.addChild(box);
        return bg;
    }

    public static TransformGroup getSmallTerrain(Forest arg) {
        Transform3D transform = new Transform3D();
        TransformGroup bg = new TransformGroup(transform);
        Box box = new Box(arg.getX(), 0, arg.getZ(), Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, ImageUtils.getTerrainAppearance());
        bg.addChild(box);
        return bg;
    }

}
