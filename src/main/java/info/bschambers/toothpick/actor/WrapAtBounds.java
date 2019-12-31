package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.TPProgram;

public class WrapAtBounds implements TPBehaviour {

    @Override
    public void update(TPProgram prog, TPActor a) {
        TPGeometry geom = prog.getGeometry();
        if (a.x < 0)
            a.x = geom.getWidth();
        else if (a.x > geom.getWidth())
            a.x = 0;
        else if (a.y < 0)
            a.y = geom.getHeight();
        else if (a.y > geom.getHeight())
            a.y = 0;
    }

}
