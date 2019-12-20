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

    public Line getLine() { return line; }

    public void collisionWon(TPLine loser, Pt p) {
        System.out.println("TPLine.collisionWon() at " + p);
    }

    public void setRotation(double angle) {}

    public void setPosition(Pt p) {
        line = archetype.shift(p);
    }

}
