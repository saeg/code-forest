package domain;

import gfx.*;

import java.awt.*;
import java.awt.geom.*;

import util.*;

/**
 * @author dmutti@gmail.com
 */
public class BranchInformation {

    public enum Direction {
        LEFT_HORIZONTAL, LEFT_DIAGONAL, UP_LEFT, UP_RIGHT, RIGHT_DIAGONAL, RIGHT_HORIZONTAL;
    }

    private final MethodDescription method;
    private final Direction direction;
    private final RelativePoint position;
    private final int maxSize;
    private int leftAngle, rightAngle;
    private double startX, startY, endX, endY;
    private LeafDistribution leafDistr;

    public BranchInformation(MethodDescription methodDescription, Direction direction, RelativePoint position, int maxBranchSize) {
        this.method = methodDescription;
        this.direction = direction;
        this.position = position;
        this.maxSize = maxBranchSize;
    }

    private void setUp() {
        Rectangle cell = Display.getInstance().at(position);
        int branchSize = Display.getInstance().getCellSize() * leafDistr.getBlocks();

        switch (direction) {
            case LEFT_HORIZONTAL : {
                leftAngle = 225;
                rightAngle = 135;
                startX = cell.getMinX();
                startY = cell.getMaxY();
                endX = cell.getMinX() - branchSize;
                endY = cell.getMaxY();
                break;
            }
            case RIGHT_HORIZONTAL : {
                startX = cell.getMaxX();
                startY = cell.getMaxY();
                endX = cell.getMaxX() + branchSize;
                endY = cell.getMaxY();
                leftAngle = 315;
                rightAngle = 45;
                break;
            }
            case UP_LEFT : {
                startX = cell.getMinX();
                startY = cell.getMaxY();
                endX = cell.getMinX();
                endY = cell.getMaxY() - branchSize;
                leftAngle = 225;
                rightAngle = 315;
                break;
            }
            case UP_RIGHT : {
                startX = cell.getMaxX();
                startY = cell.getMaxY();
                endX = cell.getMaxX();
                endY = cell.getMaxY() - branchSize;
                leftAngle = 225;
                rightAngle = 315;
                break;
            }
            case LEFT_DIAGONAL : {
                startX = cell.getMinX();
                startY = cell.getMaxY();
                endX = cell.getMinX() - branchSize;
                endY = cell.getMaxY() - branchSize;
                leftAngle = 180;
                rightAngle = 270;
                break;
            }
            case RIGHT_DIAGONAL : {
                startX = cell.getMaxX();
                startY = cell.getMaxY();
                endX = cell.getMaxX() + branchSize;
                endY = cell.getMaxY() - branchSize;
                leftAngle = 270;
                rightAngle = 0;
                break;
            }
        }
    }

    public Line2D getAsLine2D() {
        this.leafDistr = new LeafDistribution(Display.getInstance().getCellSize(), method, maxSize);
        setUp();
        return new Line2D.Double(startX, startY, endX, endY);
    }

    public MethodDescription getMethod() {
        return method;
    }
    public Direction getDirection() {
        return direction;
    }
    public RelativePoint getPosition() {
        return position;
    }
    public int getMaxSize() {
        return maxSize;
    }

    public int getLeftAngle() {
        return leftAngle;
    }

    public int getRightAngle() {
        return rightAngle;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public double getLeafOffset() {
        return leafDistr.getOffset();
    }

    public double getLeafDistance() {
        return leafDistr.getDistance();
    }
}
