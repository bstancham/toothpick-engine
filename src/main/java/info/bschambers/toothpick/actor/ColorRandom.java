package info.bschambers.toothpick.actor;

import java.awt.Color;

public class ColorRandom implements ColorGetter {

    @Override
    public Color get() { return ColorGetter.randColor(); }

    @Override
    public ColorGetter copy() { return this; }

}
