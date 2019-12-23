package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public abstract class TPForm {

    public static final TPForm NULL = new TPForm() {
            @Override
            public TPForm copy() { return this; }
        };

    private TPActor actor = null;
    protected boolean alive = true;

    public abstract TPForm copy();

    public void setActor(TPActor a) { actor = a; }

    public boolean isAlive() { return alive; }

    public void update(TPActor a) {}

}
