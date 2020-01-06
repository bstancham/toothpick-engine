package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.TPPlatform;
import java.util.function.Supplier;

/**
 * Any TPUI implementation will need to store a reference to it's associated TPProgram
 * instance, then when {@link updateUI} is called it can retrieve the relevent game assets
 * for display.<br/>
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

    public static final TPUI NULL = new TPUI() {
            @Override
            public void updateUI() {}
            @Override
            public int getUIWidth() { return 1; }
            @Override
            public int getUIHeight() { return 1; }
            @Override
            public void setPlatform(TPPlatform platform) {}
            @Override
            public void setMenu(TPMenu menu) {}
            @Override
            public void addInfoGetter(Supplier<String> getter) {}
        };

    /**
     * Update the user interface.
     */
    void updateUI();

    int getUIWidth();

    int getUIHeight();

    void setPlatform(TPPlatform platform);

    void setMenu(TPMenu menu);

    void addInfoGetter(Supplier<String> getter);

}
