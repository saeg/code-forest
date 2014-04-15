package domain;

/**
 * @author dmutti@gmail.com
 */
public class LeafDistribution {

    private double distance;
    private double offset;
    private int blocks;

    public LeafDistribution(int size, MethodDescription desc, int maxBlock) {
        int halfLeaves = Math.max(desc.getScoreOfEvenLoc().size(), desc.getScoreOfOddLoc().size());

        offset = Math.floor(size * .25);
        for (distance = size / 2; distance >= size / 8; distance -= Math.log1p(distance)) {
            for (blocks = 1; blocks < maxBlock; blocks++) {
                double lineLength = ((halfLeaves-1) * distance) + offset;
                if (((int) Math.ceil(lineLength / size)) <= blocks) {
                    return;
                }
            }
        }
        blocks = maxBlock;
        distance = (blocks * size - offset) / (halfLeaves);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }
}
