package info.bschambers.toothpick.actor;

public class DieAfter implements PartBehaviour {

    private int interval = 100;
    private int counter = 0;

    public DieAfter() {}

    public DieAfter(int interval) {
        this.interval = interval;
    }

    @Override
    public void action(TPPart part) {
        counter++;
        if (counter > interval)
            part.die();
    }

}
