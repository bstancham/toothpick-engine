package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Pt;

/**
 * Causes TPActor to seek the nearest other TPActor.
 */
public class SeekerBehaviour implements ActorBehaviour {

    private TPActor target = null;
    private double speed = 0.25;
    private boolean wrapAtBounds = true;

    public SeekerBehaviour() {
        this(true);
    }

    public SeekerBehaviour(boolean wrapAtBounds) {
        this.wrapAtBounds = wrapAtBounds;
    }

    @Override
    public void update(TPProgram prog, TPActor a) {

        if (target == null || !target.isAlive()) {
            // try to acquire new target
            target = prog.getNearest(a, wrapAtBounds);
        }

        if (target != null && target.isAlive()) {
            double angle = 0;
            if (wrapAtBounds) {
                double x = prog.getGeometry().xDistWrapped(a.x, target.x);
                double y = prog.getGeometry().yDistWrapped(a.y, target.y);
                angle = Geom.angle(0, 0, x, y);
            } else {
                angle = Geom.angle(a.x, a.y, target.x, target.y);
            }
            a.xInertia = Math.cos(angle) * speed;
            a.yInertia = Math.sin(angle) * speed;
        }
    }

}
