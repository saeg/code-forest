package representation;

import java.awt.*;

/**
 * @author dmutti@gmail.com
 */
public class StatementRepresentation {

    private float score;
    private Color color;
    private String loc;

    public StatementRepresentation(float score, String loc) {
        this.score = score;
        this.loc = loc;
        this.color = calculate();
    }

    public Color getColor() {
        return color;
    }

    public String getLoc() {
        return loc;
    }

    private Color calculate() {
        Color start = Color.red;
        Color end = Color.green;

        float[] startHSB = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), null);

        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 2;

        float hueMax = 0;
        float hueMin = 0;
        if (startHSB[0] > endHSB[0]) {
            hueMax = startHSB[0];
            hueMin = endHSB[0];
        } else {
            hueMin = startHSB[0];
            hueMax = endHSB[0];
        }

        float hue = ((hueMax - hueMin) * score) + hueMin;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
