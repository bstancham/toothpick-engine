package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;

public class KeyInputEightWayInertia extends KeyInputHandler {

    public KeyInputEightWayInertia() {
        xyStep = 0.008;
        angleStep = 0.005;
    }

    @Override
    public KeyInputEightWayInertia copy() {
        KeyInputEightWayInertia a = new KeyInputEightWayInertia();
        duplicateParameters(a);
        return a;
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
