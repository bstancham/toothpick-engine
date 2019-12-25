package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Rect;

public class WrapAtBounds implements TPBehaviour {

    @Override
    public void update(TPProgram prog, TPActor a) {
        Rect bounds = prog.getBounds();
        if (a.x < bounds.x1)
            a.x = bounds.x2;
        else if (a.x > bounds.x2)
            a.x = bounds.x1;
        else if (a.y < bounds.y1)
            a.y = bounds.y2;
        else if (a.y > bounds.y2)
            a.y = bounds.y1;
    }

}
