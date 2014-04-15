package br.usp.each.saeg.code.forest.metaphor.linear.assembler;

import java.util.*;

import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;


public class LinearAssembler {

    private List<Trunk> trunk = new ArrayList<Trunk>();
    private float x;
    private float z;
    private float minSeparator;
    private float radius;

    public LinearAssembler(List<TreeData> data, float radius, float height, ForestRestrictions restrictions) {
        this.radius = radius;
        z = 4 * radius;
        for (TreeData tree : data) {
            Trunk tr = new Trunk(tree, radius, height, restrictions);
            trunk.add(tr);
            x += tr.maxLeftBranchSize() + (2 * tr.getRadius()) + tr.maxRightBranchSize();
        }
        minSeparator = ((trunk.size() + 1) * radius);//minimo espaco entre 2 arvores eh de 1 raio. tem um espaco antes e um depois de cada arvore

    }

    public float sizeUntilTree(int index) {
        float size = 0;
        for (int i = 0; i < index; i++) {
            size += trunk.get(i).maxLeftBranchSize() + (2 * radius) + trunk.get(i).maxRightBranchSize();
        }
        return size;
    }

    public float getX() {
        return x + minSeparator;
    }

    public float getZ() {
        return z;
    }

    public float getSeparatorSpace(float effectiveX) {
        return Math.max(((effectiveX - x) / (trunk.size() + 1)), radius);
    }

    public List<Trunk> getTrunk() {
        return trunk;
    }
}
