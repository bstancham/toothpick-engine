package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.ui.TPUI;
import java.util.ArrayList;
import java.util.List;

public class ToothpickProgram extends TPProgram {

    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();

    public ToothpickProgram(String title) {
        super(title);
    }

    @Override
    public void update() {
        action();
        housekeeping();
    }

    private void action() {
        for (TPActor a : actors)
            a.getController().update();
    }

    private void housekeeping() {
        // garbage collection
        for (TPActor a : toRemove)
            actors.remove(a);
        // add new actors
        for (TPActor a : toAdd)
            actors.add(a);
        // clear lists
        toRemove.clear();
        toAdd.clear();
    }

    public void addActor(TPActor a) {
        toAdd.add(a);
    }

}
