package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class EightWayInertiaInput extends KeyInputHandler {

    public EightWayInertiaInput() {
        xyStep = 0.008;
        angleStep = 0.005;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        super.update(prog, tp);
        if (bindUp.value())    tp.yInertia -= xyStep;
        if (bindDown.value())  tp.yInertia += xyStep;
        if (bindLeft.value())  tp.xInertia -= xyStep;
        if (bindRight.value()) tp.xInertia += xyStep;
    }

}
