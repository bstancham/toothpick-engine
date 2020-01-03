package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.TPProgram;

public class DieAtBounds implements TPBehaviour {

    @Override
    public void update(TPProgram prog, TPActor a) {
        TPGeometry geom = prog.getGeometry();
        if (a.x < 0 ||
            a.x > geom.getWidth() ||
            a.y < 0 ||
            a.y > geom.getHeight())
            // if form is empty then actor will die
            a.setForm(TPForm.NULL);
    }

}
