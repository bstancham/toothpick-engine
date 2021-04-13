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

        // System.out.println("SeekNearest.update()");

        if (target == null || !target.isAlive()) {
            // try to acquire new target
            target = prog.getNearest(a, wrapAtBounds);
        }

        if (target != null && target.isAlive()) {

            double x = target.x;
            double y = target.y;

            // wrap around if required

            if (wrapAtBounds) {

                double xDist = Math.abs(a.x - target.x);
                double xDistWrapped = prog.getGeometry().getWidth() - xDist;
                if (xDistWrapped < xDist)
                    x = a.x + (a.x < target.x ? -xDistWrapped : xDistWrapped);

                double yDist = Math.abs(a.y - target.y);
                double yDistWrapped = prog.getGeometry().getHeight() - yDist;
                if (yDistWrapped < yDist)
                    y = a.y + (a.y < target.y ? -yDistWrapped : yDistWrapped);

            }

            double angle = Geom.angle(a.x, a.y, x, y);
            angle += Math.PI; // 180 degree rotation
            a.xInertia = Math.cos(angle) * speed;
            a.yInertia = Math.sin(angle) * speed;
        }
    }

}
