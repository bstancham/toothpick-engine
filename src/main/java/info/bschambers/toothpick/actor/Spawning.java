package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class Spawning implements TPBehaviour {

    private TPActor archetype = null;
    private int interval = 300;
    private int count = 0;
    private double relativeAngle = 0;

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

            spawn.x = a.x;
            spawn.y = a.y;
            spawn.angle = a.angle + relativeAngle * Math.PI;
            double speed = 2;
            spawn.xInertia = Math.sin(a.angle * Math.PI) * speed;
            spawn.yInertia = -(Math.cos(a.angle * Math.PI) * speed);
            spawn.addBehaviour(new DieAtBounds());
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

}
