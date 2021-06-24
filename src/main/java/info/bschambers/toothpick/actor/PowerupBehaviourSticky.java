package info.bschambers.toothpick.actor;

public class PowerupBehaviourSticky extends PowerupBehaviour {

    public PowerupBehaviourSticky() { super("STICKY"); }

    @Override
    public PowerupBehaviourSticky copy() { return this; }

    @Override
    public void applyPowerup(TPActor actor) {
        LBSticky sticky = new LBSticky();
        sticky.setIsContagious(true);
        // add sticky-behaviour to every link
        for (int i = 0; i < actor.getForm().numLinks(); i++)
            actor.getForm().getLink(i).addBehaviour(sticky);
    }

}
