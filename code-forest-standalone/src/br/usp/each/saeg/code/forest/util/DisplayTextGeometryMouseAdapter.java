package br.usp.each.saeg.code.forest.util;

import java.awt.event.*;

import javax.media.j3d.*;

import br.usp.each.saeg.code.forest.gui.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.primitive.*;

import com.sun.j3d.utils.picking.*;


public class DisplayTextGeometryMouseAdapter extends MouseAdapter {

    private final PickCanvas pick;
    private final SourcePanel panel;

    public DisplayTextGeometryMouseAdapter(Canvas3D canvas, BranchGroup bg, SourcePanel panel) {
        pick = new PickCanvas(canvas, bg);
        pick.setMode(PickCanvas.GEOMETRY);
        this.panel = panel;
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
                Trunk parent = info.getGeometry().getTrunk();
                SourceCode code = new SourceCode(parent);
                panel.getTextArea().setText(code.getText());
                panel.getTextArea().setCaretPosition(code.getLinePosition(info.getGeometry()));

                for (int i = 0; i < code.getScores().size(); i++) {
                    try {
                        panel.getTextArea().addLineHighlight(i, code.getScores().get(i));
                        panel.setArrow(panel.getTextArea().getCaretLineNumber());
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
