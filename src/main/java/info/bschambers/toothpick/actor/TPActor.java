package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.game.TPProgram;
import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class TPActor {

    public static final TPActor NULL = new TPActor(TPForm.NULL);

    private TPForm form;
    private TPActor parent = null;
    private List<TPActor> children = new ArrayList<>();
    private List<TPBehaviour> behaviours = new ArrayList<>();
    public double x = 0;
    public double y = 0;
    public double angle = 0;
    public double xInertia = 0;
    public double yInertia = 0;
    public double angleInertia = 0;
    public int statsNumDeaths = 0;
    public int statsNumKills = 0;

    public TPActor(TPForm form) {
        this.form = form;
        this.form.setActor(this);
    }

    public String infoString() {
        StringBuffer s = new StringBuffer();
        s.append("alive: " + isAlive() + "\n");
        s.append("position: x=" + x + " y=" + y + "\n");
        s.append("inertia: x=" + xInertia + " y=" + yInertia + "\n");
        s.append("angle: " + angle + " (angle-inertia=" + angleInertia + ")\n");
        s.append("form type: " + form.getClass().getSimpleName() + "\n");
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
        for (TPBehaviour cb : behaviours)
            tp.behaviours.add(cb);
        return tp;
    }

    public TPForm getForm() { return form; }

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

}
