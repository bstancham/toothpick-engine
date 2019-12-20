package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public class SimpleController extends TPController {

    protected Pt heading;

    public SimpleController(Pt heading) {
        this.heading = heading;
    }

    @Override
    public void update() {
        pos = pos.add(heading);
    }

}
