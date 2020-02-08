package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

/**
 * <p>On {@link die}, causes all other parts in the form to die within a short
 * interval.</p>
 */
public class SuicidePactBehaviour extends PartBehaviour {

    @Override
    public SuicidePactBehaviour copy() {
        return this;
    }

    @Override
    public void die(TPPart part, TPPart killer, Pt p) {
        TPForm form = part.getForm();
        if (form != null) {
            // schedule each part to die after a random short interval
            for (int i = 0; i < form.numParts(); i++) {
                TPPart currentPart = form.getPart(i);
                if (currentPart.isAlive()) {
                    int interval = (int) (Math.random() * 50);
                    currentPart.addBehaviour(new PBDieAfter(interval));
                    currentPart.setPassive(true);
                }
            }
        }
    }

}
