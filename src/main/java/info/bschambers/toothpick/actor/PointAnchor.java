package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Pt;

public class PointAnchor implements ActorBehaviour {

    private static double DELTA = 1.0;
    private TPLine anchorLine = null;
    private TPForm anchorForm = null;
    private boolean anchorAtStart = true;

    /**
     * @param line Anchor to start point of this line.
     * @param form When line is destroyed, check this form for other contiguous points.
     */
    public void setAnchor(TPLine line, TPForm form) {
        setAnchor(line, form, true);
    }

    /**
     * @param line Anchor to start point of this line.
     * @param form When line is destroyed, check this form for other contiguous points.
     * @param anchorAtStart If true, anchor to the start point of {@code line}, if false,
     * anchor to the end point.
     */
    public void setAnchor(TPLine line, TPForm form, boolean anchorAtStart) {
        anchorLine = line;
        anchorForm = form;
        this.anchorAtStart = anchorAtStart;
    }

    public void update(TPProgram prog, TPActor a) {

        if (anchorLine != null) {
            Pt p = (anchorAtStart ? anchorLine.getLine().start : anchorLine.getLine().end);
            a.x = p.x;
            a.y = p.y;

            // if line is dead then try to jump if other line has same vertex
            if (!anchorLine.isAlive() && anchorForm != null) {
                anchorLine = null;
                for (int i = 0; i < anchorForm.numParts(); i++) {
                    if (anchorForm.getPart(i) instanceof TPLine) {
                        TPLine tpl = (TPLine) anchorForm.getPart(i);
                        if (tpl.isAlive()) {
                            if (tpl.getLine().start.equals(p, DELTA)) {
                                anchorLine = tpl;
                                anchorAtStart = true;
                                return;
                            } else if (tpl.getLine().end.equals(p, DELTA)) {
                                anchorLine = tpl;
                                anchorAtStart = false;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
