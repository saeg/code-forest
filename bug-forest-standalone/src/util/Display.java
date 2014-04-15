package util;

import gfx.*;

import java.awt.*;

import javax.swing.*;

/**
 * @author dmutti@gmail.com
 */
public class Display {

    private static final Display instance = new Display();
    private JFrame component;
    private static final int ratio = 50;

    private Display() {
        this.component = new JFrame();
        component.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public static Display on(JFrame frame) {
        instance.component = frame;
        return instance;
    }

    public RelativePoint getRelativePoint(int x, int y) {
        return new RelativePoint(x/getCellSize(), y/getCellSize());
    }

    public RelativePoint getRelativePoint(double x, double y) {
        return new RelativePoint((int)x/getCellSize(), (int)y/getCellSize());
    }

    public Rectangle atRelative(int x, int y) {
        return at(new RelativePoint(x, y));
    }

    public Rectangle atAbsolute(int x, int y) {
        return at(new RelativePoint(x/getCellSize(), y/getCellSize()));
    }

    public Rectangle atAbsolute(double x, double y) {
        return at(new RelativePoint((int)x/getCellSize(), (int)y/getCellSize()));
    }

    public Rectangle at(RelativePoint arg) {
        return new Rectangle(arg.getX() * getCellSize(), (component.getHeight() / getCellSize()) * getCellSize() - (arg.getY() * getCellSize()), getCellSize(), getCellSize());
    }

    public static Display getInstance() {
        return instance;
    }

    public int getCellSize() {
        return Math.max(component.getWidth() / ratio, 1);
    }
}
