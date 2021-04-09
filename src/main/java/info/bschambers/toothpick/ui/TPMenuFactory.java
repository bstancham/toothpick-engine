package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.TPBase;
import info.bschambers.toothpick.TPProgram;
import java.util.function.Supplier;

/**
 * Static factory methods for menus.
 */
public class TPMenuFactory {

    public static TPMenu makeProgramMenu(TPProgram prog, TPBase base) {
        return makeProgramMenu(() -> prog.getTitle(), prog, base);
    }

    public static TPMenu makeProgramMenu(Supplier<String> ss, TPProgram prog, TPBase base) {
        TPMenu m = new TPMenu(ss);
        m.setInitAction(() -> {
                base.setProgram(prog);
                prog.updateActorsInPlace();
            });
        m.add(new TPMenuItemSimple("RUN", () -> {
                    base.hideMenu();
        }));
        m.add(new TPMenuItemSimple("RESET PROGRAM", () -> prog.init()));
        m.add(new TPMenuItemBool("pause when menu active ",
                                 prog::getPauseForMenu,
                                 prog::setPauseForMenu));
        m.add(new TPMenuItemBool("smear-mode",
                                 prog::isSmearMode,
                                 prog::setSmearMode));
        return m;
    }

}
