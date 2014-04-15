package br.usp.each.saeg.code.forest.metaphor.linear.assembler;

import java.util.*;
import java.util.Map.Entry;

import javax.media.j3d.*;
import javax.vecmath.*;

import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.util.*;

public class LinearForest implements Forest {

    private List<List<TreeData>> matrix = new ArrayList<List<TreeData>>();
    private final ForestRestrictions restrictions = new ForestRestrictions();
    private List<LinearAssembler> rows = new ArrayList<LinearAssembler>();
    private List<TransformGroup> tgs = new ArrayList<TransformGroup>();

    public LinearForest(List<TreeData> arg) {
        populateMatrix(arg);

        for (List<TreeData> data : matrix) {
            if (null == data || data.isEmpty()) {
                continue;
            }
            LinearAssembler assembler = new LinearAssembler(data, 1, 3f, restrictions);
            rows.add(assembler);
        }
        debugMatrix();
        positionRows();
   }

    private void populateMatrix(List<TreeData> arg) {
        Collections.sort(arg);
        int[] levels = new int[100];
        int[] newLevels = new int[100];

        for (int i = 0; i < arg.size(); i++) {
            int score = getScore(arg.get(i));
            levels[score] = levels[score] + 1;
        }

        float I = (int) Math.ceil(Math.cbrt(arg.size()));

        int sum = 0;
        for (int i = 0; i < levels.length; i++) {
            sum += levels[i];
            newLevels[i] = Math.max(0, (int) (sum/I) -1 );
        }

        Map<Integer, List<TreeData>> histogram = new TreeMap<Integer, List<TreeData>>();
        for (int i = 0; i < arg.size(); i++) {
            int score = getScore(arg.get(i));
            if (!histogram.containsKey(newLevels[score])) {
                List<TreeData> list = new ArrayList<TreeData>();
                list.add(arg.get(i));
                histogram.put(newLevels[score], list);
            } else {
                histogram.get(newLevels[score]).add(arg.get(i));
            }
        }
        for (Entry<Integer, List<TreeData>> each : histogram.entrySet()) {
            Collections.reverse(each.getValue());
            matrix.add(each.getValue());
        }
    }

    private int getScore(TreeData arg) {
        return (int) Math.max(0, Math.min(arg.getScore() * 100, 99));
    }

    void debugMatrix() {
        for (int i = 0; i < matrix.size(); i++) {
            List<TreeData> list = matrix.get(i);
            List<Float> scores = new ArrayList<Float>();
            for (TreeData tr : list) {
                scores.add(tr.getScore());
            }
            System.out.println(i + " - " + scores);
        }
    }

    private void positionRows() {
        float maxSize = getMaxX();
        for (int i = 0; i < rows.size(); i++) {
            LinearAssembler row = rows.get(i);
            List<Trunk> trunks = row.getTrunk();
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
        for (LinearAssembler row : rows) {
            z += row.getZ();
        }

        return z/2;
    }

    @Override
    public float getX() {
        List<Float> xs = new ArrayList<Float>();
        for (LinearAssembler row : rows) {
            xs.add(row.getX());
        }
        return CollectionUtils.max(xs)/2;
    }

    private float getMaxX() {
        List<Float> xs = new ArrayList<Float>();
        for (LinearAssembler row : rows) {
            xs.add(row.getX());
        }
        return CollectionUtils.max(xs);
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public List<Trunk> getTrunks() {
        List<Trunk> result = new ArrayList<Trunk>();
        for (LinearAssembler row : rows) {
            result.addAll(row.getTrunk());
        }
        return result;
    }

    @Override
    public ForestRestrictions getRestrictions() {
        return restrictions;
    }
}
