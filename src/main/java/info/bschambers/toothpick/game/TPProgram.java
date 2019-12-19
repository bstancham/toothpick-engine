package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.PlayerController;
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
    protected List<TPActor> actors = new ArrayList<>();
    private PlayerController player = PlayerController.NULL;

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

    /**
     * Update and move the action on by one step.
     */
    public abstract void update();

}
