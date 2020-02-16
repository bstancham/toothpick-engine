package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Pt;

public class PointAnchor implements ActorBehaviour {

    public void update(TPProgram prog, TPActor a) {
        // testing: anchor to start of first line in parent
        if (a.getParent() != null) {
            TPForm form = a.getParent().getForm();
            for (int i = 0; i < form.numParts(); i++) {
                if (form.getPart(i) instanceof TPLine) {
                    Pt p = ((TPLine) form.getPart(i)).getLine().start;
                    a.x = p.x;
                    a.y = p.y;
                }
            }
        }
    }

}
