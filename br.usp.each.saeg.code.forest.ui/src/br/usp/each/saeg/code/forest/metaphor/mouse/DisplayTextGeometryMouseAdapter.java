package br.usp.each.saeg.code.forest.metaphor.mouse;

import java.awt.event.*;
import javax.media.j3d.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.Leaf;
import br.usp.each.saeg.code.forest.metaphor.building.blocks.*;
import br.usp.each.saeg.code.forest.ui.*;
import com.sun.j3d.utils.picking.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class DisplayTextGeometryMouseAdapter extends MouseAdapter {

    private final PickCanvas pick;
    private Object parent;
    private IProject project;

    public DisplayTextGeometryMouseAdapter(IProject project, Object parent, Canvas3D canvas, BranchGroup bg) {
        this.parent = parent;
        this.project = project;
        pick = new PickCanvas(canvas, bg);
        pick.setMode(PickCanvas.GEOMETRY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        pick.setShapeLocation(e);

        PickResult result = pick.pickClosest();
        if (result != null) {
            if (result.getNode(PickResult.PRIMITIVE) instanceof CodeInformation) {
                CodeInformation info = (CodeInformation) result.getNode(PickResult.PRIMITIVE);
                CodeGeometry geom = info.getGeometry();
                if (geom instanceof Leaf) {
                    Leaf leaf = ((Leaf) geom);
                    OpenEditor.at(leaf.getData().getMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest S @ " + leaf.getData().getLogLine());

                } else if (geom instanceof Branch) {
                    Branch branch = ((Branch) geom);
                    OpenEditor.at(branch.getData().getOpenMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest M @ " + branch.getData().getLogLine());

                } else if (geom instanceof Trunk) {
                    Trunk trunk = ((Trunk) geom);
                    OpenEditor.at(trunk.getData().getOpenMarker());
                    CodeForestUIPlugin.ui(project, parent, "Forest C @ " + trunk.getData().getLogLine());
                }
            }
        }
    }
}
