package br.usp.each.saeg.code.forest.ui.views;

import java.awt.*;
import java.util.List;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import org.eclipse.core.resources.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import swingintegration.example.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.mouse.*;
import br.usp.each.saeg.code.forest.metaphor.util.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.project.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.universe.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CodeForestMouseView extends ViewPart {

    public static final String VIEW_ID = "br.usp.each.saeg.code.forest.menu.view.code.forest.mouse";
    private Canvas3D cv;
    private SimpleUniverse su;
    private Forest forest;
    private EmbeddedSwingComposite composite;
    private IProject project;

    @Override
    /**
     * http://www.eclipse.org/swt/snippets/#awt
     * http://www.eclipse.org/articles/article.php?file=Article-Swing-SWT-Integration/index.html
     */
    public void createPartControl(Composite parent) {
        try {
            System.setProperty("sun.awt.noerasebackground", "true");
        } catch (NoSuchMethodError error) {
        }

        project = ProjectUtils.getCurrentSelectedProject();
        if (project == null) {
            return;
        }
        final ProjectState state = ProjectPersistence.getStateOf(project);
        if (state == null || !state.containsAnalysis()) {
            return;
        }
        setPartName(getSecondaryViewId(project));

        if (composite != null) {
            composite.setVisible(false);
            composite.dispose();
            composite = null;
        }

        composite = new EmbeddedSwingComposite(parent, SWT.EMBEDDED) {
            @Override
            protected JComponent createSwingComponent() {
                return createContent(state);
            }
        };
        composite.populate();
    }

    private JPanel createContent(ProjectState state) {
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        cv = new Canvas3D(gc);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(cv, BorderLayout.CENTER);
        su = new SimpleUniverse(cv);
        BranchGroup bg = createSceneGraph(state.getAllAnalysis());
        bg.compile();
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup vpTransformGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTransform3D = new Transform3D();
        vpTransformGroup.getTransform(vpTransform3D);
        vpTransform3D.setTranslation(new Vector3f(0, 2, 20));
        vpTransformGroup.setTransform(vpTransform3D);
        su.addBranchGraph(bg);
        cv.addMouseListener(new DisplayTextGeometryMouseAdapter(project, this, cv, bg));
        FilterPanel filterPanel = new FilterPanel(project, forest, cv, this);

        panel.add(filterPanel, BorderLayout.EAST);
        panel.add(new ScriptPanel(project, this, state, filterPanel), BorderLayout.SOUTH);
        return panel;
    }

    private BranchGroup createSceneGraph(List<TreeData> treeData) {
        BranchGroup root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_DETACH);

        Transform3D transform3d = new Transform3D();
        transform3d.rotX(Math.toRadians(90));
        transform3d.setScale(.1);
        TransformGroup transformGroup = new TransformGroup(transform3d);
        forest = new SquareForest(treeData);
        transformGroup.addChild(Terrain.getSmallTerrain(forest));

        for (TransformGroup tg : forest.getTrees()) {
            transformGroup.addChild(tg);
        }
        root.addChild(transformGroup);
        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(forest.getX() * 3);

        Background background = ImageUtils.getBackground();
        background.setApplicationBounds(bounds);
        root.addChild(background);

        PointLight light = new PointLight(new Color3f(Color.white), new Point3f(1f, 1f, 1f), new Point3f(1f, 0.1f, 0f));
        light.setInfluencingBounds(bounds);
        root.addChild(light);

        OrbitBehavior orbit = new OrbitBehavior(cv, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
        return root;
    }

    @Override
    public void dispose() {
        if (composite != null && !composite.isDisposed()) {
            composite.dispose();
            composite = null;
        }
        super.dispose();
    }

    @Override
    public void setFocus() {
        CodeForestUIPlugin.ui(project, this, "mouse plugin focus");
        if (cv != null) {
            cv.requestFocus();
        }
    }

    public static String getSecondaryViewId(IProject project) {
        return project.getName() + " - " + "Code Forest Mouse View";
    }
}