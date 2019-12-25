package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class EightWayInput extends KeyInputHandler {

    @Override
    public void update(TPProgram prog, TPActor tp) {
        if (bindUp.value())    tp.y -= xyStep;
        if (bindDown.value())  tp.y += xyStep;
        if (bindLeft.value())  tp.x -= xyStep;
        if (bindRight.value()) tp.x += xyStep;
    }

}
