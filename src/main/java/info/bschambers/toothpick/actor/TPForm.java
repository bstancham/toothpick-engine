package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public abstract class TPForm {

    private TPActor actor = null;
    protected boolean alive = true;

    public void setActor(TPActor a) { actor = a; }

    public boolean isAlive() { return alive; }

    public void update(TPController ctrl) {}

}
