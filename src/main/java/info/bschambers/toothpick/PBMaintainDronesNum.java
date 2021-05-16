package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPFactory;
import info.bschambers.toothpick.util.RandomChooser;
import java.util.function.Function;

public class PBMaintainDronesNum implements ProgramBehaviour, TPEncodingHelper {

    private int goalNum = 5;
    private int spawnDelay = 0;
    private Function<TPProgram, Integer> goalSetter = null;
    // start off with one default chooser option
    private RandomChooser<Function<TPProgram, TPActor>> chooser =
        new RandomChooser<Function<TPProgram, TPActor>>((TPProgram prog) ->
                                                        TPFactory.lineActor(prog));

    @Override
    public PBMaintainDronesNum copy() {
        PBMaintainDronesNum out = new PBMaintainDronesNum();
        out.goalNum = goalNum;
        out.spawnDelay = spawnDelay;
        out.goalSetter = goalSetter;
        out.chooser = chooser.copy();
        return out;
    }

    @Override
    public String[] getInfoLines() {
        return new String[] {
            "drone-num goal: " + goalNum,
            "drone-spawn delay: " + spawnDelay,
        };
    }

    @Override
    public String getSingletonGroup() {
        return "maintain-drones-num";
    }

    @Override
    public void update(TPProgram prog) {

        if (goalSetter != null)
            goalNum = goalSetter.apply(prog);
        
        if (numDrones(prog) < goalNum) {
            if (spawnDelay < 1) {
                prog.addActor(makeDrone(prog));
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
        return goalNum;
    }

    public void setDronesGoal(int val) {
        goalNum = val;
    }

    public void incrDronesGoal(int amt) {
        goalNum += amt;
        if (goalNum < 0)
            goalNum = 0;
    }

    /**
     * Add a drone-making function to the RandomChooser.
     */
    public void addDroneFunc(Function<TPProgram, TPActor> func) {
        chooser.add(func);
    }

    /**
     * Add a drone-making function to the RandomChooser.
     */
    public void addDroneFunc(String label, int weight, Function<TPProgram, TPActor> func) {
        chooser.add(label, weight, func);
    }

    /**
     * Set {@code func} as the drone-making function, discarding any existing drone-functions.
     */
    public void setDroneFunc(Function<TPProgram, TPActor> func) {
        chooser = new RandomChooser<Function<TPProgram, TPActor>>(func);
    }

    public void setGoalSetter(Function<TPProgram, Integer> goalSetter) {
        this.goalSetter = goalSetter;
    }

    public RandomChooser<Function<TPProgram, TPActor>> getChooser() {
        return chooser;
    }

    private TPActor makeDrone(TPProgram prog) {
        Function<TPProgram, TPActor> func = chooser.get();
        return func.apply(prog);
    }



    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Integer.class, getDronesGoal(), "setDronesGoal");
        return params;
    }

}
