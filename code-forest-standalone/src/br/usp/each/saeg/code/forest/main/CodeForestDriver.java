package br.usp.each.saeg.code.forest.main;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.*;

import br.usp.each.saeg.code.forest.gui.*;
import br.usp.each.saeg.code.forest.image.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.metaphor.square.assembler.*;
import br.usp.each.saeg.code.forest.util.*;

import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.universe.*;

public class CodeForestDriver extends Applet {

    private static final long serialVersionUID = 1L;
    private Canvas3D cv;
    private SimpleUniverse su;
    private Forest forest;
    private static final String rootDir = "d:/Devel/ant/";

    public static void main(String[] args) {
        new MainFrame(new CodeForestDriver(), (int) DisplayUtils.getProportionalDimension().getWidth(), (int) DisplayUtils.getProportionalDimension().getHeight());
    }

    public void init() {
        // create canvas
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        cv = new Canvas3D(gc);
        setLayout(new BorderLayout());

        add(cv, BorderLayout.CENTER);
        su = new SimpleUniverse(cv);
        BranchGroup bg = createSceneGraph();
        bg.compile();
        //su.getViewingPlatform().setNominalViewingTransform();
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup vpTransformGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTransform3D = new Transform3D();
        vpTransformGroup.getTransform(vpTransform3D);
        vpTransform3D.setTranslation(new Vector3f(0,0,20));
        vpTransformGroup.setTransform(vpTransform3D);

        su.addBranchGraph(bg);

        SourcePanel panel = new SourcePanel(forest);
        cv.addMouseListener(new DisplayTextGeometryMouseAdapter(cv, bg, panel));

        add(new FilterPanel(forest), BorderLayout.EAST);
        add(panel, BorderLayout.SOUTH);
    }

    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_DETACH);
        List<TreeData> treeData = new ArrayList<TreeData>(new TreeDataBuilder(new File(rootDir + "src"), new File(rootDir + "xml")).getTreeData());

        Transform3D transform3d = new Transform3D();
        transform3d.rotX(Math.toRadians(90));
        transform3d.setScale(.1);
        TransformGroup transformGroup = new TransformGroup(transform3d);
        forest = new SquareForest(treeData);
        transformGroup.addChild(new Terrain(forest).getTransformGroup());

        for (TransformGroup tg : forest.getTrees()) {
            transformGroup.addChild(tg);
        }
        root.addChild(transformGroup);
        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(forest.getX()* 3);

        // background and light
        Background background = ImageUtils.getBackground();
        background.setApplicationBounds(bounds);
        root.addChild(background);

        PointLight light = new PointLight(new Color3f(Color.white),
                new Point3f(1f,1f,1f),
                new Point3f(1f,0.1f,0f));
        light.setInfluencingBounds(bounds);
        root.addChild(light);

        OrbitBehavior orbit = new OrbitBehavior(cv, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
        return root;
    }
}
