package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;

/**
 * <p>Actor dies after a set interval.</p>
 */
public class DieAfter implements ActorBehaviour {

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
