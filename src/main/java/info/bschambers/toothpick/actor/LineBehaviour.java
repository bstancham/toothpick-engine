package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

/**
 * <p>Abstract parent class for part-behaviours which are specific to {@link TPLine}.</p>
 */
public abstract class LineBehaviour extends PartBehaviour {

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param part The {@code TPPart} to act on.
     */
    @Override
    public void update(TPPart thisPart) {
        if (thisPart instanceof TPLine)
            updateLine((TPLine) thisPart);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param killer The {@code TPPart} which caused this death - may be {@code null}.
     */
    @Override
    public void die(TPPart thisPart, TPPart killer, Pt p) {
        if (thisPart instanceof TPLine && killer instanceof TPLine)
            lineDie((TPLine) thisPart, (TPLine) killer, p);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param victim The {@code TPPart} which was killed.
     */
    @Override
    public void kill(TPPart thisPart, TPPart victim, Pt p) {
        if (thisPart instanceof TPLine && victim instanceof TPLine)
            killLine((TPLine) thisPart, (TPLine) victim, p);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param tpl The {@code TPLine} to act on.
     */
    public void updateLine(TPLine tpl) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param killer The {@code TPLine} which caused this death - may be {@code null}.
     */
    public void lineDie(TPLine tpl, TPLine killer, Pt p) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param victim The {@code TPLine} which was killed.
     */
    public void killLine(TPLine tpl, TPLine victim, Pt p) {}

}
