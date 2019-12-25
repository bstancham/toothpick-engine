package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class ThrustInertiaInput extends KeyInputHandler {

    public ThrustInertiaInput() {
        xyStep = 0.008;
        angleStep = 0.005;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        if (bindUp.value()) {
            tp.xInertia += Math.sin(Math.PI * tp.angle) * xyStep;
            tp.yInertia -= Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindDown.value()) {
            tp.xInertia -= Math.sin(Math.PI * tp.angle) * xyStep;
            tp.yInertia += Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindLeft.value()) {
            tp.angle -= angleStep;
        }
        if (bindRight.value()) {
            tp.angle += angleStep;
        }
    }

}
