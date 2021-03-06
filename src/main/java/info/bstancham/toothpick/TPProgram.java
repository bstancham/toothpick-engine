package info.bstancham.toothpick;

import info.bstancham.toothpick.actor.TPActor;
import info.bstancham.toothpick.actor.TPFactory;
import info.bstancham.toothpick.actor.TPPlayer;
import info.bstancham.toothpick.geom.Geom;
import info.bstancham.toothpick.geom.Pt;
import info.bstancham.toothpick.geom.Rect;
import info.bstancham.toothpick.ui.TPUI;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
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
 *
 * <h2>How to Set Up TPProgram</h2>
 *
 * <p>VERY SIMPLE EXAMPLE:
 * <pre>
 * {@code
 * TPProgram prog = new TPProgram("Very Simple Demo Program");
 * // do any setup - add behaviours/actors etc
 * prog.addBehaviour(new ToothpickPhysics());
 * prog.addActor(TPFactory.lineActor(prog));
 * prog.addActor(TPFactory.regularPolygonActor(prog));
 * // initialise, then save state as reset-snapshot
 * prog.init();
 * prog.setResetSnapshot();
 * }
 * </pre>
 * </p>
 */
public class TPProgram implements Iterable<TPActor>, TPEncodingHelper {

    public static final TPProgram NULL = new TPProgram();

    private TPProgramSnapshot resetSnapshot = null;

    // SIGNIFICANT VARIABLES - need to be retained in snapshot
    // environment
    private String title;
    private TPGeometry geom = new TPGeometry();
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    private boolean smearMode = false;
    private boolean rescueChildActors = true;
    public boolean showProgramInfo = true;
    // actors
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPPlayer> players = new ArrayList<>();
    // behaviours
    private List<ProgramBehaviour> behaviours = new ArrayList<>();
    private List<ProgramBehaviour> resetBehaviours = new ArrayList<>();
    // debugging
    public boolean showDiagnosticInfo = true;
    public boolean showPlayersDiagnosticInfo = true;
    protected boolean keepIntersectionPoints = false;
    protected boolean showBoundingBoxes = false;

    // TRANSIENT VARIABLES - discarded in snapshot
    private boolean finished = false;
    private boolean pauseForMenu = true;
    private int pauseAfter = -1;
    private int pauseAfterAmt = 5;
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    private boolean sfxTriggered = false;
    protected List<Pt> intersectionPoints = new ArrayList<>();

    public TPProgram() {
        this("UNTITLED PROGRAM");
    }

    public TPProgram(String title) {
        this.title = title;
        setPlayArea(1000, 800);
    }

    public void setPlayArea(int w, int h) {
        geom.setupAndCenter(w, h);
    }

    public TPProgram(TPProgramSnapshot snapshot) {
        resetSnapshot = snapshot;
        reset(false);
    }

    public TPProgramSnapshot getResetSnapshot() { return resetSnapshot; }

    /**
     * Creates a reset-snapshot from the current program state.
     */
    public void setResetSnapshot() {
        TPProgramSnapshot snapshot = new TPProgramSnapshot();
        snapshot.title = title;
        snapshot.geom = geom.copy();
        snapshot.bgColor = bgColor;
        snapshot.bgImage = bgImage;
        snapshot.smearMode = smearMode;
        snapshot.rescueChildActors = rescueChildActors;
        snapshot.showProgramInfo = showProgramInfo;
        snapshot.showDiagnosticInfo = showDiagnosticInfo;
        snapshot.keepIntersectionPoints = keepIntersectionPoints;
        snapshot.showBoundingBoxes = showBoundingBoxes;
        for (TPActor a : actors) snapshot.actors.add(a.copy());
        for (TPPlayer p : players) snapshot.players.add(p.copy());
        for (ProgramBehaviour pb : behaviours) snapshot.behaviours.add(pb.copy());
        for (ProgramBehaviour pb : resetBehaviours) snapshot.resetBehaviours.add(pb.copy());
        resetSnapshot = snapshot;
    }

    /**
     * <p>Reset the program to a starting state - clear all existing actors and re-add the
     * initial-actors list, reset all behaviours, and set the finished-flag to false.</p>
     *
     * <p>Run any reset-behaviours.</p>
     *
     * <p>To do a full reset, you may want to call {@link resetPlayer} also.</p>
     */
    public void reset() { reset(true); }

