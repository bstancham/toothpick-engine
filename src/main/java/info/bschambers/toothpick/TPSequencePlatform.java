package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPFactory;
import info.bschambers.toothpick.actor.TPPlayer;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Manages a sequence of programs and the transitions between them.</p>
 */
public class TPSequencePlatform extends TPPlatform {

    private List<TPProgram> programs = new ArrayList<>();
    private int levelIndex = 0;
    private TPPlayer player = TPPlayer.NULL;
    private int levelNum = 1;

    public TPSequencePlatform(String title) {
        super(title);
    }

    public TPPlayer getPlayer() {
        return player;
    }

    public void setPlayer(TPPlayer player) {
        this.player = player;
    }

    public void addProgram(TPProgram p) {
        programs.add(p);
        if (programs.size() == 1) {
            levelIndex = 0;
            setProgram(programs.get(0));
        }
    }

    @Override
    protected void setProgram(TPProgram prog) {
        prog.init();
        super.setProgram(prog);
        prog.addBehaviour(new IntroTransition(levelNum + ": " + prog.getTitle()));
        prog.addBehaviour(new OutroTransition("WAVE COMPLETED!"));
    }

    @Override
    public void update() {
        // make sure that player is set up for current program
        if (getProgram().getPlayer() != getPlayer())
            getProgram().setPlayer(getPlayer());
        // update current program
        super.update();
        // if program is finished, transition to next in sequence
        if (getProgram().isFinished()) {
            System.out.println("program ended: index=" + levelIndex);
            levelIndex++;
            levelNum++;
            if (levelIndex >= programs.size())
                levelIndex = 0;
            setProgram(programs.get(levelIndex));
            getProgram().setPlayer(getPlayer());
        }
    }

}
