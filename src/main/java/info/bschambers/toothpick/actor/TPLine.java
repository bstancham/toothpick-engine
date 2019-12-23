package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class TPLine extends TPPart {

    private Line archetype;
    private Line line;

    public TPLine(Line line) {
        this.archetype = line;
        this.line = line;
    }

    @Override
    public void setState(double x, double y, double angle) {
        Line temp = getArchetype();
        temp = temp.rotate(angle);
        temp = temp.shift(x, y);
        setLine(temp);
    }

    @Override
    public TPLine copy() {
        TPLine ln = new TPLine(archetype);
        ln.alive = alive;
        return ln;
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

    /**
     * TODO: add more params to enable physics simulation - magnitude/direction/sharpness
     */
    public void forceApplied(Pt p, TPLine protagonist) {
        alive = false;
        if (getActor() != null)
            getActor().deathEvent(protagonist, p);
        if (protagonist.getActor() != null)
            protagonist.getActor().killEvent(this, p);
    }

}
