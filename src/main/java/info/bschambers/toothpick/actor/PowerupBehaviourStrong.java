package info.bschambers.toothpick.actor;

public class PowerupBehaviourStrong extends PowerupBehaviour {

    public PowerupBehaviourStrong() {
        super("STRONG");
    }

    @Override
    public PowerupBehaviourStrong copy() {
        return this;
    }

    @Override
    protected void applyPowerup(TPActor actor) {
        for (int i = 0; i < actor.getForm().numParts(); i++) {
            TPPart part = actor.getForm().getPart(i);
            if (part instanceof TPLine) {
                ((TPLine) part).setStrength(TPLine.STRENGTH_HEAVY);
            }
        }
    }

}
