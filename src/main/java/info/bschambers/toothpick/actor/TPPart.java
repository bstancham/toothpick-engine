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

    /**
     * <p>Returns true, if this part has meaningful dimensions. If this method returns
     * false then the values returned by {@link xMin}, {@link yMin}, {@link xMax} and
     * {@link yMax} should not be considered meaningful.</p>
     *
     * <p>The default implementation returns false.</p>
     */
    public boolean hasDimensions() {
        return false;
    }

    public int xMin() {
        return 0;
    }

    public int yMin() {
        return 0;
    }

    public int xMax() {
        return 0;
    }

    public int yMax() {
        return 0;
    }

    public void translate(int x, int y) {}

}
