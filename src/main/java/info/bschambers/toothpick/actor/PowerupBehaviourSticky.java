package info.bschambers.toothpick.actor;

public class PowerupBehaviourSticky extends PowerupBehaviour {

    public PowerupBehaviourSticky() {
        super("STICKY");
    }

    @Override
    public PowerupBehaviourSticky copy() {
        return this;
    }

    @Override
    protected void applyPowerup(TPActor actor) {
        for (int i = 0; i < actor.getForm().numParts(); i++) {
            TPPart part = actor.getForm().getPart(i);
            if (part instanceof TPLine) {
                part.addBehaviour(new LBSticky());
            }
        }
    }

}
