package info.bschambers.toothpick;

/**
 * <p>Sets program-finished flag after player has achieved a certain number of kills.</p>
 */
public class FinishAfterNumKills implements ProgramBehaviour {

    private int targetNum = 10;
    private int startNum = -1;
    private int remaining = 10;

    @Override
    public String[] getInfoLines() {
        return new String[] {
            "REMAINING: " + remaining + " of " + targetNum
        };
    }

    @Override
    public void reset() {
        startNum = -1;
    }

    @Override
    public void update(TPProgram prog) {
        if (startNum < 0) {
            startNum = prog.getPlayer().getActor().numKills;
        } else {
            int current = prog.getPlayer().getActor().numKills - startNum;
            remaining = targetNum - current;
            if (remaining <= 0) {
                prog.setFinished(true);
            }
        }
    }

}
