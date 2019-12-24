package info.bschambers.toothpick.actor;

import java.awt.Color;

public class ColorSmoothMono extends ColorMono {

    protected double bright;
    protected int dir;
    protected double step = 0.002;
    protected double changeFreq = 0.01;

    public ColorSmoothMono() {
        super();
        bright = Math.random();
        dir = (Math.random() < 0.5 ? -1 : 1);
    }

    @Override
    public Color get() {

        if (Math.random() < changeFreq)
            dir = -dir;

        bright += step * dir;

        if (bright > 1.0) {
            bright = 1.0;
            dir = -1;
        } else if (bright < 0.0) {
            bright = 0.0;
            dir = 1;
        }

        return ColorGetter.setBrightness(col, bright);
    }

    public double getChangeFrequency() {
        return changeFreq;
    }

    public void setChangeFrequency(double val) {
        changeFreq = val;
    }

    public ColorGetter copy() {
	ColorMono cm = new ColorSmoothMono();
	cm.col = col;
	return cm;
    }

}
