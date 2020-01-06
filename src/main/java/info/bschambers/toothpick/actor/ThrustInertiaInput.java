package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class ThrustInertiaInput extends KeyInputHandler {

    public ThrustInertiaInput() {
        xyStep = 0.008;
        angleStep = 0.005;
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
            tp.angleInertia = -angleStep;
        }
        if (bindRight.value()) {
            tp.angleInertia = angleStep;
        }
        if (!bindLeft.value() && !bindRight.value()){
            tp.angleInertia = 0;
        }
    }

}
