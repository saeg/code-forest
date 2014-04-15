package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class LeafDistribution {

    private final Random random = new Random();
    private double distance;
    private double size;

    public LeafDistribution(BranchData data, Trunk trunk) {
        size = trunk.getHeight() * ((float) Math.max(1, data.getProportionOfCoveredLoCs())/100);//TODO mudar para score???
        int halfLeaves = Math.max(data.getScoreOfCoveredEvenLoc().size(), data.getScoreOfCoveredOddLoc().size());
        int branchFactor = 1;

        double minIdealDistance = size * .08;
        double maxDistance = (size - getOffset()) / halfLeaves;

        double[] candidateSize = new double[branchFactor];
        double[] candidateDist = new double[branchFactor];

        for (int i = 0; i < branchFactor; i++) {
            candidateSize[i] = (size) * (i+1)/(branchFactor);
            candidateDist[i] = (candidateSize[i] * Branch.OUTSIDE) / halfLeaves;
        }

        List<Integer> idx = new ArrayList<Integer>();
        for (int i = 0; i < branchFactor; i++) {
            if ((Double.compare(candidateDist[i], minIdealDistance) >= 0) && (Double.compare(candidateDist[i], maxDistance) <= 0)) {
                idx.add(i);
            }
        }

        if (idx.isEmpty()) {
            distance = maxDistance / 2.1;
            return;
        }

        if (halfLeaves == 0) {
            distance = candidateDist[idx.get(0)] / 2.1;
            size = candidateSize[idx.get(0)];

        } else {
            Collections.shuffle(idx);
            int pos = idx.size() <= 1 ? 0 : random.nextInt(idx.size()-1);
            distance = candidateDist[idx.get(pos)] / 2.1;
            size = candidateSize[idx.get(pos)];
        }
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
