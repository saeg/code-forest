package mouse;

import java.awt.event.*;
import java.awt.geom.*;
import representation.*;


/**
 * @author dmutti@gmail.com
 * http://www.java.happycodings.com/Java2D/code4.html
 */
public class MethodRepresentationListener  extends MouseAdapter {
    private MethodRepresentation parent;

    public MethodRepresentationListener(MethodRepresentation arg) {
        this.parent = arg;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Line2D line = parent.getBranchInformation().getAsLine2D();
        if (distanceToSegment(line.getP1(), line.getP2(), e.getPoint()) < 3) {
            System.out.println(parent.getMethodDescription().getClassName() + "." + parent.getMethodDescription().getName());
        }
    }

    public static double distanceToSegment(Point2D p1, Point2D p2, Point2D p3) {

        final double xDelta = p2.getX() - p1.getX();
        final double yDelta = p2.getY() - p1.getY();

        if ((xDelta == 0) && (yDelta == 0)) {
            throw new IllegalArgumentException("p1 e p2 nao podem ser o mesmo ponto!");
        }

        final double u = ((p3.getX() - p1.getX()) * xDelta + (p3.getY() - p1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Point2D closestPoint;
        if (u < 0) {
            closestPoint = p1;
        } else if (u > 1) {
            closestPoint = p2;
        } else {
            closestPoint = new Point2D.Double(p1.getX() + u * xDelta, p1.getY() + u * yDelta);
        }

        return closestPoint.distance(p3);
    }
}
