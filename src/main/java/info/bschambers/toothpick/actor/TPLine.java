package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class TPLine extends TPPart {

    public static final int STRENGTH_LIGHT = 1;
    public static final int STRENGTH_MEDIUM = 3;
    public static final int STRENGTH_HEAVY = 5;

    private Line archetype;
    private Line line;
    private int strength = STRENGTH_LIGHT;

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
        super.update(x, y, angle);
    }

    /**
     * @return An exact copy of this {@code TPLine}.
     */
    @Override
    public TPLine copy() {
        TPLine ln = new TPLine(archetype);
        ln.strength = strength;
        ln.copyBehaviours(this);
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

    public int getStrength() {
        return strength;
    }

    /**
     * <p>Use the static line-strength variables.</p>
     */
    public void setStrength(int val) {
        strength = val;
    }

    /**
     * <p>Apply force to this {@code TPLine} at point {@code p}. If this line has equal or
     * lesser strength that {@code protagonist} it will result in death. If this line has
     * greater strength then {@code protagonist} will die.</p>
     *
     * <p>TODO: add more params to enable physics simulation -
     * magnitude/direction/sharpness</p>
     */
    public void forceApplied(TPProgram prog, Pt p, TPLine protagonist) {
        prog.triggerSfx();
        // this line will die if strength is the same or weaker...
        // ... if protagonist is weaker then it will die instead
        if (getStrength() <= protagonist.getStrength()) {
            die(p);
            // send messages so that stats can be updated
            if (getActor() != null)
                getActor().deathEvent(protagonist, p);
            if (protagonist.getActor() != null)
                protagonist.getActor().killEvent(this, p);
        } else {
            protagonist.die(p);
        }
    }

    @Override
    public void die() {
        if (line == null)
            die(Pt.ZERO);
        else
            die(line.center());
    }

    private void die(Pt p) {
        // die with an explosion
        super.die();
        if (form != null)
            form.addPart(new TPExplosion(p));
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Line.class, getArchetype(), "setArchetype");
        params.addMethod(Integer.class, getStrength(), "setStrength");
        return params;
    }

}
