package info.bschambers.toothpick.actor;

/**
 * <p>Use as a death-behaviour - causes all other parts in the form to die within a short
 * interval.</p>
 */
public class KeyPartBehaviour implements PartBehaviour {

    @Override
    public void action(TPPart part) {
        TPForm form = part.getForm();
        if (form != null) {
            // schedule each part to die after a random short interval
            for (int i = 0; i < form.numParts(); i++) {
                TPPart currentPart = form.getPart(i);
                if (currentPart.isAlive()) {
                    int interval = (int) (Math.random() * 50);
                    currentPart.addBehaviour(new DieAfter(interval));
                    currentPart.setPassive(true);
                }
            }
        }
    }

}
