package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.TPProgram;

/**
 * Any TPUI implementation will need to store a reference to it's associated TPProgram
 * instance, then when {@link repaintUI} is called it can retrieve the relevent game
 * assets for display.<br/>
 *
 * The display routine implementation will be different for different UI systems but these
 * are the steps which need to be present in order to display a TPProgram properly:<br/>
 *
 * - background - either plain colour, or an image
 * - menus
 * - actors
 * - text
 * - lines/circles/etc
 */
public interface TPUI {

    void repaintUI();

    void setProgram(TPProgram program);

    void setMenu(TPMenu menu);

}
