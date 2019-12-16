package info.bschambers.toothpick.game;

import java.awt.Color;
import java.awt.Image;

/**
 * Usage:
 *
 * GameBase periodically calls update()
 *
 * GameUI uses the various accessor methods to get game assets for display - NOTE: that
 * some of these may return null, so they always need to be checked!
 */
public abstract class GameProgram {

    private Color bgColor = null;
    private Image bgImage = null;

    public Color getBGColor() { return bgColor; }
    public void setBGColor(Color val) { bgColor = val; }

    public Image getBGImage() { return bgImage; }
    public void setBGImage(Image val) { bgImage = val; }

    /**
     * Update and move the action on by one step.
     */
    public abstract void update();

}
