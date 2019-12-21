package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public class SimpleController extends TPController {

    protected Pt heading;
    protected double spin;

    public SimpleController(Pt heading, double spin) {
        this.heading = heading;
        this.spin = spin;
    }

    @Override
    public void update() {
        pos = pos.add(heading);
        angle += spin;
    }

}
