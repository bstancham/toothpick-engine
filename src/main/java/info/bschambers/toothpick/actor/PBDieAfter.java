package info.bschambers.toothpick.actor;

/**
 * <p>Part dies after a set interval.</p>
 */
public class PBDieAfter extends PartBehaviour {

    private int interval = 100;
    private int counter = 0;

    public PBDieAfter() {}

    public PBDieAfter(int interval) {
        this.interval = interval;
    }

    @Override
    public PBDieAfter copy() {
        return new PBDieAfter(interval);
    }

    @Override
    public void update(TPPart part) {
        counter++;
        if (counter > interval)
            part.die();
    }

}
