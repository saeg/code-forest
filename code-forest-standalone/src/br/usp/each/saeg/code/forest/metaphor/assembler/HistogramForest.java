package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;


public class HistogramForest implements Forest {

    private List<TreeData> trees;
    private List<List<TreeData>> matrix;
    private int side;
    private float step;
    private double z;
    private double x;
    private double y = 0;
    private List<ColumnAssembler> columns = new ArrayList<ColumnAssembler>();
    private List<TransformGroup> tgs = new ArrayList<TransformGroup>();
    private final ForestRestrictions restrictions = new ForestRestrictions();

    public HistogramForest(List<TreeData> arg) {
        this.trees = arg;
        Collections.sort(trees);
        side = (int) Math.ceil(Math.sqrt(trees.size()));
        if (trees.isEmpty() || Float.compare(trees.get(0).getScore(), 0) <= 0) {
            step = .2f;
        } else {
            step = (Math.abs(trees.get(Math.min(trees.size()-1, 0)).getScore()) + Math.abs(trees.get(0).getScore())) / side;
        }

        initMatrix();
        populateMatrix();
        sortMatrix();
        shrinkMatrix();

        List<Double> zs = new ArrayList<Double>();
        for (List<TreeData> data : matrix) {
            if (null == data || data.isEmpty()) {
                continue;
            }
            ColumnAssembler assembler = new ColumnAssembler(data, 1, 3f, restrictions);
            x += assembler.getMaxLeft() + assembler.getMaxRight() + (3 * assembler.getMaxRadius());
            zs.add(assembler.getMaxRadius() * assembler.getTrunk().size() * 2);
            columns.add(assembler);
        }
        z = Collections.max(zs);
        positionColumns();
    }

    @Override
    public void applyForestRestrictions() {
        for (Trunk trunk : getTrunks()) {
            trunk.refresh();
        }
    }

    private void shrinkMatrix() {
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).isEmpty()) {
                matrix.remove(i);
            }
        }
    }

    private void initMatrix() {
        matrix = new ArrayList<List<TreeData>>();
        for (int i = 0; i < side; i++) {
            matrix.add(new ArrayList<TreeData>());
        }
    }

    private void populateMatrix() {
        for (TreeData data : trees) {
            int position = (int) Math.floor(data.getScore() / step);
            try {
                matrix.get(position >= matrix.size() ? matrix.size() - 1 : position).add(data);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(data.getScore() + "/" + step + "=" + position);
            }
        }
    }

    private void sortMatrix() {
        Collections.reverse(matrix);
        for (int i = 0; i < side; i++) {
            Collections.sort(matrix.get(i));
        }
    }

    private void positionColumns() {
        for (int i = 0; i < columns.size(); i++) {
            List<Trunk> trunks = columns.get(i).getTrunk();
            double columnX = -getX() + columns.get(i).getMaxLeft() + columns.get(i).getMaxRadius() + previousSize(i);
            for (int j = 0; j < trunks.size(); j++) {
                Transform3D tr = new Transform3D();
                Vector3d position = new Vector3d(columnX, 0, -getZ() + (2 * columns.get(i).getMaxRadius()) + (j * 4 * columns.get(i).getMaxRadius()));
                tr.setTranslation(position);
                TransformGroup tg = new TransformGroup(tr);
                tg.addChild(trunks.get(j).getTransformGroup());
                tgs.add(tg);
            }
        }
    }

    private double previousSize(int index) {
        double size = 0;
        for (int i = 0; i < index; i++) {
            size += columns.get(i).getMaxLeft() + (3 * columns.get(i).getMaxRadius()) + columns.get(i).getMaxRight();
        }
        return size;
    }

    @Override
    public List<TransformGroup> getTrees() {
        return tgs;
    }

    @Override
    public float getZ() {
        return (float) z;
    }

    @Override
    public float getX() {
        return (float) x/2;
    }

    @Override
    public float getY() {
        return (float) y;
    }

    @Override
    public List<Trunk> getTrunks() {
        List<Trunk> result = new ArrayList<Trunk>();
        for (ColumnAssembler col : columns) {
            result.addAll(col.getTrunk());
        }
        return result;
    }

    @Override
    public ForestRestrictions getRestrictions() {
        return restrictions;
    }
}
