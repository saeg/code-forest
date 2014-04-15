package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;

import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.util.*;


public class ColumnAssembler {

    private double maxLeft;
    private double maxRight;
    private List<Trunk> trunk = new ArrayList<Trunk>();
    private double maxRadius;

    public ColumnAssembler(List<TreeData> data, float radius, float height, ForestRestrictions restrictions) {
        for (TreeData tree : data) {
            trunk.add(new Trunk(tree, radius, height, restrictions));
        }
        findMax();
    }

    private void findMax() {
        List<Double> left = new ArrayList<Double>();
        List<Double> right = new ArrayList<Double>();
        List<Float> radius = new ArrayList<Float>();
        for (Trunk each : trunk) {
            left.add(each.maxLeftBranchSize());
            right.add(each.maxRightBranchSize());
            radius.add(each.getRadius());
        }
        maxLeft = CollectionUtils.max(left);
        maxRight = CollectionUtils.max(right);
        maxRadius = CollectionUtils.max(radius);
    }

    public double getMaxLeft() {
        return maxLeft;
    }

    public double getMaxRight() {
        return maxRight;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public List<Trunk> getTrunk() {
        return trunk;
    }
}
