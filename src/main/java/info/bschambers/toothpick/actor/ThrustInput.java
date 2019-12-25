package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public class ThrustInput extends KeyInputHandler {

    @Override
    public void update(TPProgram prog, TPActor tp) {
        if (bindUp.value()) {
            tp.x += Math.sin(Math.PI * tp.angle) * xyStep;
            tp.y -= Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindDown.value()) {
            tp.x -= Math.sin(Math.PI * tp.angle) * xyStep;
            tp.y += Math.cos(Math.PI * tp.angle) * xyStep;
        }
        if (bindLeft.value()) {
            tp.angle -= angleStep;
        }
        if (bindRight.value()) {
            tp.angle += angleStep;
        }
    }

}
