package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPFactory;
import java.util.function.Function;

public class MaintainDronesNum implements ProgramBehaviour, TPEncodingHelper {

    private int dronesGoal = 5;
    private int spawnDelay = 0;
    private Function<TPProgram, TPActor> droneFunc
        = (prog) -> TPFactory.lineActor(prog);

    @Override
    public String[] getInfoLines() {
        return new String[] {
            "drone-num goal: " + dronesGoal,
            "spawn-delay: " + spawnDelay,
        };
    }

    @Override
    public String getSingletonGroup() {
        return "maintain-drones-num";
    }

    @Override
    public void update(TPProgram prog) {
        if (numDrones(prog) < dronesGoal) {
            if (spawnDelay < 1) {
                prog.addActor(droneFunc.apply(prog));
                spawnDelay = (int) (Math.random() * 600);
            } else {
                spawnDelay--;
            }
        }
    }

    private int numDrones(TPProgram prog) {
        int n = 0;
        for (TPActor a : prog)
            if (a != prog.getPlayer().getActor())
                n++;
        return n;
    }

    public int getDronesGoal() {
        return dronesGoal;
    }

    public void setDronesGoal(int val) {
        dronesGoal = val;
    }

    public void setDroneFunc(Function<TPProgram, TPActor> func) {
        droneFunc = func;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Integer.class, getDronesGoal(), "setDronesGoal");
        return params;
    }

}
