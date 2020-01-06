package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Pt;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TPActor implements TPEncodingHelper {

    public enum BoundaryBehaviour {
        DO_NOTHING_AT_BOUNDS,
        DIE_AT_BOUNDS,
        WRAP_AT_BOUNDS,
        WRAP_PARTS_AT_BOUNDS
    };

    public static final TPActor NULL = new TPActor(new TPForm(new TPPart[0]));

    private Color bgColor = Color.BLACK;

    private TPForm form;
    private TPActor parent = null;
    private List<TPActor> children = new ArrayList<>();
    private List<TPActor> childrenToAdd = new ArrayList<>();
    private List<TPActor> childrenToRemove = new ArrayList<>();
    private List<TPBehaviour> behaviours = new ArrayList<>();
    private BoundaryBehaviour boundsBehaviour = BoundaryBehaviour.DIE_AT_BOUNDS;
    private ColorGetter color = new ColorMono(Color.PINK);
    private boolean actionTrigger = false;
    public double x = 0;
    public double y = 0;
    public double angle = 0;
    public double xInertia = 0;
    public double yInertia = 0;
    public double angleInertia = 0;
    public int numDeaths = 0;
    public int numKills = 0;

    public TPActor() {
        this(TPForm.NULL);
    }

    public TPActor(TPForm form) {
        setForm(form);
    }

    public String infoString() {
        StringBuffer s = new StringBuffer();
        s.append("instance: " + super.toString() + "\n");
        s.append("alive: " + isAlive() + "\n");
        s.append("position: x=" + x + " y=" + y + "\n");
        s.append("inertia: x=" + xInertia + " y=" + yInertia + "\n");
        s.append("angle: " + angle + " (angle-inertia=" + angleInertia + ")\n");
        s.append("bounds-behaviour: " + boundsBehaviour);
        s.append("FORM: (" + form.numParts() + " parts)\n");
        for (int i = 0; i < form.numParts(); i++)
            s.append("... " + form.getPart(i) + "\n");
        s.append("parent: " + parent + "\n");
        s.append("num-children: " + children.size() + "\n");
        s.append("stats: deaths=" + numDeaths + " kills=" + numKills + "\n");
        s.append("BEHAVIOURS:\n");
        for (TPBehaviour b : behaviours)
            s.append("... " + b + "\n");
        return s.toString();
    }

    public TPActor copy() {
        TPActor tp = new TPActor(form.copy());
        tp.boundsBehaviour = boundsBehaviour;
        tp.x = x;
        tp.y = y;
        tp.angle = angle;
        tp.xInertia = xInertia;
        tp.yInertia = yInertia;
        tp.angleInertia = angleInertia;
        tp.numDeaths = numDeaths;
        tp.numKills = numKills;
        tp.color = color.copy();
        for (TPBehaviour cb : behaviours)
            tp.behaviours.add(cb);
        tp.updateForm();
        return tp;
    }

    public void copyStats(TPActor tp) {
        numDeaths = tp.numDeaths;
        numKills = tp.numKills;
    }

    public TPForm getForm() {
        return form;
    }

    public void setForm(TPForm form) {
        this.form = form;
        this.form.setActor(this);
    }

    public BoundaryBehaviour getBoundaryBehaviour() {
        return boundsBehaviour;
    }

    public void setBoundaryBehaviour(BoundaryBehaviour val) {
        boundsBehaviour = val;
    }

    public boolean getActionTrigger() {
        return actionTrigger;
    }

    public void setActionTrigger(boolean val) {
        actionTrigger = val;
    }

    public Color getColor() {
        // return color.get();
        return color.getWithBG(bgColor);
    }

    public void setColorGetter(ColorGetter cg) {
        color = cg;
    }

    public boolean isAlive() {
        return form.isAlive();
    }

    public int numChildren() {
        return children.size();
    }

    public TPActor getChild(int index) {
        return children.get(index);
    }

    public void addChild(TPActor child) {
        childrenToAdd.add(child);
    }

    /**
     * <p>Add a behaviour. If behaviour {@code tpb} is a member of a singleton-group then
     * any existing member of that group will be removed.</p>
     */
    public void addBehaviour(TPBehaviour tpb) {
        String group = tpb.getSingletonGroup();
        if (!group.isEmpty()) {
            List<TPBehaviour> tpbToRemove = new ArrayList<>();
            for (TPBehaviour other : behaviours)
                if (other.getSingletonGroup().equals(group))
                    tpbToRemove.add(other);
            for (TPBehaviour other : tpbToRemove)
                behaviours.remove(other);
        }
        behaviours.add(tpb);
    }

    /**
     * Removes any existing input handlers and adds this one.
     */
    public void setInputHandler(KeyInputHandler newInput) {
        // KeyInputHandler will be identified by it's singleton-group ID
        behaviours.add(newInput);
    }

    public void update(TPProgram prog) {

        bgColor = prog.getBGColor();

        x += xInertia;
        y += yInertia;
        angle += angleInertia;

        boundaryCheckPosition(prog);

        updateForm();

        if (boundsBehaviour == BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS)
            wrapFormAtBounds(prog.getGeometry());

        for (TPBehaviour b : behaviours)
            b.update(prog, this);

        // add and remove children
        for (TPActor child : children)
            if (!child.isAlive())
                childrenToRemove.add(child);
        for (TPActor child : childrenToRemove)
            children.remove(child);
        childrenToRemove.clear();

        for (TPActor child : childrenToAdd)
            children.add(child);
        childrenToAdd.clear();

        for (TPActor child : children)
            child.update(prog);
    }

    /**
     * Updates form in accordance with current angle and position.
     */
    public void updateForm() {
        form.update(this);
    }

    private void wrapFormAtBounds(TPGeometry geom) {
        form.wrapAtBounds(geom);
    }

    private void boundaryCheckPosition(TPProgram prog) {
        if (boundsBehaviour == BoundaryBehaviour.DIE_AT_BOUNDS) {
            TPGeometry geom = prog.getGeometry();
            if (x < 0 ||
                x > geom.getWidth() ||
                y < 0 ||
                y > geom.getHeight())
                // if form is empty then actor will die
                setForm(TPForm.NULL);

        } else if (boundsBehaviour == BoundaryBehaviour.WRAP_AT_BOUNDS ||
                   boundsBehaviour == BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS) {
            TPGeometry geom = prog.getGeometry();
            if (x < 0)
                x = geom.getWidth();
            else if (x > geom.getWidth())
                x = 0;
            else if (y < 0)
                y = geom.getHeight();
            else if (y > geom.getHeight())
                y = 0;
        }
    }

    public void setPos(Pt pos) {
        x = pos.x;
        y = pos.y;
    }

    public void deathEvent(TPLine protagonist, Pt p) {
        numDeaths++;
    }

    public void killEvent(TPLine victim, Pt p) {
        numKills++;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addField(Double.class, x, "x");
        params.addField(Double.class, y, "y");
        params.addField(Double.class, angle, "angle");
        params.addField(Double.class, xInertia, "xInertia");
        params.addField(Double.class, yInertia, "yInertia");
        params.addField(Double.class, angleInertia, "angleInertia");
        params.addField(Integer.class, numDeaths, "numDeaths");
        params.addField(Integer.class, numKills, "numKills");
        params.addMethod(ColorGetter.class, color, "setColorGetter");
        params.addMethod(TPForm.class, getForm(), "setForm");
        params.addListMethod(TPBehaviour.class, behaviours, "addBehaviour");
        params.addListMethod(TPActor.class, children, "addChild");
        params.addVoidMethod("updateForm");
        return params;
    }

}
