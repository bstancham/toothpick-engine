package info.bschambers.toothpick.actor;

import java.util.List;
import java.util.ArrayList;

public class TPActor {

    private TPController controller;
    private TPForm form;
    private TPActor parent = null;
    private List<TPActor> children = new ArrayList<>();

    public TPActor(TPForm form, TPController controller) {
        this.form = form;
        this.controller = controller;
    }

    public TPForm getForm() { return form; }

    public TPController getController() { return controller; }
    public void setController(TPController ac) { controller = ac; }

}
