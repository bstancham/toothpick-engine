package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncodingHelper;
import info.bstancham.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public abstract class TPPart implements TPEncodingHelper {

    private boolean alive = true;
    private boolean passive = false;
    private List<PartBehaviour> behaviours = new ArrayList<>();

    /**
     * Runs part-behaviours - child classes should override and call super.update() at the
     * end.
     */
    public void update(TPActor a) {
        for (PartBehaviour pb : behaviours)
            pb.update(this, a);
    }

    /**
     * <p>NOTE: child classes implementing {@code copy} should utilise
     * {@link copyPartProperties}.</p>
     */
    public abstract TPPart copy();

    /**
     * <p>Copy properties and all behaviours from the input-part.</p>
     */
    public void copyPartProperties(TPPart part) {
        alive = part.alive;
        passive = part.passive;
        for (PartBehaviour pb : part.behaviours)
            addBehaviour(pb.copy());
    }

    public void addBehaviour(PartBehaviour pb) { behaviours.add(pb); }

    public boolean isAlive() { return alive; }

    public boolean isPassive() { return passive; }

    public void setPassive(boolean val) { passive = val; }

    /**
     * <p>Die, without running any part-behaviours.</p>
     */
    public void die(TPActor selfActor) { alive = false; }

    public void die(TPActor selfActor, TPPart killerPart, TPActor killerActor, Pt p) {
        alive = false;
        for (PartBehaviour pb: behaviours)
            pb.die(this, selfActor, killerPart, killerActor, p);
    }

    public void kills(TPActor selfActor, TPPart victimPart, TPActor victimActor, Pt p) {
        for (PartBehaviour pb: behaviours)
            pb.kill(this, selfActor, victimPart, victimActor, p);
    }

}
