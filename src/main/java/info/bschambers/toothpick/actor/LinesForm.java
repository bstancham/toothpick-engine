package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class LinesForm extends TPForm {

    private List<TPLine> lines = new ArrayList<>();
    private List<TPLine> toRemove = new ArrayList<>();

    public LinesForm(TPLine line) {
        this(new TPLine[] { line });
    }

    public LinesForm(TPLine[] lines) {
        // make defensive copy of lines
        for (TPLine tpl : lines)
            this.lines.add(new TPLine(tpl));
    }

    @Override
    public void setActor(TPActor a) {
        super.setActor(a);
        for (TPLine ln : lines)
            ln.setActor(a);
    }

    public int numLines() { return lines.size(); }

    public TPLine getLine(int index) { return lines.get(index); }

    @Override
    public void update(TPController ctrl) {
        // remove dead lines
        toRemove.clear();
        for (TPLine ln : lines)
            if (!ln.isAlive())
                toRemove.add(ln);
        for (TPLine ln : toRemove)
            lines.remove(ln);
        // is form dead?
        if (lines.size() < 1)
            alive = false;
        // update position
        for (TPLine ln : lines) {
            Line temp = ln.getArchetype();
            temp = temp.rotate(ctrl.angle());
            temp = temp.shift(ctrl.pos());
            ln.setLine(temp);
        }
    }

}
