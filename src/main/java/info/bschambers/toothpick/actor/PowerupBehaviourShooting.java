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
        actor.setTriggerAction(new ShooterRunnable(actor));
    }

    private class ShooterRunnable implements Runnable {

        private TPActor actor;
        private TPActor archetype;
        private double relativeAngle = Math.PI * 0.5;

        public ShooterRunnable(TPActor actor) {
            this.actor = actor;
            archetype = new TPActor(TPFactory.singleLineForm(50));
        }

        @Override
        public void run() {
            TPActor bullet = archetype.copy();
            bullet.x = actor.x;
            bullet.y = actor.y;
            bullet.angle = actor.angle + relativeAngle;
            double speed = 2;
            bullet.xInertia = Math.sin(actor.angle * Math.PI) * speed;
            bullet.yInertia = -(Math.cos(actor.angle * Math.PI) * speed);
            bullet.setBoundaryBehaviour(TPActor.BoundaryBehaviour.DIE_AT_BOUNDS);
            actor.addChild(bullet);
        }
    }

}
