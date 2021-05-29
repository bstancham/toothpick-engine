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
    private int levelNum = 1;
    private List<TPPlayer> players = new ArrayList<>();

    public TPSequencePlatform(String title) {
        super(title);
    }

    public int numPlayers() { return players.size(); }

    public TPPlayer getPlayer(int i) { return players.get(i); }

    public void addPlayer(TPPlayer p) { players.add(p); }

    public void clearPlayers() { players.clear(); }

    public void addProgram(TPProgram p) {
        programs.add(p);
        if (programs.size() == 1) {
            levelIndex = 0;
            setProgram(programs.get(0));
        }
    }

    @Override
    protected void setProgram(TPProgram prog) {
        prog.reset();
        super.setProgram(prog);
        prog.addBehaviour(new PBIntroTransition(levelNum + ": " + prog.getTitle()));
        prog.addBehaviour(new PBOutroTransition("WAVE COMPLETED!"));
    }

    @Override
    public void update() {
        // make sure that players are set up for current program
        boolean updatePlayers = getProgram().numPlayers() != numPlayers();
        if (updatePlayers) {
            updateProgramPlayers();
        } else {
            // check that each player matches
            for (int i = 0; i < numPlayers(); i++) {
                if (getPlayer(i) != getProgram().getPlayer(i)) {
                    updatePlayers = true;
                }
            }
            if (updatePlayers) {
                updateProgramPlayers();
            }
        }

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
            // move players into new program
            updateProgramPlayers();
        }
    }

    /** Make sure that current program has same players as sequence-platform. */
    private void updateProgramPlayers() {
        getProgram().clearPlayers();
        for (int i = 0; i < numPlayers(); i++)
            getProgram().addPlayer(getPlayer(i));
    }
}
