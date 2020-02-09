package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPProgram;

public class Spawning implements ActorBehaviour, TPEncodingHelper {

    private TPActor archetype = null;
    private double relativeAngle = 0;
    private int interval = 300;
    private int count = 0;

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
            // set position, angle, inertia
            spawn.x = a.x;
            spawn.y = a.y;
            spawn.angle = a.angle + relativeAngle * Math.PI;
            double speed = 2;
            spawn.xInertia = Math.sin(a.angle * Math.PI) * speed;
            spawn.yInertia = -(Math.cos(a.angle * Math.PI) * speed);
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
     * @param val The angle of the spawn (child actor) relative to the angle of the parent
     * actor. 1.0 means a half turn,  0.5 means a quarter turn.
     */
    public void setRelativeAngle(double val) {
        relativeAngle = val;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Double.class, relativeAngle, "setRelativeAngle");
        params.addMethod(Integer.class, interval, "setInterval");
        params.addMethod(TPActor.class, archetype, "setArchetype");
        return params;
    }

}
