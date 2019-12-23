package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class TPForm {

    private TPActor actor = null;
    protected boolean alive = true;
    private List<TPPart> parts = new ArrayList<>();
    private List<TPPart> toAdd = new ArrayList<>();
    private List<TPPart> toRemove = new ArrayList<>();

    public TPForm(TPPart[] parts) {
        for (TPPart p : parts) {
            p.setForm(this);
            this.parts.add(p);
        }
    }

    public TPForm copy() {
        TPPart[] copies = new TPPart[parts.size()];
        for (int i = 0; i < parts.size(); i++)
            copies[i] = parts.get(i).copy();
        TPForm form = new TPForm(copies);
        form.actor = actor;
        form.alive = alive;
        return form;
    }

    public TPActor getActor() { return actor; }

    public void setActor(TPActor a) { actor = a; }

    public boolean isAlive() { return alive; }

    public int numParts() { return parts.size(); }

    public TPPart getPart(int index) { return parts.get(index); }

    /**
     * Schedule part to be added on next update.
     */
    public void addPart(TPPart p) {
        p.setForm(this);
        toAdd.add(p);
    }

    /**
     * Schedule part to be removed on next update.
     */
    public void removePart(TPPart p) {
        toRemove.add(p);
    }

    public void housekeeping() {
        // remove dead parts
        for (TPPart p : toRemove)
            parts.remove(p);
        // add new parts
        for (TPPart p : toAdd)
            parts.add(p);
        // is form dead?
        if (parts.size() < 1)
            alive = false;

        toRemove.clear();
        toAdd.clear();
    }

    public void update(TPActor a) {
        housekeeping();
        // update position
        for (TPPart p : parts)
            p.update(a.x, a.y, a.angle);
    }

}
