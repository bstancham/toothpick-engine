package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class ThrustAndAngleInertiaInput extends KeyInputHandler {

    public ThrustAndAngleInertiaInput() {
        xyStep = 0.008;
        angleStep = 0.00003;
    }

    @Override
    public ThrustAndAngleInertiaInput copy() {
        ThrustAndAngleInertiaInput a = new ThrustAndAngleInertiaInput();
        duplicateParameters(a);

        return a;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        super.update(prog, tp);
        if (bindUp.value()) {
            tp.xInertia += Math.sin(Math.PI * tp.angle) * xyStep;
            tp.yInertia -= Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindDown.value()) {
            tp.xInertia -= Math.sin(Math.PI * tp.angle) * xyStep;
            tp.yInertia += Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindLeft.value()) {
            tp.angleInertia -= angleStep;
        }
        if (bindRight.value()) {
            tp.angleInertia += angleStep;
        }
    }

}
