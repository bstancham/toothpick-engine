package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncoding;
import java.awt.Color;

public class ColorSmoothColor implements ColorGetter {

    protected Color col;
    protected int rdir;
    protected int gdir;
    protected int bdir;
    protected int r;
    protected int g;
    protected int b;
    protected int min = 0;
    protected int max = 255;
    protected double changeFreq = 0.01;
    // double step = 0.002;

    public ColorSmoothColor() {
        rdir = (Math.random() < 0.5 ? -1 : 1);
        gdir = (Math.random() < 0.5 ? -1 : 1);
        bdir = (Math.random() < 0.5 ? -1 : 1);
        r = ColorGetter.rand255();
        g = ColorGetter.rand255();
        b = ColorGetter.rand255();
        col = new Color(r, g, b);
    }

    @Override
    public Color get() {

        if (Math.random() < changeFreq) rdir = -rdir;
        if (Math.random() < changeFreq) gdir = -gdir;
        if (Math.random() < changeFreq) bdir = -bdir;

        r += rdir;
        g += gdir;
        b += bdir;

        if (r > max) {
            r = max;
            rdir = -1;
        } else if (r < min) {
            r = min;
            rdir = 1;
        }

        if (g > max) {
            g = max;
            gdir = -1;
        } else if (g < min) {
            g = min;
            gdir = 1;
        }

        if (b > max) {
            b = max;
            bdir = -1;
        } else if (b < min) {
            b = min;
            bdir = 1;
        }

        return new Color(r, g, b);
    }

    public double getChangeFrequency() {
        return changeFreq;
    }

    public void setChangeFrequency(double val) {
        changeFreq = val;
    }

    public ColorGetter copy() {
        return new ColorSmoothColor();
    }

    @Override
    public TPEncoding getEncoding() {
        return new TPEncoding();
    }

}
