package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class TPForm implements TPEncodingHelper {

    public static final TPForm NULL = new TPForm();

    private TPActor actor = null;
    protected boolean alive = true;
    private List<TPPart> parts = new ArrayList<>();
    private List<TPPart> toAdd = new ArrayList<>();
    private List<TPPart> toRemove = new ArrayList<>();

    public TPForm() {}

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

    public TPActor getActor() {
        return actor;
    }

    public void setActor(TPActor a) {
        actor = a;
    }

    public boolean isAlive() {
        return alive;
    }

    public int numParts() {
        return parts.size();
    }

    public TPPart getPart(int index) {
        return parts.get(index);
    }

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
        toRemove.clear();
        // add new parts
        for (TPPart p : toAdd)
            parts.add(p);
        toAdd.clear();
        // is form dead?
        if (parts.size() < 1)
            alive = false;
    }

    public void update(TPActor a) {
        housekeeping();
        for (TPPart p : parts)
            p.update(a.x, a.y, a.angle);
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addListMethod(TPPart.class, parts, "addPart");
        params.addVoidMethod("housekeeping");
        return params;
    }

}
