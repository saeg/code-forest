package br.usp.each.saeg.code.forest.metaphor.square.assembler;

import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.util.*;

public class SquareForest implements Forest {

    static final int RADIUS = 1;
    private final ForestRestrictions restrictions = new ForestRestrictions();
    private List<SquareAssembler> matrix = new ArrayList<SquareAssembler>();
    private List<TransformGroup> tgs = new ArrayList<TransformGroup>();
    private List<Trunk> trunks = new ArrayList<Trunk>();

    public SquareForest(List<TreeData> arg) {
        Collections.sort(arg);
        float xSize = 0;
        for (TreeData data : arg) {
            Trunk tr = new Trunk(data, RADIUS, 3f, restrictions);
            trunks.add(tr);
            xSize += tr.getLinearSize();
        }
        xSize = xSize + ((arg.size() + 1) * RADIUS);
        float idealX = (float) (xSize / Math.sqrt(arg.size()));

        for (int i = 0; i < trunks.size(); i++) {
            SquareAssembler current = matrix.isEmpty() ? null : matrix.get(matrix.size()-1);
            if (null == current) {
                current = new SquareAssembler(idealX);
                matrix.add(current);
            }
            if (!current.add(trunks.get(i))) {
                matrix.add(new SquareAssembler(idealX));
                i--;
            }
        }
        debugMatrix();
        positionRows();
    }

    private void positionRows() {
        for (int i = 0; i < matrix.size(); i++) {
            SquareAssembler row = matrix.get(i);
            List<Trunk> trunks = row.getTrunk();
            float maxSize = getMaxX();
            float separator = row.getSeparatorSpace(maxSize);

            double rowZ = (-getZ() + (row.getZ() * i) + 1); //ok

            for (int j = 0; j < trunks.size(); j++) {
                Transform3D tr = new Transform3D();
                double trunkX = -getX() + (separator * (j + 1)) + row.getTrunk().get(j).maxLeftBranchSize() + (2 * 1) + row.sizeUntilTree(j);

                Vector3d position = new Vector3d(trunkX, 0, rowZ);
                tr.setTranslation(position);
                TransformGroup tg = new TransformGroup(tr);
                tg.addChild(trunks.get(j).getTransformGroup());
                tgs.add(tg);
            }
        }
    }

    private float getMaxX() {
        List<Float> xs = new ArrayList<Float>();
        for (SquareAssembler row : matrix) {
            xs.add(row.getX());
        }
        return CollectionUtils.max(xs);
    }

    void debugMatrix() {
        for (int i = 0; i < matrix.size(); i++) {
            SquareAssembler list = matrix.get(i);
            List<Float> scores = new ArrayList<Float>();
            for (Trunk tr : list.getTrunk()) {
                scores.add(tr.getData().getScore());
            }
            System.out.println(i + " - " + scores);
        }
    }

    @Override
    public void applyForestRestrictions() {
        for (Trunk trunk : getTrunks()) {
            trunk.refresh();
        }
    }

    @Override
    public List<TransformGroup> getTrees() {
        return tgs;
    }

    @Override
    public float getZ() {
        float z = 0;
        for (SquareAssembler row : matrix) {
            z += row.getZ();
        }

        return z/2;
    }

    @Override
    public float getX() {
        return getMaxX()/2;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public List<Trunk> getTrunks() {
        return trunks;
    }

    @Override
    public ForestRestrictions getRestrictions() {
        return restrictions;
    }
}
