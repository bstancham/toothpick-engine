package info.bschambers.toothpick.actor;

public class TrigActionShooter implements TriggerBehaviour {

    private TPActor bulletArchetype;
    private double bulletSpeed = 2;
    private int delay = 200;
    private int waitCount = 0;

    public TrigActionShooter() { this(200); }

    public TrigActionShooter(int delay) {
        this(new TPActor(TPFactory.singleLineFormHoriz(50)), delay);
    }

    public TrigActionShooter(TPActor bulletArchetype, int delay) {
        this.bulletArchetype = bulletArchetype;
        this.delay = delay;
    }

    @Override
    public void update(TPActor actor, boolean triggered) {
        if (waitCount > 0)
            waitCount--;
        if (triggered && waitCount <= 0) {
            waitCount = delay;
            triggerAction(actor);
        }
    }

    protected void triggerAction(TPActor actor) {
        addBullet(actor, 0);
    }

    protected void addBullet(TPActor actor, double relAngle) {
        TPActor bullet = bulletArchetype.copy();
        bullet.x = actor.x;
        bullet.y = actor.y;
        bullet.angle = actor.angle + relAngle;
        bullet.xInertia = Math.cos(bullet.angle) * bulletSpeed;
        bullet.yInertia = (Math.sin(bullet.angle) * bulletSpeed);
        bullet.setBoundaryBehaviour(TPActor.BoundaryBehaviour.DIE_AT_BOUNDS);
        actor.addChild(bullet);
    }

}
