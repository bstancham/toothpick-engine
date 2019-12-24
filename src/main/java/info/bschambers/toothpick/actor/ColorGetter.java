package info.bschambers.toothpick.actor;

import java.awt.Color;

public interface ColorGetter {

    Color get();

    ColorGetter copy();

    public static Color randColor() {
        return new Color(rand255(), rand255(), rand255());
    }

    public static int rand255() {
        return (int) (Math.random() * 256);
    }

    public static Color setBrightness(Color c, double brightness) {
        int r = (int) (c.getRed() * brightness);
        int g = (int) (c.getGreen() * brightness);
        int b = (int) (c.getBlue() * brightness);
	if (r > 255) r = 255;
	if (g > 255) g = 255;
	if (b > 255) b = 255;
        return new Color(r, g, b);
    }

}
