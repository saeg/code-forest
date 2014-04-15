package br.usp.each.saeg.code.forest.metaphor.collision;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import org.eclipse.core.resources.*;
import com.sun.j3d.utils.universe.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.views.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class KeyBehavior extends Behavior {
    private static final double FAST_SPEED = 20.0;
    private static final double NORMAL_SPEED = 1.0;
    private static final double SLOW_SPEED = 0.5;

    protected TransformGroup transformGroup;
    protected Transform3D transform3D;
    protected WakeupCondition keyCriterion;

    private double rotateXAmount = Math.PI / 16.0;
    private double rotateYAmount = Math.PI / 16.0;
    private double rotateZAmount = Math.PI / 16.0;

    private double moveRate = 0.5;
    private double speed = SLOW_SPEED;

    private final double kMoveForwardScale = 1.1;
    private final double kMoveBackwardScale = 0.9;

    private int forwardKey = KeyEvent.VK_DOWN;
    private int backKey = KeyEvent.VK_UP;
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    private int equalsKey = KeyEvent.VK_EQUALS;
    private final Vector3f initialPos;

    private final Object parent;
    private final IProject project;
    private final FilterPanel filterPanel;


    public KeyBehavior(IProject project, FilterPanel filterPanel, TransformGroup tg, Vector3f initialPosition, Object parent) {
        this.project = project;
        this.parent = parent;
        this.filterPanel = filterPanel;
        transformGroup = tg;
        transform3D = new Transform3D();
        initialPos = initialPosition;
    }

    public void initialize() {
        WakeupCriterion[] keyEvents = new WakeupCriterion[2];
        keyEvents[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        keyEvents[1] = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
        keyCriterion = new WakeupOr(keyEvents);

        wakeupOn(keyCriterion);
    }

    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] event;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();

            if (!(wakeup instanceof WakeupOnAWTEvent)) {
                continue;
            }

            event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();

            for (int i = 0; i < event.length; i++) {
                if (event[i].getID() == KeyEvent.KEY_PRESSED) {
                    processKeyEvent((KeyEvent) event[i]);
                }
            }
        }

        wakeupOn(keyCriterion);
    }

    protected void processKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();

        if (event.isShiftDown()) {
            if (keycode == KeyEvent.VK_DOWN) {
                shiftMove(KeyEvent.VK_UP);
            } else if (keycode == KeyEvent.VK_UP) {
                shiftMove(KeyEvent.VK_DOWN);
            } else {
                shiftMove(keycode);
            }
        } else if (event.isControlDown() || event.isMetaDown()) {
            if (keycode == KeyEvent.VK_DOWN) {
                controlMove(KeyEvent.VK_UP);
            } else if (keycode == KeyEvent.VK_UP) {
                controlMove(KeyEvent.VK_DOWN);
            } else {
                shiftMove(keycode);
            }

        } else {
            standardMove(keycode);
        }
    }

    // moves forward backward or rotates left right
    private void standardMove(int keycode) {
        if (keycode == forwardKey) {
            CodeForestUIPlugin.ui(project, parent, "standard down");
            moveForward();
        } else if (keycode == backKey) {
            CodeForestUIPlugin.ui(project, parent, "standard up");
            moveBackward();
        } else if (keycode == leftKey) {
            CodeForestUIPlugin.ui(project, parent, "standard left");
            moveLeft();
        } else if (keycode == rightKey) {
            CodeForestUIPlugin.ui(project, parent, "standard right");
            moveRight();
        } else if (keycode == equalsKey) {
            CodeForestUIPlugin.ui(project, parent, "view reset");
            filterPanel.resetFilter();
            goHome();
        }
    }

    // moves left right, rotate up down
    protected void shiftMove(int keycode) {
        if (keycode == forwardKey) {
            CodeForestUIPlugin.ui(project, parent, "alt down");
            rotUp();
        } else if (keycode == backKey) {
            CodeForestUIPlugin.ui(project, parent, "alt up");
            rotDown();
        } else if (keycode == leftKey) {
            CodeForestUIPlugin.ui(project, parent, "alt left");
            rotLeft();
        } else if (keycode == rightKey) {
            CodeForestUIPlugin.ui(project, parent, "alt right");
            rotRight();
        }
    }

    // move up down, rot left right
    protected void controlMove(int keycode) {
        if (keycode == forwardKey) {
            CodeForestUIPlugin.ui(project, parent, "ctrl down");
            moveUp();
        } else if (keycode == backKey) {
            CodeForestUIPlugin.ui(project, parent, "ctrl up");
            moveDown();
        } else if (keycode == leftKey) {
            CodeForestUIPlugin.ui(project, parent, "ctrl left");
            rollLeft();
        } else if (keycode == rightKey) {
            CodeForestUIPlugin.ui(project, parent, "ctrl right");
            rollRight();
        }
    }

    private void moveForward() {
        doMove(new Vector3d(0.0, 0.0, kMoveForwardScale * speed));
    }

    private void moveBackward() {
        doMove(new Vector3d(0.0, 0.0, -kMoveBackwardScale * speed));
    }

    private void moveLeft() {
        doMove(new Vector3d(-getMovementRate(), 0.0, 0.0));
    }

    private void moveRight() {
        doMove(new Vector3d(getMovementRate(), 0.0, 0.0));
    }

    private void moveUp() {
        doMove(new Vector3d(0.0, getMovementRate(), 0.0));
    }

    private void moveDown() {
        doMove(new Vector3d(0.0, -getMovementRate(), 0.0));
    }

    protected void rotRight() {
        doRotateY(getRotateRightAmount());
    }

    protected void rotUp() {
        doRotateX(getRotateUpAmount());
    }

    protected void rotLeft() {
        doRotateY(getRotateLeftAmount());
    }

    protected void rotDown() {
        doRotateX(getRotateDownAmount());
    }

    protected void rollLeft() {
        doRotateZ(getRollLeftAmount());
    }

    protected void rollRight() {
        doRotateZ(getRollRightAmount());
    }

    private void goHome() {
        transformGroup.getTransform(transform3D);
        transform3D.rotX(Math.toRadians(0));
        transform3D.rotY(Math.toRadians(0));
        transform3D.rotZ(Math.toRadians(0));
        transform3D.setTranslation(initialPos);
        updateTransform();
    }

    protected void updateTransform() {
        transformGroup.setTransform(transform3D);
    }

    protected void doRotateY(double radians) {
        transformGroup.getTransform(transform3D);
        Transform3D toMove = new Transform3D();
        toMove.rotY(radians);
        transform3D.mul(toMove);
        updateTransform();
    }

    protected void doRotateX(double radians) {
        transformGroup.getTransform(transform3D);
        Transform3D toMove = new Transform3D();
        toMove.rotX(radians);
        transform3D.mul(toMove);
        updateTransform();
    }

    protected void doRotateZ(double radians) {
        transformGroup.getTransform(transform3D);
        Transform3D toMove = new Transform3D();
        toMove.rotZ(radians);
        transform3D.mul(toMove);
        updateTransform();
    }

    protected void doMove(Vector3d theMove) {
        transformGroup.getTransform(transform3D);
        Transform3D toMove = new Transform3D();
        toMove.setTranslation(theMove);
        transform3D.mul(toMove);
        updateTransform();
    }

    protected double getMovementRate() {
        return moveRate * speed;
    }

    protected double getRollLeftAmount() {
        return rotateZAmount * speed;
    }

    protected double getRollRightAmount() {
        return -rotateZAmount * speed;
    }

    protected double getRotateUpAmount() {
        return rotateYAmount * speed;
    }

    protected double getRotateDownAmount() {
        return -rotateYAmount * speed;
    }

    protected double getRotateLeftAmount() {
        return rotateYAmount * speed;
    }

    protected double getRotateRightAmount() {
        return -rotateYAmount * speed;
    }
}
