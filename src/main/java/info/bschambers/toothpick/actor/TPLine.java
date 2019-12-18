package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Line;

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

}
