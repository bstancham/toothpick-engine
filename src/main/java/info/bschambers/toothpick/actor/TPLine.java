package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class TPLine {

    private Line archetype;
    private Line line;

    public TPLine(Line line) {
        this.archetype = line;
        this.line = line;
    }

    /** Copy constructor. */
    public TPLine(TPLine tpl) {
        this.archetype = tpl.line;
        this.line = tpl.line;
    }

    public Line getArchetype() { return archetype; }

    public Line getLine() { return line; }

    public void setLine(Line ln) { line = ln; }

    public void collisionWon(TPLine loser, Pt p) {
        // System.out.println("TPLine.collisionWon() at " + p);
    }

}
