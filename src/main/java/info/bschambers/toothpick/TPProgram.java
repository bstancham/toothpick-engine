package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPPlayer;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Usage:
 *
 * TPBase periodically calls update()
 *
 * TPUI uses the various accessor methods to get game assets for display - NOTE: that
 * some of these may return null, so they always need to be checked!
 */
public class TPProgram implements Iterable<TPActor> {

    private String title;
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    protected Rect bounds = new Rect(0, 0, 1000, 800);
    private transient List<ProgramBehaviour> behaviours;
    private TPPlayer player;
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    private boolean pauseForMenu = true;
    protected boolean keepIntersectionPoints = false;
    protected List<Pt> intersectionPoints = new ArrayList<>();
    private boolean smearMode = false;
    private int stopAfter = -1;

    public TPProgram() {
        this("UNTITLED PROGRAM");
    }

    public TPProgram(String title) {
        this.title = title;
        // initialize transient fields
        behaviours = new ArrayList<ProgramBehaviour>();
        player = TPPlayer.NULL;

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
    public void setTitle(String val) { title = val; }

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    public boolean getPauseForMenu() { return pauseForMenu; }
    public void setPauseForMenu(boolean val) { pauseForMenu = val; }

    public int stopAfter() {
        return stopAfter;
    }

    /**
     * <p>Request that the program stop iterating after specified number of frames.</p>
     *
     * <p>If val is a negative number then the program will iterate indefinitely.</p>
     */
    public void setStopAfter(int val) {
        stopAfter = val;
    }

    public boolean isShowIntersections() { return keepIntersectionPoints; }
    public void setShowIntersections(boolean val) { keepIntersectionPoints = val; }

    public boolean isSmearMode() { return smearMode; }
    public void setSmearMode(boolean val) { smearMode = val; }

    public Rect getBounds() { return bounds; }

    public void addBehaviour(ProgramBehaviour pb) {
        behaviours.add(pb);
    }

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

    public void addIntersectionPoint(Pt p) {
        intersectionPoints.add(p);
    }

    public List<String> getInfoLines() {
        List<String> lines = new ArrayList<String>();
        lines.add("num kills: " + getPlayer().getActor().statsNumKills);
        lines.add("num deaths: " + getPlayer().getActor().statsNumDeaths);
        lines.add("num actors: " + actors.size());
        for (ProgramBehaviour pb : behaviours)
            for (String pbLine : pb.getInfoLines())
                lines.add(pbLine);
        return lines;
    }

    /**
     * Update and move the action on by one step.
     */
    public void update() {
        intersectionPoints.clear();
        for (TPActor a : actors)
            a.update(this);
        for (ProgramBehaviour pb : behaviours)
            pb.update(this);
        housekeeping();
    }

    /**
     * Add and remove actors as appropriate.
     */
    private void housekeeping() {
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

    @Override
    public Iterator<TPActor> iterator() { return actors.iterator(); }

}
