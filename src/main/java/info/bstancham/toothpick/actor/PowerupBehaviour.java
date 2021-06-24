package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.geom.Pt;

/**
 * <p>On {@link die}, a powerup is applied to the killer.</p>
 */
public abstract class PowerupBehaviour extends PartBehaviour {

    private String powerupText;

    public PowerupBehaviour(String powerupText) {
        this.powerupText = powerupText;
    }

    @Override
    public void die(TPPart selfPart, TPActor selfActor,
                    TPPart killerPart, TPActor killerActor, Pt p) {
        applyPowerup(killerActor);
        if (p != null) {
            TPActor text = new TPActor(TPFactory.textFormCentered("*POWERUP>>>" +
                                                                  powerupText + "*"));
            text.x = p.x;
            text.y = p.y;
            text.yInertia = -0.3;
            text.addBehaviour(new DieAfter(200));
            killerActor.addChild(text);
        }
    }

    public abstract void applyPowerup(TPActor actor);

}
