package representation;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.*;

import mouse.*;
import util.*;
import domain.*;
import domain.BranchInformation.Direction;

/**
 * @author dmutti@gmail.com
 */
public class MethodRepresentation extends JComponent {

    private static final long serialVersionUID = 1L;
    private MethodDescription desc;
    private BranchInformation geom;
    private TexturePaint texture;
    private MouseListener mouseListener;
    private static float leafRatio = 2.8F;

    public MethodRepresentation(BranchInformation info, TexturePaint texturePaint) {
        this.desc = info.getMethod();
        this.geom = info;
        this.texture = texturePaint;
        this.mouseListener = new MethodRepresentationListener(this);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        if (desc.getLocScore().isEmpty()) {
            return;
        }
        Graphics2D d2 = (Graphics2D) g;
        d2.setStroke(new BasicStroke(Display.getInstance().getCellSize()/4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        d2.setPaint(texture);
        d2.draw(geom.getAsLine2D());

        d2.setStroke(new BasicStroke(Display.getInstance().getCellSize()/6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawLeaves(geom.getLeftAngle(), d2, CollectionUtils.getEvenIndexedValuesFrom(desc.getLocScore()));
        drawLeaves(geom.getRightAngle(), d2, CollectionUtils.getOddIndexedValuesFrom(desc.getLocScore()));
    }

    private void drawLeaves(int angle, Graphics2D d2, List<Float> leaves) {

        if (geom.getDirection() == Direction.UP_LEFT || geom.getDirection() == Direction.UP_RIGHT) {
            drawLeavesVertical(angle, d2, leaves);
            return;
        }
        if (geom.getDirection() == Direction.LEFT_HORIZONTAL || geom.getDirection() == Direction.RIGHT_HORIZONTAL) {
            drawLeavesHorizontal(angle, d2, leaves);
            return;
        }
        drawLeavesDiagonal(angle, d2, leaves);
    }

    private void drawLeavesVertical(int angle, Graphics2D d2, List<Float> leaves) {
        for (int i = 0; i < leaves.size(); i++) {
            d2.setColor(color(leaves.get(i)));
            double delta = geom.getStartY() - geom.getLeafOffset() - (i * geom.getLeafDistance());
            d2.draw(new Line2D.Double(geom.getStartX(), delta,  geom.getStartX() + Math.cos(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio), delta + Math.sin(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio)));
        }
    }

    private void drawLeavesHorizontal(int angle, Graphics2D d2, List<Float> leaves) {
        double delta;
        int signal = 1;
        if (geom.getDirection() == Direction.LEFT_HORIZONTAL) {
            signal = -1;
        }

        for (int i = 0; i < leaves.size(); i++) {
            d2.setColor(color(leaves.get(i)));
            delta = geom.getStartX() + ((geom.getLeafOffset() + (i * geom.getLeafDistance())) * signal);
            d2.draw(new Line2D.Double(delta, geom.getStartY(),  delta + Math.cos(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio), geom.getStartY() + Math.sin(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio)));
        }
    }

    private void drawLeavesDiagonal(int angle, Graphics2D d2, List<Float> leaves) {
        double newOffset = Math.sqrt(Math.pow(geom.getLeafOffset(), 2));
        double newDistance = Math.sqrt(Math.pow(geom.getLeafDistance(), 2));
        int signal = 1;
        if (geom.getDirection() == Direction.LEFT_DIAGONAL) {
            signal = -1;
        }

        Line2D aux = new Line2D.Double(geom.getStartX() + (newOffset*signal), geom.getStartY() - newOffset, geom.getEndX(), geom.getEndY());

        for (int i = 0; i < leaves.size(); i++) {
            d2.setColor(color(leaves.get(i)));
            double deltaX = geom.getStartX() + ((newOffset + (i * newDistance)) * signal);
            double y = (aux.getY2()-aux.getY1()) / (aux.getX2()-aux.getX1()) * (deltaX - aux.getX1()) + aux.getY1();
            d2.draw(new Line2D.Double(deltaX, y, deltaX + Math.cos(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio), y + Math.sin(Math.toRadians(angle)) * (Display.getInstance().getCellSize()/leafRatio)));
        }
    }

    private static Color color(Float score) {
        Color start = Color.red;
        Color end = Color.green;

        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 2;

        float hueMax = 0;
        float hueMin = 0;
        if (startHSB[0] > endHSB[0]) {
            hueMax = startHSB[0];
            hueMin = endHSB[0];
        } else {
            hueMin = startHSB[0];
            hueMax = endHSB[0];
        }

        float hue = ((hueMax - hueMin) * score) + hueMin;

        return Color.getHSBColor(hue, saturation, brightness);
    }

    public MouseListener[] getMouseListeners() {
        return new MouseListener[] { mouseListener };
    }

    public BranchInformation getBranchInformation() {
        return geom;
    }

    public MethodDescription getMethodDescription() {
        return desc;
    }
}
