package info.bstancham.toothpick.actor;

public class TrigActionSpreadShooter extends TrigActionShooter {

    private int numBullets;
    private double spreadWidth;

    public TrigActionSpreadShooter() {
        this(200, 3, Math.PI * 0.05);
    }

    public TrigActionSpreadShooter(int delay, int numBullets, double spreadWidth) {
        this(new TPActor(TPFactory.singleLineFormHoriz(50)), delay, numBullets, spreadWidth);
    }

    public TrigActionSpreadShooter(TPActor bulletArchetype,
                                   int delay, int numBullets, double spreadWidth) {
        super(bulletArchetype, delay);
        this.numBullets = numBullets;
        this.spreadWidth = spreadWidth;
    }

    @Override
    protected void triggerAction(TPActor actor) {
        double angle = -(spreadWidth / 2);
        double angleStep = spreadWidth / (numBullets - 1);
        for (int i = 0; i < numBullets; i++) {
            addBullet(actor, angle);
            angle += angleStep;
        }
    }

}
