package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

/**
 * <p>On {@link killLine}, assimilates every line of victim's form into own form.</p>
 */
public class LBSticky extends LineBehaviour {

    private boolean makeVictimsSticky = false;

    @Override
    public LBSticky copy() {
        return this;
    }

    @Override
    public void killLine(TPLine thisLine, TPLine victim, Pt p) {
	TPForm form = thisLine.getForm();
	TPForm victimForm = victim.getForm();
        if (form != null && victimForm != null) {
            TPActor actor = form.getActor();
            TPActor victimActor = victimForm.getActor();
            if (actor != null && victimActor != null) {
                for (int i = 0; i < victimForm.numParts(); i++) {
                    // assimilate each line
                    if (victimForm.getPart(i) instanceof TPLine) {
                        Line ln = ((TPLine) victimForm.getPart(i)).getLine();
                        // rotate and translate to fit sticky line's frame of reference

                        // position
                        ln = ln.shift(-actor.x, -actor.y);
                        // rotation
                        ln = Geom.rotate(ln, -actor.angle, Pt.ZERO);
                        // add to sticky-line's form
                        form.addPart(new TPLine(ln));
                    }
                }
                // dispose of victim via instant death
                victimForm.setAlive(false);
            }
        }
    }

}
