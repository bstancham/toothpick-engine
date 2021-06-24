package info.bstancham.toothpick.actor;

import java.awt.Color;

public class ColorRandomMono extends ColorMono {

    @Override
    public Color get() {
        return ColorGetter.setBrightness(col, Math.random());
    }

    @Override
    public ColorGetter copy() {
        ColorRandomMono getter = new ColorRandomMono();
        getter.col = col;
        return getter;
    }

}
