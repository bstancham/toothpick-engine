package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;

/**
 * <p>Like {@link PBToothpickPhysics}, but non-player actors do not interact with
 * one-another. Also, player-actors do not interact with one-another</p>
 */
public class PBToothpickPhysicsLight extends PBToothpickPhysics {

    @Override
    public PBToothpickPhysicsLight copy() { return this; }

    @Override
    public void update(TPProgram prog) {
        for (int i = 0; i < prog.numPlayers(); i++) {
            TPActor player = prog.getPlayer(i).getActor();
            if (player.isAlive())
                for (TPActor a : prog)
                    if (!a.isPlayer())
                        interact(prog, a, player);
        }
    }

}
