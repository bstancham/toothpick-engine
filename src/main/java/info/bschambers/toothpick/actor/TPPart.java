package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncodingHelper;

public abstract class TPPart implements TPEncodingHelper {

    protected TPForm form = null;

    public TPForm getForm() {
        return form;
    }

    public void setForm(TPForm form) {
        this.form = form;
    }

    public TPActor getActor() {
        if (form == null)
            return null;
        return form.getActor();
    }

    public abstract void update(double x, double y, double angle);

    public abstract TPPart copy();

}
