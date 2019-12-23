package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class TPLine {

    private Line archetype;
    private Line line;
    private TPActor actor = null;
    private boolean alive = true;

    public TPLine(Line line) {
        this.archetype = line;
        this.line = line;
    }

    /**
     * Makes a copy of the line and line-archetype, but doesn't copy other parameters.
     */
    public TPLine weakCopy() {
        TPLine ln = new TPLine(archetype);
        ln.line = line;
        return ln;
    }

    public Line getArchetype() { return archetype; }

    public Line getLine() { return line; }

    public void setLine(Line ln) { line = ln; }

    public boolean isAlive() { return alive; }

    public void setActor(TPActor a) { actor = a; }

    /**
     * TODO: add more params to enable physics simulation - magnitude/direction/sharpness
     */
    public void forceApplied(Pt p, TPLine protagonist) {
        alive = false;
        if (actor != null)
            actor.deathEvent(protagonist, p);
        if (protagonist.actor != null)
            protagonist.actor.killEvent(this, p);
    }

}
