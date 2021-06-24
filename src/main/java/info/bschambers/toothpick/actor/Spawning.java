package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Pt;

public class Spawning implements ActorBehaviour, TPEncodingHelper {

    private TPActor archetype = null;
    private double relativeAngle = 0;
    private double relativeRotation = 0;
    private int interval = 300;
    private int count = 0;

    // if origin is not null, pick random point on line
    private TPLink origin = null;

    @Override
    public void update(TPProgram prog, TPActor a) {
        count++;
        if (count > interval) {
            count = 0;
            // make spawn, using default if archetype is not set
            TPActor spawn = null;
            if (archetype == null)
                spawn = new TPActor(TPFactory.singleLineFormHoriz(50));
            else
                spawn = archetype.copy();
            // get origin position
            Pt p = new Pt(a.x, a.y);
            if (origin != null)
                p = Geom.randPoint(origin);
            // set position, angle, inertia
            spawn.x = p.x;
            spawn.y = p.y;
            spawn.angle = a.angle + relativeRotation;
            double speed = 2;
            spawn.xInertia = Math.cos(a.angle + relativeAngle) * speed;
            spawn.yInertia = Math.sin(a.angle + relativeAngle) * speed;
            spawn.setBoundaryBehaviour(TPActor.BoundaryBehaviour.DIE_AT_BOUNDS);
            a.addChild(spawn);
        }
    }

    public void setArchetype(TPActor a) { archetype = a.copy(); }

    public void setInterval(int interval) { this.interval = interval; }

    /**
     * @param val The direction to shoot spawn (child actor) out in radians, relative to
     * the angle of the parent actor.
     */
    public void setRelativeAngle(double relativeAngle) { this.relativeAngle = relativeAngle; }

    /**
     * @param val The angle of the spawn (child actor) in radians, relative to the angle
     * of the parent actor.
     */
    public void setRelativeRotation(double relativeRotation) { this.relativeRotation = relativeRotation; }

    public void setOrigin(TPLink origin) { this.origin = origin; }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Double.class, relativeAngle, "setRelativeAngle");
        params.addMethod(Double.class, relativeRotation, "setRelativeRotation");
        params.addMethod(Integer.class, interval, "setInterval");
        params.addMethod(TPActor.class, archetype, "setArchetype");
        return params;
    }

}
