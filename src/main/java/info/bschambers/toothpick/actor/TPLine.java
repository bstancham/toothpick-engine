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

    /** Copy constructor. */
    public TPLine(TPLine tpl) {
        this.archetype = tpl.line;
        this.line = tpl.line;
    }

    public Line getArchetype() { return archetype; }

    public Line getLine() { return line; }

    public void setLine(Line ln) { line = ln; }

    public boolean isAlive() { return alive; }

    public void setActor(TPActor a) { actor = a; }

    public void collisionWon(TPLine loser, Pt p) {
        loser.kill(this, p);
    }

    public void kill(TPLine killer, Pt p) {
        alive = false;
        if (actor != null)
            actor.getController().deathEvent(killer, p);
        if (killer.actor != null)
            killer.actor.getController().killEvent(killer, p);
    }

}
