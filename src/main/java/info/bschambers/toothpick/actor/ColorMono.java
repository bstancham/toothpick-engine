package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import java.awt.Color;

public class ColorMono implements ColorGetter {

    public static final ColorMono WHITE = new ColorMono(Color.WHITE);
    public static final ColorMono BLACK = new ColorMono(Color.BLACK);
    public static final ColorMono RED = new ColorMono(Color.RED);
    public static final ColorMono GREEN = new ColorMono(Color.GREEN);
    public static final ColorMono BLUE = new ColorMono(Color.BLUE);
    public static final ColorMono CYAN = new ColorMono(Color.CYAN);
    public static final ColorMono MAGENTA = new ColorMono(Color.MAGENTA);
    public static final ColorMono YELLOW = new ColorMono(Color.YELLOW);

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
