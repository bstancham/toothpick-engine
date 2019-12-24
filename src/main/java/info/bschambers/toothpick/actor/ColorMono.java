package info.bschambers.toothpick.actor;

import java.awt.Color;

public class ColorMono implements ColorGetter {

    protected Color col;

    public ColorMono() {
        this(ColorGetter.randColor());
    }

    public ColorMono(Color c) {
        col = c;
    }

    @Override
    public Color get() {
        return col;
    }

    @Override
    public ColorGetter copy() {
        return new ColorMono(col);
    }

}
