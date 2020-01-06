package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class TPLine extends TPPart {

    private Line archetype;
    private Line line;

    public TPLine() {}

    public TPLine(Line line) {
        this.archetype = line;
        this.line = line;
    }

    @Override
    public void update(double x, double y, double angle) {
        Line temp = getArchetype();
        temp = temp.rotate(angle);
        temp = temp.shift(x, y);
        setLine(temp);
    }

    @Override
    public TPLine copy() {
        TPLine ln = new TPLine(archetype);
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

    @Override
    public boolean hasDimensions() {
        return true;
    }

    @Override
    public int xMin() {
        return (int) Math.min(getLine().start.x, getLine().end.x);
    }

    @Override
    public int yMin() {
        return (int) Math.min(getLine().start.y, getLine().end.y);
    }

    @Override
    public int xMax() {
        return (int) Math.max(getLine().start.x, getLine().end.x);
    }

    @Override
    public int yMax() {
        return (int) Math.max(getLine().start.y, getLine().end.y);
    }

    @Override
    public void translate(int x, int y) {
        line = line.shift(x, y);
    }

    public Line getArchetype() {
        return archetype;
    }

    public void setArchetype(Line ln) {
        archetype = ln;
    }

    public Line getLine() {
        if (line == null)
            return archetype;
        return line;
    }

    private void setLine(Line ln) {
        line = ln;
    }

    /**
     * TODO: add more params to enable physics simulation - magnitude/direction/sharpness
     */
    public void forceApplied(TPProgram prog, Pt p, TPLine protagonist) {
        // die with an explosion
        if (form != null) {
            form.removePart(this);
            form.addPart(new TPExplosion(p));
            prog.triggerSfx();
        }
        // send messages so that stats can be updated
        if (getActor() != null)
            getActor().deathEvent(protagonist, p);
        if (protagonist.getActor() != null)
            protagonist.getActor().killEvent(this, p);
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Line.class, getArchetype(), "setArchetype");
        return params;
    }

}
