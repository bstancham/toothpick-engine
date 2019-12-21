package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.PlayerController;
import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage:
 *
 * TPBase periodically calls update()
 *
 * TPUI uses the various accessor methods to get game assets for display - NOTE: that
 * some of these may return null, so they always need to be checked!
 */
public abstract class TPProgram {

    public static final TPProgram NULL = new TPProgram("NULL PROGRAM") {};

    private String title;
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    protected Rect bounds = new Rect(0, 0, 640, 430);
    private PlayerController playerCtrl = PlayerController.NULL;
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    protected boolean keepIntersectionPoints = true;
    protected List<Pt> intersectionPoints = new ArrayList<>();
    private boolean pauseForMenu = false;

    public TPProgram(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    public boolean getPauseForMenu() { return pauseForMenu; }
    public void setPauseForMenu(boolean val) { pauseForMenu = val; }

    public Rect getBounds() { return bounds; }

    public int numActors() { return actors.size(); }
    public TPActor getActor(int index) { return actors.get(index); }

    public PlayerController getPlayerController() { return playerCtrl; }
    public void setPlayerController(PlayerController val) { playerCtrl = val; }

    /**
     * Sets the player-controller and attaches it to the current player-actor.
     */
    public void changePlayerController(PlayerController ctrl) {
        // find current player-actor
        TPActor currentActor = null;
        for (TPActor a : actors)
            if (a.getController() == getPlayerController())
                currentActor = a;

        if (currentActor != null)
            currentActor.setController(ctrl);

        ctrl.setPos(playerCtrl.pos());
        ctrl.setAngle(playerCtrl.angle());

        setPlayerController(ctrl);
    }

    /**
     * <p>Add actor (if not already present) and set as player.</p>
     *
     * <p>Does two things:</p>
     *
     * <ul>
     *
     * <li>Add actor if not already present.</li>
     *
     * <li>Set actor's controller as the player-controller, unless not an instance of
     * PlayerController - then set the existing player-controller as controller for
     * actor.</li>
     *
     * </ul>
     */
    public void setPlayer(TPActor player) {
        if (player.getController() instanceof PlayerController) {
            setPlayerController((PlayerController) player.getController());
        } else {
            player.setController(playerCtrl);
        }
        if (!actors.contains(player))
            addActor(player);
    }

    public List<Pt> getIntersectionPoints() { return intersectionPoints; }

    public List<String> getInfoLines() { return new ArrayList<String>(); }

    /**
     * Update and move the action on by one step.
     */
    public void update() {
        action();
        boundaryCheck();
        interactions();
        housekeeping();
    }

    protected void action() {
        for (TPActor a : actors)
            a.update();
    }

    /** Wrap-around screen edges */
    protected void boundaryCheck() {
        for (TPActor a : actors)
            a.getController().boundaryCheck(bounds);
    }

    protected void interactions() {}

    protected void housekeeping() {
        // garbage collection
        for (TPActor a : actors)
            if (!a.isAlive())
                toRemove.add(a);
        for (TPActor a : toRemove)
            actors.remove(a);
        // add new actors
        for (TPActor a : toAdd)
            actors.add(a);
        // clear lists
        toRemove.clear();
        toAdd.clear();
    }

    public void addActor(TPActor a) {
        toAdd.add(a);
    }

}
