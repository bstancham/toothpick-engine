package info.bschambers.toothpick.actor;

public abstract class TPPart {

    protected TPForm form = null;
    protected boolean alive = true;

    public boolean isAlive() { return alive; }

    public TPForm getForm() { return form; }

    public void setForm(TPForm form) { this.form = form; }

    public TPActor getActor() {
        if (form == null)
            return null;
        return form.getActor();
    }

    public abstract void setState(double x, double y, double angle);

    public abstract TPPart copy();

}