    public void reset(boolean invokeResetBehaviours) {

        setFinished(false);

        // clear existing actors and behaviours
        actors.clear();
        players.clear();
        toAdd.clear();
        toRemove.clear();
        behaviours.clear();
        resetBehaviours.clear();

        if (resetSnapshot == null) {
            System.out.println("WARNING: in TPProgram.reset() - no reset snapshot exists!");
        } else {

            // environment
            title = resetSnapshot.title;
            geom = resetSnapshot.geom.copy();
            bgColor = resetSnapshot.bgColor;
            bgImage = resetSnapshot.bgImage;
            smearMode = resetSnapshot.smearMode;
            rescueChildActors = resetSnapshot.rescueChildActors;
            showProgramInfo = resetSnapshot.showProgramInfo;

            // debugging
            showDiagnosticInfo = resetSnapshot.showDiagnosticInfo;
            keepIntersectionPoints = resetSnapshot.keepIntersectionPoints;
            showBoundingBoxes = resetSnapshot.showBoundingBoxes;

            // actors/players
            for (TPActor a: resetSnapshot.actors) actors.add(a.copy());
            for (TPPlayer p: resetSnapshot.players) {
                TPPlayer temp = p.copy();
                players.add(temp);
                actors.add(temp.getActor());
            }
            updateActorsInPlace();

            // behaviours
            for (ProgramBehaviour pb : resetSnapshot.behaviours) behaviours.add(pb);
            for (ProgramBehaviour pb : resetSnapshot.resetBehaviours) resetBehaviours.add(pb);
        }

        if (invokeResetBehaviours) {
            for (ProgramBehaviour pb : behaviours) pb.reset();
            for (ProgramBehaviour pb : resetBehaviours) pb.update(this);
        }
    }

    public void init() {
        updateActorsInPlace();
    }

    public int numPlayers() { return players.size(); }

    public TPPlayer getPlayer(int i) { return players.get(i); }

    public void setPlayer(int i, TPPlayer p) {
        System.out.println("TPProgram.setPlayer(" + i + ") --> " + p);
        // make sure that the old player-actor has been removed
        removeActor(players.get(i).getActor());
        players.set(i, p);
        addActor(p.getActor());
        updateActorsInPlace();
    }

    public void addPlayer(TPPlayer p) {
        System.out.println("TPProgram.addPlayer() --> " + p);
        players.add(p);
        addActor(p.getActor());
        updateActorsInPlace();
    }

    protected boolean playerIsDead(int i) {
        return !getPlayer(i).getActor().isAlive();
    }

    public void revivePlayer(int i, boolean retainStats) {
        System.out.println("TPProgram.revivePlayer(" + i + ")");
        removeActor(players.get(i).getActor());
        players.get(i).reset(retainStats);
        addActor(players.get(i).getActor());
        updateActorsInPlace();
    }

