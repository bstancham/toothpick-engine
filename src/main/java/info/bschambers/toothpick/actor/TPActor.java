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

    public void setController(TPController ac) {
        setController(ac, false);
    }

    public void setController(TPController ac, boolean keepExistingPosition) {
        if (keepExistingPosition) {
            ac.setPos(controller.pos());
            ac.setAngle(controller.angle());
        }
        controller = ac;
    }

    public int numChildren() { return children.size(); }

    public TPActor getChild(int index) { return children.get(index); }

    public void update() {
        controller.update();
        form.update(controller);
    }

}
