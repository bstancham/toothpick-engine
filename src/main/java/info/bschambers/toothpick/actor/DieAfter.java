package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

/**
 * <p>Actor dies after a set interval.</p>
 */
public class DieAfter implements TPBehaviour {

    private int interval = 100;
    private int counter = 0;

    public DieAfter() {}

    public DieAfter(int interval) {
        this.interval = interval;
    }

    @Override
    public void update(TPProgram prog, TPActor a) {
        counter++;
        if (counter > interval)
            a.getForm().setAlive(false);
    }

}
