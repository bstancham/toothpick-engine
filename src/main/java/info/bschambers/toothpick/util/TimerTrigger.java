package info.bschambers.toothpick.util;

public class TimerTrigger implements Trigger {

    private boolean triggered = false;
    private int delay;
    private int countdown;

    public TimerTrigger(int delay) {
        this.delay = delay;
        countdown = delay;
    }

    @Override
    public void reset() {
        triggered = false;
        countdown = delay;
    }

    @Override
    public void update() {
        if (countdown <= 0)
            triggered = true;
        else
            countdown--;
    }

    @Override
    public boolean isTriggered() { return triggered; }

}
