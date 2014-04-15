package br.usp.each.saeg.code.forest.metaphor;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public enum Size {

    BIG(1F, 3F, .66F), NORMAL(.66F, 1.8F, .33F), SMALL(.5F, 1.5F, .33F), EXTRA_SMALL(.33F, 1F, .33F);

    private final float radius;
    private final float step;
    private final float maxHorizontal;


    Size(float radius, float height, float maxHorizontal) {
        this.radius = radius;
        this.step = height;
        this.maxHorizontal = maxHorizontal;
    }

    public float getRadius() {
        return radius;
    }
    public float getStep() {
        return step;
    }
    public float getMaxHorizontal() {
        return maxHorizontal;
    }

    public static Size from(float score) {
        if (score >= .85F) {
            return BIG;
        }
        if (score >= .65F) {
            return NORMAL;
        }
        if (score >= .45) {
            return SMALL;
        }
        return EXTRA_SMALL;
    }
}
