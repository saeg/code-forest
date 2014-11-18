package br.usp.each.saeg.code.forest.ui.views;

import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import org.eclipse.core.resources.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import swingintegration.example.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.collision.*;
import br.usp.each.saeg.code.forest.metaphor.mouse.*;
import br.usp.each.saeg.code.forest.metaphor.util.*;
import br.usp.each.saeg.code.forest.ui.project.*;
import com.sun.j3d.utils.universe.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CodeForestKeyboardView extends ViewPart {

    public static final String VIEW_ID = "br.usp.each.saeg.code.forest.menu.view.code.forest.keyboard";
    private Canvas3D cv;
    private SimpleUniverse su;
    private EmbeddedSwingComposite composite;
    private FilterPanel filterPanel;
    private IProject project;
    private ProjectState state;

    /**
     * http://www.eclipse.org/swt/snippets/#awt
     * http://www.eclipse.org/articles/article.php?file=Article-Swing-SWT-Integration/index.html
     * http://www.vogella.com/articles/EclipseJobs/article.html
     */
    @Override
    public void createPartControl(Composite parent) {
        try {
            System.setProperty("sun.awt.noerasebackground", "true");
        } catch (NoSuchMethodError error) {
        }

        project = ProjectUtils.getCurrentSelectedProject();
        if (project == null) {
            return;
        }
        state = ProjectPersistence.getStateOf(project);
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
                return createContent(new SquareForest(state.getAllAnalysis()), state);
            }
        };
        composite.populate();
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } while (cv == null);
        cv.requestFocus();
    }

    private JPanel createContent(final Forest forest, ProjectState state) {

        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        cv = new Canvas3D(gc);

        ViewingPlatform vp = new ViewingPlatform();
        vp.setCapability(ViewingPlatform.ALLOW_BOUNDS_WRITE);
        Viewer viewer = new Viewer(cv);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(cv, BorderLayout.CENTER);
        su = new SimpleUniverse(vp, viewer);

        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(forest.getX() * 1.5);

        BranchGroup bg = createSceneGraph(forest, bounds);

        final Vector3d translation = new Vector3d();
        final float maxX = forest.getX() / 3;
        final float maxY = forest.getY() / 8;
        final float maxZ = forest.getZ() / 5;
        final Vector3f initialPosition = new Vector3f(0, 1, 20);

        TransformGroup vpTransformGroup = vp.getMultiTransformGroup().getTransformGroup(0);
        Transform3D vpTransform3D = new Transform3D();
        vpTransformGroup.getTransform(vpTransform3D);
        vpTransform3D.setTranslation(initialPosition);
        vpTransformGroup.setTransform(vpTransform3D);

        filterPanel = new FilterPanel(project, forest, cv, this);
        KeyCollisionBehavior keyBehavior = new KeyCollisionBehavior(project, filterPanel, vpTransformGroup, initialPosition, new CollisionDetector() {
            @Override
            public boolean isCollision(Transform3D t3d, boolean bViewSide) {
                t3d.get(translation);
                return translation.getY() < .3 || Math.abs(translation.getX()) > maxX || Math.abs(translation.getZ()) > initialPosition.getZ() + maxZ || Math.abs(translation.getY()) > maxY;
            }
        }, this);
        keyBehavior.setSchedulingBounds(bounds);
        bg.addChild(keyBehavior);
        bg.compile();
        su.addBranchGraph(bg);
        cv.addMouseListener(new DisplayTextGeometryMouseAdapter(project, this, cv, bg));

        panel.add(filterPanel, BorderLayout.EAST);
        panel.add(new ScriptPanel(project, this, state, filterPanel), BorderLayout.SOUTH);
        return panel;
    }

    private BranchGroup createSceneGraph(Forest forest, Bounds bounds) {
        BranchGroup root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_DETACH);

        Transform3D transform3d = new Transform3D();
        transform3d.setScale(.1);
        TransformGroup transformGroup = new TransformGroup(transform3d);

        transformGroup.addChild(Terrain.getFullTerrain());

        for (TransformGroup tg : forest.getTrees()) {
            transformGroup.addChild(tg);
        }
        root.addChild(transformGroup);

        Background background = ImageUtils.getBackground();
        background.setApplicationBounds(bounds);
        root.addChild(background);

        PointLight light = new PointLight(new Color3f(Color.white), new Point3f(1f, 1f, 1f), new Point3f(1f, 0.1f, 0f));
        light.setInfluencingBounds(bounds);
        root.addChild(light);
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
        if (filterPanel != null) {
            filterPanel.applyFilter(state.getScriptFilter());
        }
    }

    public static String getSecondaryViewId(IProject project) {
        return project.getName() + " - " + "Code Forest View";
    }

}
