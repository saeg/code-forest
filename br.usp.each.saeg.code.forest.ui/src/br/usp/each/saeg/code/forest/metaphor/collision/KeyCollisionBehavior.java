package br.usp.each.saeg.code.forest.metaphor.collision;

import javax.media.j3d.*;
import javax.vecmath.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.ui.views.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class KeyCollisionBehavior extends KeyBehavior {

    private CollisionChecker collisionChecker = null;

    public KeyCollisionBehavior(IProject project, FilterPanel filterPanel, TransformGroup tg, Vector3f initialPosition, CollisionDetector collisionDetector, Object parent) {
        super(project, filterPanel, tg, initialPosition, parent);
        collisionChecker = new CollisionChecker(tg, collisionDetector, true);
    }

    protected void updateTransform() {
        if (collisionChecker.isCollision(transform3D) == false) {
            transformGroup.setTransform(transform3D);
        }
    }
}
