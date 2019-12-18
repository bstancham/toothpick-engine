package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public class ActorController {

    private Pt pos = Pt.ZERO;
    private double angle = 0.0;

    public Pt position() { return pos; }

    public double angle() { return angle; }

}
