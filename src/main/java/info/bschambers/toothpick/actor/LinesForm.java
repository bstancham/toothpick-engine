package info.bschambers.toothpick.actor;

import java.util.List;
import java.util.ArrayList;

public class LinesForm extends ActorForm {

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

}
