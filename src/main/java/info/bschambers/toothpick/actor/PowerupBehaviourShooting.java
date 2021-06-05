package info.bschambers.toothpick.actor;

public class PowerupBehaviourShooting extends PowerupBehaviour {

    public PowerupBehaviourShooting() {
        super("SHOOTING");
    }

    @Override
    public PowerupBehaviourShooting copy() {
        return this;
    }

    @Override
    protected void applyPowerup(TPActor actor) {
        actor.setTriggerBehaviour(new TrigActionShooter());
    }

}
