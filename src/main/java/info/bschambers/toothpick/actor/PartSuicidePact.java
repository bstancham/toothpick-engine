package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

/**
 * <p>On {@link die}, causes all other parts in the form to die within a short
 * interval.</p>
 */
public class PartSuicidePact extends PartBehaviour {

    private int maxWait = 50;

    @Override
    public PartSuicidePact copy() { return this; }

    @Override
    public void die(TPPart selfPart, TPActor selfActor,
                    TPPart killerPart, TPActor killerActor, Pt p) {
        for (int i = 0; i < selfActor.getForm().numParts(); i++) {
            TPPart currentPart = selfActor.getForm().getPart(i);
            if (currentPart.isAlive()) {
                int interval = (int) (Math.random() * maxWait);
                currentPart.addBehaviour(new PartDieAfter(interval));
                currentPart.setPassive(true);
            }
        }
    }

}
