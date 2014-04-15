package br.usp.each.saeg.code.forest.metaphor.collision;

import javax.media.j3d.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CollisionChecker {

    private CollisionDetector detector = null;
    private boolean viewSide = false;

    public CollisionChecker(Node node, CollisionDetector detector, boolean bViewSide) {
        this.detector = detector;
        this.viewSide = bViewSide;
    }

    public boolean isCollision(Transform3D t3d) {
        return detector.isCollision(t3d, viewSide);
    }
}