    public void clearPlayers() { players.clear(); }

    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < numPlayers(); i++)
            getPlayer(i).getInputHandler().setKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < numPlayers(); i++)
            getPlayer(i).getInputHandler().setKey(e.getKeyCode(), false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String val) {
        title = val;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color val) {
        bgColor = val;
    }

    public Image getBGImage() {
        return bgImage;
    }

    public void setBGImage(Image val) {
        bgImage = val;
    }

    public boolean getPauseForMenu() {
        return pauseForMenu;
    }

    public void setPauseForMenu(boolean val) {
        pauseForMenu = val;
    }

    /**
     * <p>If menu is active, iterate specified number of times before pausing.</p>
     *
     * <p>If val is a zero then pause immediately.</p>
     *
     * <p>If val is a negative number then do nothing.</p>
     */
    public void setPauseAfter(int val) {
        pauseAfter = val;
        if (pauseAfter > 0)
            pauseForMenu = false;
        else if (pauseAfter == 0)
            pauseForMenu = true;
    }

    public int getPauseAfterAmt() {
        return pauseAfterAmt;
    }

    public void setPauseAfterAmt(int val) {
        pauseAfterAmt = val;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean val) {
        finished = val;
    }

    public boolean isShowIntersections() {
        return keepIntersectionPoints;
    }

    public void setShowIntersections(boolean val) {
        keepIntersectionPoints = val;
    }

    public boolean isShowBoundingBoxes() {
        return showBoundingBoxes;
    }

    public void setShowBoundingBoxes(boolean val) {
        showBoundingBoxes = val;
    }

    public boolean isSmearMode() {
        return smearMode;
    }

    public void setSmearMode(boolean val) {
        smearMode = val;
    }

    public boolean getShowProgramInfo() {
        return showProgramInfo;
    }

    public void setShowProgramInfo(boolean val) {
        showProgramInfo = val;
    }

    public boolean getShowDiagnosticInfo() {
        return showDiagnosticInfo;
    }

    public void setShowDiagnosticInfo(boolean val) {
        showDiagnosticInfo = val;
    }

    public boolean getShowPlayersDiagnosticInfo() {
        return showPlayersDiagnosticInfo;
    }

    public void setShowPlayersDiagnosticInfo(boolean val) {
        showPlayersDiagnosticInfo = val;
    }

    public TPGeometry getGeometry() {
        return geom;
    }

    public void setGeometry(TPGeometry g) {
        geom = g;
    }

    public void fitUI(TPUI ui) {
        getGeometry().setXCenter(ui.getUIWidth() / 2);
        getGeometry().setYCenter(ui.getUIHeight() / 2);
    }

    public void triggerSfx() {
        sfxTriggered = true;
    }

    public boolean isSfxTriggered() {
        return sfxTriggered;
    }

    public void sfxReset() {
        sfxTriggered = false;
    }

    /**
     * <p>Add a program-behaviour. If behaviour {@code pb} is a member of a
     * singleton-group then any existing member of that group will be removed.</p>
     */
    public void addBehaviour(ProgramBehaviour pb) {
        addBehaviour(pb, behaviours);
    }

    public int numBehaviours() {
        return behaviours.size();
    }

    public ProgramBehaviour getBehaviour(int i) {
        return behaviours.get(i);
    }

    /**
     * <p>Add a reset-behaviour. If behaviour {@code pb} is a member of a
     * singleton-group then any existing member of that group will be removed.</p>
     */
    public void addResetBehaviour(ProgramBehaviour pb) {
        addBehaviour(pb, resetBehaviours);
    }

    /**
     * <p>Add a program-behaviour. If behaviour {@code pb} is a member of a
     * singleton-group then any existing member of that group will be removed.</p>
     */
    private void addBehaviour(ProgramBehaviour pb, List<ProgramBehaviour> behaviourList) {
        String groupID = pb.getSingletonGroup();
        if (!groupID.isEmpty())
            removeBehaviourGroup(groupID);
        behaviourList.add(pb);
    }

    public void removeBehaviourGroup(String groupID) {
        removeBehaviourGroup(groupID, behaviours);
    }

    private void removeBehaviourGroup(String groupID, List<ProgramBehaviour> behaviourList) {
        List<ProgramBehaviour> pbToRemove = new ArrayList<>();
        for (ProgramBehaviour other : behaviourList)
            if (other.getSingletonGroup().equals(groupID))
                pbToRemove.add(other);
        for (ProgramBehaviour other : pbToRemove)
            behaviourList.remove(other);

    }

    public int numActors() {
        return actors.size();
    }

    public int numActorsIncChildren() {
        int n = 0;
        for (TPActor a : actors)
            n = countChildren(a, n + 1);
        return n;
    }

    /**
     * Recursive method for counting children of TPActor a.
     *
     * @return The total number of children and sub-children, plus 1 for the initial input
     * TPActor. i.e. if TPActor a has just one child-actor the value returned will be 2.
     */
    private int countChildren(TPActor a, int n) {
        for (int i = 0; i < a.numChildren(); i++)
            n = countChildren(a.getChild(i), n + 1);
        return n;
    }

    public TPActor getActor(int index) {
        return actors.get(index);
    }

    /**
     * <p>Gets first actor who's name matches {@code name}.</p>
     *
     * <p>WARNING! Returns null if named actor does not exist.</p>
     */
    public TPActor getActor(String name) {
        for (TPActor a : actors)
            if (a.name == name)
                return a;
        return null;
    }

    public List<Pt> getIntersectionPoints() {
        return intersectionPoints;
    }

    public void addIntersectionPoint(Pt p) {
        intersectionPoints.add(p);
    }

    public List<String> getInfoLines() {
        List<String> lines = new ArrayList<String>();
        if (showProgramInfo) {
            for (int i = 0; i < numPlayers(); i++) {
                addPlayerInfoLines(i, lines);
                if (showPlayersDiagnosticInfo) {
                    addPlayerDiagnosticLines(i, lines);
                }
            }
        }
        if (showDiagnosticInfo) {
            lines.add("actors (top lvl): " + numActors());
            lines.add("actors (total): " + numActorsIncChildren());
            for (ProgramBehaviour pb : behaviours)
                for (String pbLine : pb.getInfoLines())
                    lines.add(pbLine);
        }
        return lines;
    }

    private void addPlayerInfoLines(int i, List<String> lines) {
        String pNumStr = "PLAYER " + (i + 1);
        if (playerIsDead(i))
            pNumStr += " (DEAD)";
        lines.add(pNumStr);
        lines.add("kills: " + getPlayer(i).getActor().numKills);
        lines.add("deaths: " + getPlayer(i).getActor().numDeaths);
    }

    private void addPlayerDiagnosticLines(int i, List<String> lines) {
        lines.add("angle=" + getPlayer(i).getActor().angle);
        lines.add("x=" + getPlayer(i).getActor().x);
        lines.add("y=" + getPlayer(i).getActor().y);
    }

    /**
     * Update and move the action on by one step.
     */
    public void update() {

        // maybe pause program
        if (pauseAfter > 0) {
            pauseAfter--;
            if (pauseAfter == 0) {
                setPauseForMenu(true);
            }
        }

        // if player is dead, revive on trigger press
        for (int i = 0; i < numPlayers(); i++) {
            if (playerIsDead(i)) {
                getPlayer(i).getActor().update(this);
                if (getPlayer(i).getActor().getActionTrigger()) {
                    System.out.println("... PLAYER " + (i + 1) + ": TRIGGER REVIVAL!");
                    revivePlayer(i, true);
                }
            }
        }

        // update all actors and program behaviours
        intersectionPoints.clear();
        for (TPActor a : actors)
            a.update(this);
        for (ProgramBehaviour pb : behaviours)
            pb.update(this);
        housekeeping();
    }

    /**
     * Update all actors, but without advancing the action.
     */
    public void updateActorsInPlace() {
        housekeeping();
        for (TPActor a : actors)
            a.updateForm();
    }

    /**
     * <p>Add and remove actors as appropriate.</p>
     *
     * <p>This is called by {@link update}, however you may occasionally want to call it
     * manually to update actors without advancing movement.</p>
     */
    private void housekeeping() {
        // remove
        for (TPActor a : actors)
            if (!a.isAlive()) {
                toRemove.add(a);
                if (rescueChildActors)
                    for (int i = 0; i < a.numChildren(); i++)
                        addActor(a.getChild(i));
            }
        for (TPActor a : toRemove) {
            actors.remove(a);
        }
        toRemove.clear();
        // add
        for (TPActor a : toAdd)
            if (!actors.contains(a))
                actors.add(a);
        toAdd.clear();
    }

    /**
     * Puts TPActor {@code a} on the to-remove list. Call {@link update} to finalise the
     * operation.
     */
    public void removeActor(TPActor a) {
        toRemove.add(a);
    }

    /**
     * Puts TPActor {@code a} on the to-add list. Call {@link update} to finalise the
     * operation.
     */
    public void addActor(TPActor a) {
        toAdd.add(a);
    }

    public boolean contains(TPActor a) {
        return actors.contains(a);
    }

    @Override
    public Iterator<TPActor> iterator() {
        return actors.iterator();
    }

    /**
     * Gets the nearest other TPActor to TPActor {@code a}.
     *
     * @return The actor which is the shortest distance away from actor {@code a}.
     * Returns {@code null} if there is no other actor active in this program, or if
     * {@code a} is not active in this program.
     */
    public TPActor getNearest(TPActor a, boolean wrapAtBounds) {
        if (!contains(a))
            return null;

        TPActor nearest = null;
        double dist = Double.POSITIVE_INFINITY;
        for (TPActor b : actors) {
            if (a != b) {
                double bDist = getDistance(a, b, wrapAtBounds);
                if (nearest == null || bDist < dist) {
                    nearest = b;
                    dist = bDist;
                }
            }
        }
        return nearest;
    }

    /**
     * Gets the distance between actors {@code a} and {@code b} within the geometry of
     * this program.
     *
     * Warning! Does not check whether {@code a} and {@code b} are in the actors-list for
     * this program - you may want to check by calling {@link contains} first.
     *
     */
    public double getDistance(TPActor a, TPActor b, boolean wrapAtBounds) {
        if (wrapAtBounds) {
            double xDist = Math.abs(a.x - b.x);
            double yDist = Math.abs(a.y - b.y);
            double xDistWrap = getGeometry().getWidth() - xDist;
            double yDistWrap = getGeometry().getHeight() - yDist;
            return Geom.distance(0, 0, Math.min(xDist, xDistWrap), Math.min(yDist, yDistWrap));
        } else {
            return Geom.distance(a.x, a.y, b.x, b.y);
        }
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(String.class, getTitle(), "setTitle");
        params.addMethod(Color.class, getBGColor(), "setBGColor");
        // params.addMethod(Image.class, getBGImage(), "setBGImage");
        params.addMethod(TPGeometry.class, getGeometry(), "setGeometry");
        params.addMethod(Boolean.class, getPauseForMenu(), "setPauseForMenu");
        params.addMethod(Boolean.class, isShowIntersections(), "setShowIntersections");
        params.addMethod(Boolean.class, isSmearMode(), "setSmearMode");
        params.addListMethod(TPPlayer.class, players, "addPlayer");
        params.addListMethod(ProgramBehaviour.class, behaviours, "addBehaviour");
        params.addListMethod(ProgramBehaviour.class, resetBehaviours, "addResetBehaviour");
        List<TPActor> exceptPlayers = new ArrayList<>();
        // don't add player-actors twice
        for (TPActor a : actors)
            if (!a.isPlayer())
                exceptPlayers.add(a);
        params.addListMethod(TPActor.class, exceptPlayers, "addActor");
        return params;
    }

}
