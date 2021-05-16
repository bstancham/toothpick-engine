package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;

/**
 * <p>Like {@link PBToothpickPhysics}, but non-player actors do not interact with
 * one-another.</p>
 */
public class PBToothpickPhysicsLight extends PBToothpickPhysics {

    @Override
    public PBToothpickPhysicsLight copy() { return this; }

    @Override
    public void update(TPProgram prog) {
        TPActor player = prog.getPlayer().getActor();
        if (player.isAlive())
            for (TPActor a : prog)
                if (a != player)
                    interact(prog, a, player);
    }

}
