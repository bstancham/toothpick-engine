package info.bstancham.toothpick.actor;

public class PowerupBehaviourStrong extends PowerupBehaviour {

    public PowerupBehaviourStrong() { super("STRONG"); }

    @Override
    public PowerupBehaviourStrong copy() { return this; }

    @Override
    public void applyPowerup(TPActor actor) {
        // make every link in form STRONG
        for (int i = 0; i < actor.getForm().numLinks(); i++)
            actor.getForm().getLink(i).setStrength(TPLink.STRENGTH_HEAVY);
    }

}
