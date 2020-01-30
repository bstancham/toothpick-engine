package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncodingHelper;
import java.util.ArrayList;
import java.util.List;

public abstract class TPPart implements TPEncodingHelper {

    protected TPForm form = null;
    private List<PartBehaviour> behaviours = new ArrayList<>();
    private List<PartBehaviour> deathBehaviours = new ArrayList<>();
    private boolean alive = true;
    private boolean passive = false;

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

    public void update(double x, double y, double angle) {
        for (PartBehaviour pb : behaviours)
            pb.action(this);
    }

    /**
     * <p>WARNING: child classes implementing {@code copy} should utilise
     * {@link copyBehaviours}.</p>
     */
    public abstract TPPart copy();

    /**
     * <p>Copy all behaviours and death-behaviours from {@code part} to this
     * {@code TPPart}.</p>
     */
    public void copyBehaviours(TPPart part) {
        for (PartBehaviour pb : part.behaviours)
            addBehaviour(pb.copy());
        for (PartBehaviour pb : part.deathBehaviours)
            addDeathBehaviour(pb.copy());
    }

    public void addBehaviour(PartBehaviour pb) {
        behaviours.add(pb);
    }

    public void addDeathBehaviour(PartBehaviour pb) {
        deathBehaviours.add(pb);
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isPassive() {
        return passive;
    }

    public void setPassive(boolean val) {
        passive = val;
    }

    public void die() {
        alive = false;
        for (PartBehaviour pb: deathBehaviours)
            pb.action(this);
    }

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
