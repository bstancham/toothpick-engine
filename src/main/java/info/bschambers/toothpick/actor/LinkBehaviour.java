package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

/**
 * <p>Abstract parent class for part-behaviours which are specific to {@link TPLink}.</p>
 */
public abstract class LinkBehaviour extends PartBehaviour {

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param part The {@code TPPart} to act on.
     */
    @Override
    public void update(TPPart selfPart, TPActor selfActor) {
        if (selfPart instanceof TPLink)
            updateLink((TPLink) selfPart, selfActor);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param killer The {@code TPPart} which caused this death - may be {@code null}.
     */
    @Override
    public void die(TPPart selfPart, TPActor selfActor,
                    TPPart killerPart, TPActor killerActor, Pt p) {
        if (selfPart instanceof TPLink && killerPart instanceof TPLink)
            linkDie((TPLink) selfPart, selfActor, (TPLink) killerPart, killerActor, p);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param victim The {@code TPPart} which was killed.
     */
    @Override
    public void kill(TPPart selfPart, TPActor selfActor,
                     TPPart victimPart, TPActor victimActor, Pt p) {
        if (selfPart instanceof TPLink && victimPart instanceof TPLink)
            killLink((TPLink) selfPart, selfActor, (TPLink) victimPart, victimActor, p);
    }

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param tpl The {@code TPLine} to act on.
     */
    public void updateLink(TPLink selfLink, TPActor selfActor) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param killer The {@code TPLine} which caused this death - may be {@code null}.
     */
    public void linkDie(TPLink selfLink, TPActor selfActor,
                        TPLink killerLink, TPActor killerActor, Pt p) {}

    /**
     * <p>Does nothing - child classes may override this method.</p>
     *
     * @param victim The {@code TPLine} which was killed.
     */
    public void killLink(TPLink selfLink, TPActor selfActor,
                         TPLink victimLink, TPActor victimActor, Pt p) {}

}
