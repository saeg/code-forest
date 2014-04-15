package br.usp.each.saeg.code.forest.metaphor;

import javax.media.j3d.*;

import br.usp.each.saeg.code.forest.image.*;

import com.sun.j3d.utils.geometry.*;

public class Terrain {

    private final Transform3D transform;
    private final TransformGroup bg;

    public Terrain(Forest arg) {
        transform = new Transform3D();
        bg = new TransformGroup(transform);
        Box box = new  Box(arg.getX(), 0, arg.getZ(), Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, ImageUtils.getTerrainAppearance());
        bg.addChild(box);
    }

    public TransformGroup getTransformGroup() {
        return bg;
    }
}
