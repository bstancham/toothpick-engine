package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import java.awt.Color;

public class ColorMono implements ColorGetter {

    protected Color col;

    public ColorMono() {
        this(ColorGetter.randColor());
    }

    public ColorMono(Color c) {
        col = c;
    }

    public void setBaseColor(Color c) {
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

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Color.class, col, "setBaseColor");
        return params;
    }

}
