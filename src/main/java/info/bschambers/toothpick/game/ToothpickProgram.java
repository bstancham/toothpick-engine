package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.Actor;
import info.bschambers.toothpick.ui.GameUI;
import java.util.ArrayList;
import java.util.List;

public class ToothpickProgram extends GameProgram {

    private List<Actor> toAdd = new ArrayList<>();
    private List<Actor> toRemove = new ArrayList<>();

    public ToothpickProgram(String title) {
        super(title);
    }

    @Override
    public void update() {
        action();
        housekeeping();
    }

    private void action() {
        for (Actor a : actors)
            a.getController().update();
    }

    private void housekeeping() {
        // garbage collection
        for (Actor a : toRemove)
            actors.remove(a);
        // add new actors
        for (Actor a : toAdd)
            actors.add(a);
        // clear lists
        toRemove.clear();
        toAdd.clear();
    }

    public void addActor(Actor a) {
        toAdd.add(a);
    }

}
