package info.bschambers.toothpick.actor;

import java.util.List;
import java.util.ArrayList;

public class Actor {

    private ActorController controller;
    private ActorForm form;
    private Actor parent = null;
    private List<Actor> children = new ArrayList<>();

    public Actor(ActorForm form, ActorController controller) {
        this.form = form;
        this.controller = controller;
    }

    public ActorForm getForm() { return form; }

    public ActorController getController() { return controller; }
    public void setController(ActorController ac) { controller = ac; }

}
