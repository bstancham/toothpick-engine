package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPPlayer;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import info.bschambers.toothpick.ui.TPUI;
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
public class TPProgram implements Iterable<TPActor>, TPEncodingHelper {

    public static final TPProgram NULL = new TPProgram();

    private String title;
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    private TPGeometry geom = new TPGeometry();
    private boolean pauseForMenu = true;
    protected boolean keepIntersectionPoints = false;
    protected List<Pt> intersectionPoints = new ArrayList<>();
    protected boolean showBoundingBoxes = false;
    private boolean smearMode = false;
    private TPPlayer player = TPPlayer.NULL;
    private List<ProgramBehaviour> behaviours = new ArrayList<>();;
    protected List<TPActor> actors = new ArrayList<>();
    private List<TPActor> toAdd = new ArrayList<>();
    private List<TPActor> toRemove = new ArrayList<>();
    private boolean rescueChildActors = true;
    private boolean finished = false;
    private int pauseAfter = -1;
    public boolean showProgramInfo = true;
    public boolean showDiagnosticInfo = true;
    private boolean sfxTriggered = false;

    public TPProgram() {
        this("UNTITLED PROGRAM");
    }

    public TPProgram(String title) {
        this.title = title;
        geom.setupAndCenter(1000, 800);
    }

    /**
     * <p>Reset the program to a starting state - clear the actors list, reset all
     * behaviours, and set the finished-flag to false.</p>
     *
     * <p>To do a full reset, you may want to call {@link resetPlayer} also.</p>
     */
    public void reset() {
        actors.clear();
        toAdd.clear();
        toRemove.clear();
        for (ProgramBehaviour pb : behaviours)
            pb.reset();
        setFinished(false);
    }

    public void resetPlayer() {
        removeActor(player.getActor());
        player.reset();
        addActor(player.getActor());
        // updateActorsInPlace();
    }

    public TPPlayer getPlayer() {
        return player;
    }

    public void setPlayer(TPPlayer newPlayer) {
        System.out.println("TPProgram.setPlayer() --> " + newPlayer);
        // make sure that the old player-actor has been removed
        removeActor(player.getActor());
        player = newPlayer;
        addActor(player.getActor());
        updateActorsInPlace();
    }

    public void revivePlayer(boolean retainStats) {
        System.out.println("TPProgram.revivePlayer()");
        removeActor(player.getActor());
        player.reset(retainStats);
        addActor(player.getActor());
        updateActorsInPlace();
    }

    protected boolean playerIsDead() {
        return !getPlayer().getActor().isAlive();
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

    public TPGeometry getGeometry() {
        return geom;
    }

    public void setGeometry(TPGeometry val) {
        geom = val;
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
        String group = pb.getSingletonGroup();
        if (!group.isEmpty()) {
            List<ProgramBehaviour> pbToRemove = new ArrayList<>();
            for (ProgramBehaviour other : behaviours)
                if (other.getSingletonGroup().equals(group))
                    pbToRemove.add(other);
            for (ProgramBehaviour other : pbToRemove)
                behaviours.remove(other);
        }
        behaviours.add(pb);
    }

    public int numActors() {
        return actors.size();
    }

    public TPActor getActor(int index) {
        return actors.get(index);
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
            if (playerIsDead()) {
                lines.add("PLAYER DEAD!");
            }
            lines.add("kills: " + getPlayer().getActor().numKills);
            lines.add("deaths: " + getPlayer().getActor().numDeaths);
        }
        if (showDiagnosticInfo) {
            lines.add("actors: " + actors.size());
            for (ProgramBehaviour pb : behaviours)
                for (String pbLine : pb.getInfoLines())
                    lines.add(pbLine);
        }
        return lines;
    }

    /**
     * Update and move the action on by one step.
     */
    public void update() {

        if (pauseAfter > 0) {
            pauseAfter--;
            if (pauseAfter == 0) {
                setPauseForMenu(true);
            }
        }

        if (playerIsDead()) {
            getPlayer().getActor().update(this);
            if (getPlayer().getActor().getActionTrigger()) {
                System.out.println("... TRIGGER REVIVAL!");
                revivePlayer(true);
            }
        }

        intersectionPoints.clear();
        for (TPActor a : actors)
            a.update(this);
        for (ProgramBehaviour pb : behaviours)
            pb.update(this);
        housekeeping();
    }

    /**
     * Update without advancing the action.
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
        for (TPActor a : toRemove)
            actors.remove(a);
        toRemove.clear();
        // add
        for (TPActor a : toAdd)
            if (!actors.contains(a))
                actors.add(a);
        toAdd.clear();
    }

    public void removeActor(TPActor a) {
        toRemove.add(a);
    }

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
        params.addMethod(TPPlayer.class, getPlayer(), "setPlayer");
        params.addListMethod(ProgramBehaviour.class, behaviours, "addBehaviour");
        List<TPActor> exceptPlayer = new ArrayList<>();
        // don't add the player-actor twice
        for (TPActor a : actors)
            if (a != player.getActor())
                exceptPlayer.add(a);
        params.addListMethod(TPActor.class, exceptPlayer, "addActor");
        return params;
    }

}
