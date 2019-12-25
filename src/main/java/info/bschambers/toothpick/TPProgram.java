package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPPlayer;
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
    protected Rect bounds = new Rect(0, 0, 1000, 800);
    private TPPlayer player = TPPlayer.NULL;
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    private boolean pauseForMenu = false;
    protected boolean keepIntersectionPoints = false;
    protected List<Pt> intersectionPoints = new ArrayList<>();
    private boolean smearMode = false;

    public TPProgram(String title) {
        this.title = title;
        init();
    }

    /**
     * <p>Initialise the program - if already running, then return the program to it's
     * starting state.</p>
     *
     * <p>Child classes should override this method to do setup - the default
     * implemetation simply resets the player and clears the actors lists.</p>
     */
    public void init() {
        actors.clear();
        toAdd.clear();
        toRemove.clear();
        player.reset();
        addActor(player.getActor());
    }

    public String getTitle() { return title; }

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    public boolean getPauseForMenu() { return pauseForMenu; }
    public void setPauseForMenu(boolean val) { pauseForMenu = val; }

    public boolean isShowIntersections() { return keepIntersectionPoints; }
    public void setShowIntersections(boolean val) { keepIntersectionPoints = val; }

    public boolean isSmearMode() { return smearMode; }
    public void setSmearMode(boolean val) { smearMode = val; }

    public Rect getBounds() { return bounds; }

    public int numActors() { return actors.size(); }
    public TPActor getActor(int index) { return actors.get(index); }

    public TPPlayer getPlayer() { return player; }

    public void setPlayer(TPPlayer player) {
        this.player = player;
        if (!actors.contains(player.getActor()))
            actors.add(player.getActor());
    }

    public void revivePlayer(boolean retainStats) {
        System.out.println("TPProgram.revivePlayer()");
        player.reset(retainStats);
        if (!actors.contains(player.getActor()))
            toAdd.add(player.getActor());
    }

    public List<Pt> getIntersectionPoints() { return intersectionPoints; }

    public List<String> getInfoLines() { return new ArrayList<String>(); }

    /**
     * Update and move the action on by one step.
     */
    public void update() {
        action();
        interactions();
        housekeeping();
    }

    protected void action() {
        for (TPActor a : actors)
            a.update(this);
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

    public boolean contains(TPActor a) {
        for (TPActor b : actors)
            if (a == b)
                return true;
        return false;
    }

}
