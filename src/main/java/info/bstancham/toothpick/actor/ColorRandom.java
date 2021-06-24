package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncoding;
import java.awt.Color;

public class ColorRandom implements ColorGetter {

    @Override
    public Color get() {
        return ColorGetter.randColor();
    }

    @Override
    public ColorGetter copy() {
        return this;
    }

    @Override
    public TPEncoding getEncoding() {
        return new TPEncoding();
    }

}
