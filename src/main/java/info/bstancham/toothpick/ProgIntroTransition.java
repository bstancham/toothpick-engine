package info.bstancham.toothpick;

import info.bstancham.toothpick.actor.TPActor;
import info.bstancham.toothpick.actor.TPFactory;

/**
 * <p>Runs a simple intro-sequence and then deactivates.</p>
 *
 * <ul>
 * <li>The play area is positioned just off the edge of the visible screen area to the
 * right and the intro-message is placed in the center of it.</li>
 * <li>The play area slides into the center of the screen and then after a short delay the
 * intro message slides off the top.</li>
 * </ul>
 */
public class ProgIntroTransition implements ProgramBehaviour {

    private String message;
    private boolean triggered = false;
    private boolean finished = false;
    private double offsetGoal = 0;
    private double offset = 0;
    private int messagePause = 1000;
    private int messageCount = 0;
    private TPActor textActor = null;

    public ProgIntroTransition() {
        this("...");
    }

    public ProgIntroTransition(String message) { this.message = message; }
    
    @Override
    public ProgIntroTransition copy() {
        ProgIntroTransition out = new ProgIntroTransition(message);
        out.triggered = triggered;
        out.finished = finished;
        out.offsetGoal = offsetGoal;
        out.offset = offset;
        out.messagePause = messagePause;
        out.messageCount = messageCount;
        if (textActor != null)
            out.textActor = textActor.copy();
        return out;
    }

    @Override
    public String getSingletonGroup() {
        return ProgramBehaviour.INTRO_TRANSITION_ID;
    }

    @Override
    public void reset() {
        triggered = false;
        messageCount = 0;
    }

    @Override
    public void update(TPProgram prog) {
        if (!triggered) {
            // this will run only once (the first time) - do all setup
            triggered = true;
            TPGeometry geom = prog.getGeometry();
            offsetGoal = -(geom.getWidth() / 2);
            offset = geom.getXCenter() + geom.getWidth();
            geom.xOffset = offset;
            textActor = TPFactory.textActor(prog, message);
            textActor.setPos(TPFactory.centerPos(prog));
            textActor.x = geom.getWidth() / 2;
            textActor.y = geom.getHeight() / 2;
            textActor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.DIE_AT_BOUNDS);
            prog.addActor(textActor);
        } else if (!finished) {
            if (offset > offsetGoal) {
                // slide play-area into center of screen
                offset -= 4;
                prog.getGeometry().xOffset = offset;
            } else if (messageCount < messagePause) {
                // leave message on screen for a while
                messageCount++;
            } else {
                // set message moving up to top of screen where it will die
                textActor.yInertia = -2;
                finished = true;
            }
        }
    }

}
