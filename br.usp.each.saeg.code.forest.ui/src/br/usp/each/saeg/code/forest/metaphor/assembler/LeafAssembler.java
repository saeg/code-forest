package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.Leaf;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class LeafAssembler {

    public static final float OUTSIDE = 1;
    private List<Leaf> leaves = new ArrayList<Leaf>();

    public LeafAssembler(LeafDistribution distr, Vector3d position, Trunk trunk, List<LeafData> data, Branch branch, ForestRestrictions restrictions) {
        Vector3d previous = null;
        for (int i = 0; i < data.size(); i++) {

            Transform3D tr = new Transform3D();
            int leafSignal = 1;
            int angle;

            Vector3d translation;

            if (up(i)) {
                leafSignal = 1;
                if (left(branch.getSignal())) {
                    angle = 30;
                } else {
                    angle = 330;
                }
                tr.rotZ(Math.toRadians(angle));
                previous = translation = new Vector3d(position.x - (branch.getSignal() * (distr.getSize() / 2)) + (branch.getSignal() * distr.getOffset()) + branch.getSignal() * ((distr.getDistance()) * i), position.y + leafSignal * (trunk.getRadius() / 2), position.z);

            } else {
                leafSignal = -1;
                if (left(branch.getSignal())) {
                    angle = 130;
                } else {
                    angle = 220;
                }
                translation = previous;
                tr.rotZ(Math.toRadians(angle));
                previous.y = position.y + leafSignal * (trunk.getRadius() / 2) * OUTSIDE;
            }

            tr.setTranslation(translation);
            Leaf leaf = new Leaf(branch, trunk.getRadius(), data.get(i), restrictions, tr);
            leaves.add(leaf);
        }
    }

    public List<Leaf> getLeaves() {
        return leaves;
    }

    private boolean up(int i) {
        return i % 2 == 0;
    }

    private boolean left(int i) {
        return i < 0;
    }
}
