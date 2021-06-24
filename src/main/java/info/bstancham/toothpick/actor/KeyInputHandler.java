package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;

public abstract class KeyInputHandler implements ActorBehaviour {

    public static final KeyInputHandler NULL = new KeyInputHandler() {
            @Override
            public KeyInputHandler copy() { return this; }
            @Override
            public void update(TPProgram prog, TPActor a) {}
        };

    protected double xyStep = 1;
    protected double angleStep = 0.015;

    public KeyBinding bindUp      = new KeyBinding("up",       81); // q
    public KeyBinding bindDown    = new KeyBinding("down",     65); // a
    public KeyBinding bindLeft    = new KeyBinding("left",     87); // w
    public KeyBinding bindRight   = new KeyBinding("right",    69); // e
    public KeyBinding bindAction  = new KeyBinding("action",   90); // z
    public KeyBinding bindZoomIn  = new KeyBinding("zoom in",  50); // 2
    public KeyBinding bindZoomOut = new KeyBinding("zoom out", 51); // 3

    private KeyBinding[] bindings = new KeyBinding[] { bindUp,
                                                       bindDown,
                                                       bindLeft,
                                                       bindRight,
                                                       bindAction,
                                                       bindZoomIn,
                                                       bindZoomOut };

    @Override
    public String getSingletonGroup() {
        return ActorBehaviour.KEY_INPUT_BEHAVIOUR_FLAG;
    }

    @Override
    public void update(TPProgram prog, TPActor tp) {
        tp.setActionTrigger(bindAction.value());
    }

    public abstract KeyInputHandler copy();

    protected void duplicateParameters(KeyInputHandler a) {
        a.xyStep = xyStep;
        a.angleStep = angleStep;
        a.bindUp = bindUp.copy();
        a.bindDown = bindDown.copy();
        a.bindLeft = bindLeft.copy();
        a.bindRight = bindRight.copy();
        a.bindAction = bindAction.copy();
        a.bindZoomIn = bindZoomIn.copy();
        a.bindZoomOut = bindZoomOut.copy();
        a.bindings = new KeyBinding[] { bindUp,
                                        bindDown,
                                        bindLeft,
                                        bindRight,
                                        bindAction,
                                        bindZoomIn,
                                        bindZoomOut };
    }

    public double getXYStep() { return xyStep; }

    public double getAngleStep() { return angleStep; }

    public void incrXYStep(double amt) { xyStep += amt; }

    public void incrAngleStep(double amt) { angleStep += amt; }

    public void setKey(int keyCode, boolean val) {
        // System.out.println("keycode = " + keyCode);
        for (KeyBinding b : bindings)
            if (keyCode == b.code())
                b.setValue(val);
    }

}
