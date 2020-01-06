package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Pt;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class TPForm implements TPEncodingHelper {

    public static final TPForm NULL = new TPForm();

    private TPActor actor = null;
    protected boolean alive = true;
    private List<TPPart> parts = new ArrayList<>();
    private List<TPPart> toAdd = new ArrayList<>();
    private List<TPPart> toRemove = new ArrayList<>();
    public Rectangle bounds = new Rectangle();

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

    public Rectangle getBoundingBox() {
        return bounds;
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

    /**
     * <p>Does housekeeping, updates the form for the actor position and rotation, updates
     * the bounding box.</p>
     */
    public void update(TPActor a) {
        housekeeping();

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        boolean first = true;

        for (TPPart p : parts) {
            p.update(a.x, a.y, a.angle);

            if (p.hasDimensions()) {
                if (first) {
                    x1 = p.xMin();
                    y1 = p.yMin();
                    x2 = p.xMax();
                    y2 = p.yMax();
                    first = false;
                } else {
                    if (p.xMin() < x1)
                        x1 = p.xMin();
                    if (p.yMin() < y1)
                        y1 = p.yMin();
                    if (p.xMax() > x2)
                        x2 = p.xMax();
                    if (p.yMax() > y2)
                        y2 = p.yMax();
                }
            }
        }
        bounds.setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * <p>For any part outside of bounds, wrap around to the other side.</p>
     */
    public void wrapAtBounds(TPGeometry geom) {
        for (TPPart p : parts) {
            if (p.hasDimensions()) {
                int x = Geom.midVal(p.xMin(), p.xMax());
                int y = Geom.midVal(p.yMin(), p.yMax());
                if (x < 0)
                    p.translate(geom.getWidth(), 0);
                else if (x > geom.getWidth())
                    p.translate(-geom.getWidth(), 0);
                if (y < 0)
                    p.translate(0, geom.getHeight());
                else if (y > geom.getHeight())
                    p.translate(0, -geom.getHeight());
            }
        }
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addListMethod(TPPart.class, parts, "addPart");
        return params;
    }

}
