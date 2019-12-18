package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.GameProgram;

/**
 * Any GameUI implementation will need to store a reference to it's associated GameProgram
 * instance, then when {@link repaintUI} is called it can retrieve the relevent game
 * assets for display.<br/>
 *
 * The display routine implementation will be different for different UI systems but these
 * are the steps which need to be present in order to display a GameProgram properly:<br/>
 *
 * - background - either plain colour, or an image
 * - menus
 * - actors
 * - text
 * - lines/circles/etc
 */
public interface GameUI {

    void repaintUI();

    void setProgram(GameProgram program);

    void setMenu(TPMenu menu);

}
