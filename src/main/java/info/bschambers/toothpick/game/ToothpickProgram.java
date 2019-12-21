package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.ui.TPUI;
import java.util.ArrayList;
import java.util.List;

public class ToothpickProgram extends TPProgram {

    public ToothpickProgram(String title) {
        super(title);
    }

    @Override
    public List<String> getInfoLines() {
        List<String> lines = super.getInfoLines();
        lines.add("num kills: " + getPlayerController().getNumKills());
        lines.add("num deaths: " + getPlayerController().getNumDeaths());
        lines.add("fps: ");
        lines.add("num actors: " + actors.size());
        return lines;
    }

    /**
     * Do collision detection - store intersection points for further use.
     */
    @Override
    protected void interactions() {
        intersectionPoints.clear();
        for (TPActor a : actors)
            for (TPActor b : actors)
                if (a != b)
                    interact(a, b);
    }

    private void interact(TPActor a, TPActor b) {
        // interact forms
	interact(a.getForm(), b.getForm());
        // interact children
        for (int n = 0; n < a.numChildren(); n++)
            interact(b, a.getChild(n));
        for (int n = 0; n < b.numChildren(); n++)
            interact(a, b.getChild(n));
    }

    private void interact(TPForm formA, TPForm formB) {
	// can only perform collision detection with LinesForm
	if (formA instanceof LinesForm && formB instanceof LinesForm) {
            LinesForm linesA = (LinesForm) formA;
            LinesForm linesB = (LinesForm) formB;
	    // Compare every line of form A with every line of form B.
            for (int a = 0; a < linesA.numLines(); a++) {
                for (int b = 0; b < linesB.numLines(); b++) {
                    collisionDetection(linesA.getLine(a), linesB.getLine(b));
                }
            }
	}
    }

    /**
     * Compares two lines to see whether they intersect. If an intersection is detected,
     * the die signal is sent to the appropriate line... <br/>
     *
     * If either or both of the lines are 'dying', the <code>null</code> value is
     * returned.<br/>
     *
     * @return The intersection point - or <code>null</code> if either or both
     * of the lines is dying...
     */
    private Pt collisionDetection(TPLine a, TPLine b) {
        Line ln1 = a.getLine();
        Line ln2 = b.getLine();
        Pt iPt = Geom.lineIntersection(ln1, ln2);

        if (keepIntersectionPoints)
            intersectionPoints.add(iPt);

        double dist1 = Geom.fractionDistFromCenter(ln1, iPt);
        double dist2 = Geom.fractionDistFromCenter(ln2, iPt);

        if (dist1 < 1.0 && dist2 < 1.0) {
            if (dist1 > dist2) {
                // line A is the winner!
                a.collisionWon(b, iPt);
            } else {
                // line B is the winner!
                b.collisionWon(a, iPt);
            }
        }

        return iPt;
    }

}
