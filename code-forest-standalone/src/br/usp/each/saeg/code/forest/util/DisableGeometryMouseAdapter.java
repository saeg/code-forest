package br.usp.each.saeg.code.forest.util;

import java.awt.event.*;
import javax.media.j3d.*;
import br.usp.each.saeg.code.forest.primitive.*;
import com.sun.j3d.utils.picking.*;


public class DisableGeometryMouseAdapter extends MouseAdapter {

    private final PickCanvas pick;

    public DisableGeometryMouseAdapter(Canvas3D canvas, BranchGroup bg) {
        pick = new PickCanvas(canvas, bg);
        pick.setMode(PickCanvas.GEOMETRY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3) {
            return;
        }

        pick.setShapeLocation(e);
        PickResult result = pick.pickClosest();
        if (result != null) {
            if (result.getNode(PickResult.PRIMITIVE) instanceof CodeInformation) {
                CodeInformation info = (CodeInformation) result.getNode(PickResult.PRIMITIVE);
                info.getGeometry().changeStatus();
            }
        }
    }

}
