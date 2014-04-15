package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;

public class LeafDistribution {

    private final Random random = new Random();
    private double distance;
    private double size;

    public LeafDistribution(BranchData data, double height) {
        size = height / Branch.PROPORTION;
        int halfLeaves = Math.max(data.getScoreOfEvenLoc().size(), data.getScoreOfOddLoc().size());

        double minIdealDistance = size * .05;
        double maxDistance = (size - getOffset()) / halfLeaves;

        double[] candidateSize = new double[20];
        double[] candidateDist = new double[20];

        for (int i = 0; i < 20; i++) {
            candidateSize[i] = (size) * (i+1)/20f;
            candidateDist[i] = (candidateSize[i] * Branch.OUTSIDE) / halfLeaves;
        }

        List<Integer> idx = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            if ((Double.compare(candidateDist[i], minIdealDistance) >= 0) && (Double.compare(candidateDist[i], maxDistance) <= 0)) {
                idx.add(i);
            }
        }

        if (idx.isEmpty()) {
            distance = maxDistance / 2;
            return;
        }

        Collections.shuffle(idx);
        int pos = idx.size() <= 1 ? 0 : random.nextInt(idx.size()-1);
        distance = candidateDist[idx.get(pos)] / 2;
        size = candidateSize[idx.get(pos)];
    }

    public double getDistance() {
        return distance;
    }

    public double getOffset() {
        return size * (1 - Branch.OUTSIDE) * 4;
    }

    public double getSize() {
        return size;
    }
}
