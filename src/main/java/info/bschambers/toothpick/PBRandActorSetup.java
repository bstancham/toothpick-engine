package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPFactory;

/**
 * Used as a reset-behaviour - for named actors, setup random position and inertia withing
 * specified bounds.
 */
public class PBRandActorSetup implements ProgramBehaviour {

    private String[] names;

    public double xMin = 0;
    public double xMax = 0;
    public double yMin = 0;
    public double yMax = 0;
    public double xInertiaMin = 0;
    public double xInertiaMax = 0;
    public double yInertiaMin = 0;
    public double yInertiaMax = 0;
    public double angleMin = 0;
    public double angleMax = 0;
    public double angleInertiaMin = 0;
    public double angleInertiaMax = 0;

    @Override
    public PBRandActorSetup copy() { return this; }

    public void setTarget(String targetName) { names = new String[] { targetName }; };

    public void setTargets(String[] names) { this.names = names; };

    public void initBounds(TPGeometry geom) {
        initBoundsWithMargins(geom, 0);
        // xMin = 0;
        // xMax = geom.getWidth();
        // yMin = 0;
        // yMax = geom.getHeight();
    }

    public void initBoundsWithMargins(TPGeometry geom, int margin) {
        xMin = margin;
        xMax = geom.getWidth() - margin;
        yMin = margin;
        yMax = geom.getHeight() - margin;
    }

    public void initBoundsLeftHandSide(TPGeometry geom) {
        xMin = 0;
        xMax = geom.getWidth() / 2;
        yMin = 0;
        yMax = geom.getHeight();
    }

    public void initBoundsRightHandSide(TPGeometry geom) {
        xMin = geom.getWidth() / 2;
        xMax = geom.getWidth();
        yMin = 0;
        yMax = geom.getHeight();
    }

    public void initInertia(double min, double max) {
        xInertiaMin = min;
        xInertiaMax = max;
        yInertiaMin = min;
        yInertiaMax = max;
    }

    @Override
    public void update(TPProgram prog) {
        for (String str : names) {
            TPActor a = prog.getActor(str);
            if (a != null) {
                a.x = rand(xMin, xMax);
                a.y = rand(yMin, yMax);
                a.xInertia = rand(xInertiaMin, xInertiaMax);
                a.yInertia = rand(yInertiaMin, yInertiaMax);
                a.angle = rand(angleMin, angleMax);
                a.angleInertia = rand(angleInertiaMin, angleInertiaMax);
            }
        }
    }

    private double rand(double min, double max) {

        if (min == 0 && max == 0)
            return 0;
        
        double range = Math.abs(max - min);
        return min + (Math.random() * range);
    }

}
