package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;

public class KeyInputThrustInertia extends KeyInputHandler {

    public KeyInputThrustInertia() {
        xyStep = 0.008;
        angleStep = 0.015;
    }

    @Override
    public KeyInputThrustInertia copy() {
        KeyInputThrustInertia a = new KeyInputThrustInertia();
        duplicateParameters(a);
        return a;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        super.update(prog, tp);
        if (bindUp.value()) {
            tp.xInertia += Math.cos(tp.angle) * xyStep;
            tp.yInertia += Math.sin(tp.angle) * xyStep;
        }
        if (bindDown.value()) {
            tp.xInertia -= Math.cos(tp.angle) * xyStep;
            tp.yInertia -= Math.sin(tp.angle) * xyStep;
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

    public static void applyThrust(TPActor a, double angle, double power) {
        // a.xInertia += Math.sin(Math.PI * angle) * amount;
        // a.yInertia -= Math.cos(Math.PI * angle) * amount;
        a.xInertia += thrustAmountX(angle, power);
        a.yInertia -= thrustAmountY(angle, power);
    }

    public static double thrustAmountX(double angle, double power) {
        return Math.cos(angle) * power;
    }

    public static double thrustAmountY(double angle, double power) {
        return Math.sin(angle) * power;
    }

}
