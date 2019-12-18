package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.Actor;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage:
 *
 * GameBase periodically calls update()
 *
 * GameUI uses the various accessor methods to get game assets for display - NOTE: that
 * some of these may return null, so they always need to be checked!
 */
public abstract class GameProgram {

    private String title;
    private Color bgColor = null;
    private Image bgImage = null;
    protected List<Actor> actors = new ArrayList<>();

    public GameProgram(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    public int numActors() { return actors.size(); }
    public Actor getActor(int index) { return actors.get(index); }

    /**
     * Update and move the action on by one step.
     */
    public abstract void update();

}
