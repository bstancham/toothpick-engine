package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public class TPController {

    protected Pt pos = Pt.ZERO;
    protected double angle = 0.0;

    public Pt position() { return pos; }

    public double angle() { return angle; }

    public void update() {}

}
