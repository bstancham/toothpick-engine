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
    private TPLine originLine = null;

    @Override
    public void update(TPProgram prog, TPActor a) {
        count++;
        if (count > interval) {
            count = 0;
            // make spawn, using default if archetype is not set
            TPActor spawn = null;
            if (archetype == null)
                spawn = new TPActor(TPFactory.singleLineForm(50));
            else
                spawn = archetype.copy();
            // get origin position
            Pt p = new Pt(a.x, a.y);
            if (originLine != null)
                p = Geom.randPointOnLine(originLine.getLine());
            // set position, angle, inertia
            spawn.x = p.x;
            spawn.y = p.y;
            spawn.angle = a.angle + relativeRotation * Math.PI;
            double speed = 2;
            spawn.xInertia = Math.sin((a.angle + relativeAngle) * Math.PI) * speed;
            spawn.yInertia = -(Math.cos((a.angle + relativeAngle) * Math.PI) * speed);
            spawn.setBoundaryBehaviour(TPActor.BoundaryBehaviour.DIE_AT_BOUNDS);
            a.addChild(spawn);
        }
    }

    public void setArchetype(TPActor a) {
        archetype = a.copy();
    }

    public void setInterval(int val) {
        interval = val;
    }

    /**
     * @param val The direction to shoot spawn (child actor) out, relative to the angle of
     * the parent actor. 1.0 means a half turn, 0.5 means a quarter turn.
     */
    public void setRelativeAngle(double val) {
        relativeAngle = val;
    }

    /**
     * @param val The angle of the spawn (child actor) relative to the angle of the parent
     * actor. 1.0 means a half turn,  0.5 means a quarter turn.
     */
    public void setRelativeRotation(double val) {
        relativeRotation = val;
    }

    public void setOriginLine(TPLine tpl) {
        originLine = tpl;
    }

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
