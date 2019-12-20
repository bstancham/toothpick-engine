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

    public static final TPProgram NULL = new TPProgram("NULL PROGRAM") {
            @Override
            public void update() {}
        };

    private String title;
    private Color bgColor = Color.BLACK;
    private Image bgImage = null;
    protected Rect bounds = new Rect(0, 0, 640, 430);
    private PlayerController player = PlayerController.NULL;
    protected List<TPActor> actors = new ArrayList<>();
    protected boolean keepIntersectionPoints = true;
    protected List<Pt> intersectionPoints = new ArrayList<>();

    public TPProgram(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    public int numActors() { return actors.size(); }
    public TPActor getActor(int index) { return actors.get(index); }

    public PlayerController getPlayer() { return player; }
    public void setPlayer(PlayerController val) { player = val; }

    public List<Pt> getIntersectionPoints() { return intersectionPoints; }

    public List<String> getInfoLines() { return new ArrayList<String>(); }

    /**
     * Update and move the action on by one step.
     */
    public abstract void update();

}
