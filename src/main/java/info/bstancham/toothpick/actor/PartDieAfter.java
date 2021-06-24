package info.bstancham.toothpick.actor;

/**
 * <p>Part dies after a set interval.</p>
 */
public class PartDieAfter extends PartBehaviour {

    private int interval = 100;
    private int counter = 0;

    public PartDieAfter() {}

    public PartDieAfter(int interval) { this.interval = interval; }

    @Override
    public PartDieAfter copy() { return new PartDieAfter(interval); }

    @Override
    public void update(TPPart selfPart, TPActor selfActor) {
        counter++;
        if (counter > interval)
            selfPart.die(selfActor);
    }

}
