package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public abstract class PartBehaviour {

    public abstract PartBehaviour copy();

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param part The {@code TPPart} to act on.
     */
    public void update(TPPart selfPart, TPActor selfActor) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param killer The {@code TPPart} which caused this death - may be {@code null}.
     */
    public void die(TPPart selfPart, TPActor selfActor,
                    TPPart killerPart, TPActor killerActor, Pt p) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param victim The {@code TPPart} which was killed.
     */
    public void kill(TPPart selfPart, TPActor selfActor,
                     TPPart victimPart, TPActor victimActor, Pt p) {}

}
