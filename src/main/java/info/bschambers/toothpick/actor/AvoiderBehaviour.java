package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Pt;

/**
 * Causes TPActor to avoid the nearest other TPActor.
 */
public class AvoiderBehaviour implements ActorBehaviour {

    private TPActor target = null;
    private double speed = 0.25;
    private boolean wrapAtBounds = true;

    public AvoiderBehaviour() {
        this(true);
    }

    public AvoiderBehaviour(boolean wrapAtBounds) {
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
            angle += Math.PI; // 180 degree rotation
            a.xInertia = Math.cos(angle) * speed;
            a.yInertia = Math.sin(angle) * speed;
        }
    }

}
