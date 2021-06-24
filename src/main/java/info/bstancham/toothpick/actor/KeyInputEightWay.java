package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;

public class KeyInputEightWay extends KeyInputHandler {

    @Override
    public KeyInputEightWay copy() {
        KeyInputEightWay a = new KeyInputEightWay();
        duplicateParameters(a);
        return a;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        super.update(prog, tp);
        if (bindUp.value())    tp.y -= xyStep;
        if (bindDown.value())  tp.y += xyStep;
        if (bindLeft.value())  tp.x -= xyStep;
        if (bindRight.value()) tp.x += xyStep;
    }

}
