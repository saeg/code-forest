package representation;

import gfx.*;
import image.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import util.*;
import domain.*;

/**
 * @author dmutti@gmail.com
 */
public class ClassRepresentation extends JPanel {

    private static final long serialVersionUID = 1L;

    private RelativePoint position;
    private ClassDescription desc;
    private Rectangle2D tree;
    private MouseListener mouseListener;
    private List<MethodRepresentation> methods = new ArrayList<MethodRepresentation>();

    public ClassRepresentation(ClassDescription desc) {
        this.desc = desc;
        mouseListener = new ClassRepresentationListener(this);
        this.desc = desc;
        prepareComponent();
        setOpaque(false);
    }

    private void prepareComponent() {
        methods.clear();
        this.position = new RelativePoint(0,0);

        BranchDistribution distr = new BranchDistribution(desc, position, 2);
        TexturePaint branchPaint = ImageUtils.getBranchTexture();
        for (BranchInformation info : distr.getResult()) {
            methods.add(new MethodRepresentation(info, branchPaint));
        }

        List<Rectangle> left = Arrays.asList(
                Display.getInstance().atRelative(position.getX(), position.getY()),
                Display.getInstance().atRelative(position.getX(), position.getY() + distr.getHeight()));

        tree = new Rectangle2D.Double(left.get(0).getMinX(), left.get(1).getMaxY(), Display.getInstance().getCellSize(), (left.get(0).getMaxY() -  left.get(1).getMaxY()));
        adjustSize();
    }

    private void adjustSize() {
        Dimension size = new Dimension((int) getDimension().getWidth(), (int) getDimension().getHeight());
        setSize(size);
        setPreferredSize(size);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D d2 = (Graphics2D) g;
        d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        d2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        prepareComponent();

        d2.setPaint(ImageUtils.getBarkTexture());
        d2.translate(getTranslation().getX(), getTranslation().getY());
        d2.fill(tree);

        for (MethodRepresentation meth : methods) {
            meth.paintComponent(g);
        }
        //d2.draw(new Rectangle2D.Double(getDimension().getMinX(), getDimension().getMinY(), getDimension().getWidth(), getDimension().getHeight()));
    }

    private Point2D getTranslation() {
        double x = 0;
        double y = 0;

        Rectangle2D rect = getDimension();
        if (rect.getMinX() < 0) {
            x = Math.abs(rect.getMinX());
        }
        if (rect.getMinY() < 0) {
            y = Math.abs(rect.getMinY());
        } else {
            y = - 1 * rect.getMinY();
        }
        return new Point2D.Double(x, y);
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public Rectangle getDimension() {
        List<Line2D> lines = new ArrayList<Line2D>();
        lines.add(new Line2D.Double(tree.getMinX(), tree.getMaxY(), tree.getMaxX(), tree.getMinY()));
        for (MethodRepresentation meth: methods) {
            lines.add(meth.getBranchInformation().getAsLine2D());
        }

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (Line2D line : lines) {
            if (Double.compare(maxX, line.getX1()) < 0) {
                maxX = line.getX1();
            }
            if (Double.compare(maxX, line.getX2()) < 0) {
                maxX = line.getX2();
            }
            if (Double.compare(minX, line.getX2()) > 0) {
                minX = line.getX2();
            }
            if (Double.compare(minX, line.getX1()) > 0) {
                minX = line.getX1();
            }
            if (Double.compare(maxY, line.getY1()) < 0) {
                maxY = line.getY1();
            }
            if (Double.compare(maxY, line.getY2()) < 0) {
                maxY = line.getY2();
            }
            if (Double.compare(minY, line.getY2()) > 0) {
                minY = line.getY2();
            }
            if (Double.compare(minY, line.getY1()) > 0) {
                minY = line.getY1();
            }
        }
        int k = Display.getInstance().getCellSize();
        return new Rectangle((int) minX - k, (int) minY - k, (int) (maxX - minX) + (2 * k), (int) (maxY - minY) + (2 * k));
    }

    static class ClassRepresentationListener extends MouseAdapter {
        private ClassRepresentation parent;

        ClassRepresentationListener(ClassRepresentation arg) {
            this.parent = arg;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (parent.getDimension().contains(e.getLocationOnScreen())) {
                System.out.println(parent.desc.getName());
            }
        }
    }
}
