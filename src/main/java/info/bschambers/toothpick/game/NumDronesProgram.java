package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.PlayerController;
import java.util.function.Supplier;
import java.util.List;

/**
 * <p>Program maintains a set number of drones, spawning a new one after a drone is
 * killed.</p>
 *
 * <p>New drones a spawned from a list of actor-factory methods.</p>
 */
public class NumDronesProgram extends ToothpickProgram {

    private int targetNum = 3;
    private int spawnDelay = 0;

    private Supplier<TPActor> droneSupplier = () -> TPFactory.randSingleLineEdgePos(getBounds());

    public NumDronesProgram(String title) {
        super(title);
        TPActor player = TPFactory.playerLine(100);
        player.getController().setPos(getBounds().center());
        setPlayer(player);
    }

    @Override
    public void update() {
        doSpawning();
        super.update();
    }

    private void doSpawning() {
        if (numDrones() < targetNum) {
            if (spawnDelay < 1) {
                TPActor drone = droneSupplier.get();
                addActor(drone);
                spawnDelay = (int) (Math.random() * 200);
            } else {
                spawnDelay--;
            }
        }
    }

    @Override
    public List<String> getInfoLines() {
        List<String> lines = super.getInfoLines();
        lines.add("drone-num target: " + targetNum);
        lines.add("spawn-delay: " + spawnDelay);
        return lines;
    }

    private int numDrones() {
        int n = 0;
        for (TPActor a : actors) {
            if (!(a.getController() instanceof PlayerController)) {
                n++;
            }
        }
        return n;
    }

}
