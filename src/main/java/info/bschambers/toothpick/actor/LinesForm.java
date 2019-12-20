package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class LinesForm extends TPForm {

    private List<TPLine> lines = new ArrayList<>();

    public LinesForm(TPLine line) {
        this(new TPLine[] { line });
    }

    public LinesForm(TPLine[] lines) {
        // make defensive copy of lines
        for (TPLine tpl : lines)
            this.lines.add(new TPLine(tpl));
    }

    public int numLines() { return lines.size(); }
    public TPLine getLine(int index) { return lines.get(index); }

    @Override
    public void setRotation(double angle) {
        for (TPLine ln : lines)
            ln.setRotation(angle);
    }

    @Override
    public void setPosition(Pt p) {
        for (TPLine ln : lines)
            ln.setPosition(p);
    }

}
