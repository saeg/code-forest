package br.usp.each.saeg.code.forest.metaphor.collision;

import javax.media.j3d.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public interface CollisionDetector {

    public boolean isCollision(Transform3D t3d, boolean bViewSide);
}
