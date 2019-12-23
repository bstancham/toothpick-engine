package info.bschambers.toothpick.game;

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

    public static final TPProgram NULL = new TPProgram("NULL PROGRAM") {
            @Override
            public void reset() {}
        };

    private String title;
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    protected Rect bounds = new Rect(0, 0, 640, 430);
    private TPPlayer player = TPPlayer.NULL;
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    protected boolean keepIntersectionPoints = true;
    protected List<Pt> intersectionPoints = new ArrayList<>();
    private boolean pauseForMenu = false;

    public TPProgram(String title) {
        this.title = title;
    }

    /** Return the program to it's starting state. */
    public void reset() {
        System.out.println("TPProgram.reset()");
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

    public TPPlayer getPlayer() { return player; }

    public void setPlayer(TPPlayer player) {
        this.player = player;
        if (!actors.contains(player.getActor()))
            actors.add(player.getActor());
    }

    public void revivePlayer() {
        System.out.println("TPProgram.revivePlayer()");
        player.reset();
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

}
