package info.bschambers.toothpick;

/**
 * <p>Sets program-finished flag by calling {@code TPProgram.setFinished(true)} after
 * player has achieved a certain number of kills.</p>
 *
 * <p>NOTE: the flag is only set once, so once it has been triggered once, even if the
 * conditions are met it will not trigger again (unless {@link reset} is called). This is
 * usually the most useful behaviour since it allows other behaviours such as
 * {@link OutroTransition} to use this as a trigger. In the case of {@code OutroTransition}
 * what happens is something like this:</p>
 *
 * <ul>
 * <li>A {@link TPProgram} is running in {@link TPSequencePlatform}.</li>
 * <li>{@code FinishAfterNumKills} triggers and sets the program-finished flag.</li>
 * <li>{@code OutroTransition} notices that the program-finished flag has been set, sets
 * the transition-sequence in motion and un-sets it so that the {@code TPSequencePlatform}
 * won't end the program.</li>
 * <li>When the transition-sequence is finished, {@code OutroTransition} sets the
 * program-finished flag so that {@code TPSequencePlatform} will switch to the next
 * program.</li>
 * </ul>
 *
 * <p>NOTE: in order for the above sequence to work {@code FinishAfterNumKills} must be
 * put into the program's behaviours list before {@code OutroTransition}.</p>
 */
public class FinishAfterNumKills implements ProgramBehaviour {

    private int targetNum = 10;
    private int startNum = -1;
    private int remaining = 10;
    private boolean triggered = false;

    public FinishAfterNumKills() {}

    public FinishAfterNumKills(int targetNum) {
        this.targetNum = targetNum;
    }

    @Override
    public String[] getInfoLines() {
        return new String[] {
            "REMAINING: " + remaining + " of " + targetNum
        };
    }

    @Override
    public void reset() {
        startNum = -1;
        triggered = false;
    }

    @Override
    public void update(TPProgram prog) {
        if (startNum < 0) {
            startNum = prog.getPlayer().getActor().numKills;
        } else {
            int current = prog.getPlayer().getActor().numKills - startNum;
            remaining = targetNum - current;
            if (remaining <= 0) {
                if (!triggered) {
                    prog.setFinished(true);
                    triggered = true;
                }
            }
        }
    }

}
