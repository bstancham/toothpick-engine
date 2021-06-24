package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.geom.Geom;
import info.bstancham.toothpick.geom.Node;
import info.bstancham.toothpick.geom.Pt;

/**
 * <p>On {@link killLine}, assimilates every line of victim's form into own form.</p>
 */
public class LBSticky extends LinkBehaviour {

    private boolean contagious = false;

    @Override
    public LBSticky copy() { return this; }

    public boolean isContagious() { return contagious; }

    public void setIsContagious(boolean val) { contagious = val; }

    @Override
    public void killLink(TPLink selfLink, TPActor selfActor,
                         TPLink victimLink, TPActor victimActor, Pt p) {
        TPForm form = selfActor.getForm();
        TPForm victimForm = victimActor.getForm();
        if (form != null && victimForm != null) {

            // transform victim nodes to fit sticky line's frame of reference
            for (int i = 0; i < victimForm.numNodes(); i++) {
                Node n = victimForm.getNode(i);
                // position first...
                double x = victimActor.x - selfActor.x;
                double y = victimActor.y - selfActor.y;
                n.update(0, x, y);
                n.resetArchetype();
                // ... then rotation
                n.update(-selfActor.angle, 0, 0);
                n.resetArchetype();
            }

            // add victim's links to sticky line's form
            for (int i = 0; i < victimForm.numLinks(); i++) {
                TPLink ln = victimForm.getLink(i);
                TPLink newLink = new TPLink(ln.getStartNode(), ln.getEndNode());
                if (contagious)
                    newLink.addBehaviour(this.copy());
                form.addPart(newLink);
            }
            form.housekeeping();

            // dispose of victim via instant death
            victimForm.setAlive(false);
        }
    }

}
