package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Pt;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TPActor implements TPEncodingHelper {

    public static final TPActor NULL = new TPActor(new TPForm(new TPPart[0]));

    private TPForm form;
    private TPActor parent = null;
    private List<TPActor> children = new ArrayList<>();
    private List<TPBehaviour> behaviours = new ArrayList<>();
    private ColorGetter color = new ColorMono(Color.PINK);
    public double x = 0;
    public double y = 0;
    public double angle = 0;
    public double xInertia = 0;
    public double yInertia = 0;
    public double angleInertia = 0;
    public int statsNumDeaths = 0;
    public int statsNumKills = 0;

    public TPActor() {
        this(TPForm.NULL);
    }

    public TPActor(TPForm form) {
        setForm(form);
    }

    public String infoString() {
        StringBuffer s = new StringBuffer();
        s.append("alive: " + isAlive() + "\n");
        s.append("position: x=" + x + " y=" + y + "\n");
        s.append("inertia: x=" + xInertia + " y=" + yInertia + "\n");
        s.append("angle: " + angle + " (angle-inertia=" + angleInertia + ")\n");
        s.append("FORM: (" + form.numParts() + " parts)\n");
        for (int i = 0; i < form.numParts(); i++)
            s.append("... " + form.getPart(i) + "\n");
        s.append("parent: " + parent + "\n");
        s.append("num-children: " + children.size() + "\n");
        s.append("stats: deaths=" + statsNumDeaths + " kills=" + statsNumKills + "\n");
        s.append("BEHAVIOURS:\n");
        for (TPBehaviour b : behaviours)
            s.append("... " + b + "\n");
        return s.toString();
    }

    public TPActor copy() {
        TPActor tp = new TPActor(form.copy());
        tp.x = x;
        tp.y = y;
        tp.angle = angle;
        tp.xInertia = xInertia;
        tp.yInertia = yInertia;
        tp.angleInertia = angleInertia;
        tp.statsNumDeaths = statsNumDeaths;
        tp.statsNumKills = statsNumKills;
        tp.color = color.copy();
        for (TPBehaviour cb : behaviours)
            tp.behaviours.add(cb);

        tp.form.housekeeping();

        return tp;
    }

    public void copyStats(TPActor tp) {
        statsNumDeaths = tp.statsNumDeaths;
        statsNumKills = tp.statsNumKills;
    }

    public TPForm getForm() {
        return form;
    }

    public void setForm(TPForm form) {
        this.form = form;
        this.form.setActor(this);
    }

    public Color getColor() { return color.get(); }

    public void setColorGetter(ColorGetter cg) { color = cg; }

    public boolean isAlive() { return form.isAlive(); }

    public int numChildren() { return children.size(); }

    public TPActor getChild(int index) { return children.get(index); }

    public void addBehaviour(TPBehaviour tpb) {
        behaviours.add(tpb);
    }

    /**
     * Removes any existing input handlers and adds this one.
     */
    public void setInputHandler(KeyInputHandler newInput) {
        List<TPBehaviour> existing = new ArrayList<>();
        for (TPBehaviour tpb : behaviours)
            if (tpb instanceof KeyInputHandler)
                existing.add(tpb);
        for (TPBehaviour ih : existing)
            behaviours.remove(ih);
        behaviours.add(newInput);
    }

    public void update(TPProgram prog) {
        x += xInertia;
        y += yInertia;
        angle += angleInertia;
        for (TPBehaviour b : behaviours)
            b.update(prog, this);
        updateForm();
    }

    /**
     * Updates form in accordance with current angle and position.
     */
    public void updateForm() {
        form.update(this);
    }

    public void setPos(Pt pos) {
        x = pos.x;
        y = pos.y;
    }

    public void deathEvent(TPLine protagonist, Pt p) {
        statsNumDeaths++;
    }

    public void killEvent(TPLine victim, Pt p) {
        statsNumKills++;
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
        params.addField(Integer.class, statsNumDeaths, "statsNumDeaths");
        params.addField(Integer.class, statsNumKills, "statsNumKills");
        params.addMethod(ColorGetter.class, color, "setColorGetter");
        params.addMethod(TPForm.class, getForm(), "setForm");
        params.addListMethod(TPBehaviour.class, behaviours, "addBehaviour");
        params.addListMethod(TPActor.class, children, "addChild");
        return params;
    }

}
