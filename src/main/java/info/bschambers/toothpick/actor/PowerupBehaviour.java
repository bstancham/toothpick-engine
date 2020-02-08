package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

/**
 * <p>On {@link die}, a powerup is applied to the killer.</p>
 */
public abstract class PowerupBehaviour extends PartBehaviour {

    private String powerupText;

    public PowerupBehaviour(String powerupText) {
        this.powerupText = powerupText;
    }

    @Override
    public void die(TPPart part, TPPart killer, Pt p) {
        TPActor actor = getActor(killer);
        if (actor != null) {
            applyPowerup(actor);
            if (p != null) {
                TPActor text = new TPActor(TPFactory.textFormCentered("*POWERUP>>>" +
                                                                      powerupText + "*"));
                text.x = p.x;
                text.y = p.y;
                text.yInertia = -0.3;
                text.addBehaviour(new DieAfter(200));
                actor.addChild(text);
            }
        }
    }

    /**
     * <p>Get actor if it exists, else return null.</p>
     */
    protected TPActor getActor(TPPart part) {
        if (part != null)
            if (part.getForm() != null)
                if (part.getForm().getActor() != null)
                    return part.getForm().getActor();
        return null;
    }

    protected abstract void applyPowerup(TPActor actor);

}
