package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;

import br.usp.each.saeg.code.forest.metaphor.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class SquareAssembler {

    private List<Trunk> trunk = new ArrayList<Trunk>();
    private float maxHorSize;
    private float currentHorSize;
    private float minSeparator;
    private float z = 8 * SquareForest.RADIUS;


    public SquareAssembler(float maxHorizontalSize) {
        this.maxHorSize = maxHorizontalSize;
    }

    public boolean add(Trunk arg) {
        if (trunk.isEmpty()) {
            currentHorSize += arg.getLinearSize() + (2 * SquareForest.RADIUS);
            trunk.add(arg);
            minSeparator = (trunk.size() + 1) * 8 * SquareForest.RADIUS;
            return true;
        }
        if (Double.compare(currentHorSize + arg.getLinearSize() + (2 * SquareForest.RADIUS), maxHorSize) <= 0) {
            trunk.add(arg);
            minSeparator = (trunk.size() + 1) * 8 * SquareForest.RADIUS;
            currentHorSize += arg.getLinearSize();
            return true;
        }
        return false;
    }

    public float sizeUntilTree(int index) {
        float size = 0;
        for (int i = 0; i < index; i++) {
            size += trunk.get(i).getLinearSize();
        }
        return size;
    }

    public float getSeparatorSpace(float effectiveX) {
        return Math.max(((effectiveX - currentHorSize) / (trunk.size() + 1)), SquareForest.RADIUS);
    }

    public float getX() {
        return currentHorSize + minSeparator;
    }

    public float getZ() {
        return z;
    }

    public List<Trunk> getTrunk() {
        return trunk;
    }
}
