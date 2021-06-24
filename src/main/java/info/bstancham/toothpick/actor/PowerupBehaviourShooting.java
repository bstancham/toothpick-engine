package info.bstancham.toothpick.actor;

public class PowerupBehaviourShooting extends PowerupBehaviour {

    public PowerupBehaviourShooting() {
        super("SHOOTING");
    }

    @Override
    public PowerupBehaviourShooting copy() {
        return this;
    }

    @Override
    public void applyPowerup(TPActor actor) {
        actor.setTriggerBehaviour(new TrigActionShooter());
    }

}
