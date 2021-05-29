package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.*;
import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.Pt;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Static methods for creating menus.
 */
public class TPMenuFactory {

    public static TPMenuItem makeIncrementorItemInt(String label,
                                                    Supplier<Integer> getter,
                                                    Consumer<Integer> setter,
                                                    int min, int max) {
        return new TPMenuItemIncr(label, () -> "" + getter.get(),
                                  () -> setter.accept(inBounds(getter.get() - 1, min, max)),
                                  () -> setter.accept(inBounds(getter.get() + 1, min, max)));
    }

    private static int inBounds(int i, int min, int max) {
        if (i < min) return min;
        if (i > max) return max;
        return i;
    }

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
        m.add(new TPMenuItemSimple(() -> "add player (currently " + prog.numPlayers() + ")",
                                   () -> addPlayer(prog)));
        m.add(new TPMenuItemSimple("revive players", () -> {
                    for (int i = 0; i < prog.numPlayers(); i++)
                        prog.revivePlayer(i, true);
        }));
        m.add(new TPMenuItemSimple("RESET PROGRAM", () -> prog.reset()));
        m.add(new TPMenuItemBool("pause when menu active ",
                                 prog::getPauseForMenu,
                                 prog::setPauseForMenu));
        m.add(new TPMenuItemSimple(() -> "step forward by " + prog.getPauseAfterAmt() + " frames",
                                   () -> prog.setPauseAfter(prog.getPauseAfterAmt())));
        m.add(makeIncrementorItemInt("set step-forward amount",
                                     prog::getPauseAfterAmt,
                                     prog::setPauseAfterAmt,
                                     1, 1000));
        m.add(new PlayerMenu(prog));
        m.add(makeScreenGeometryMenu(prog));
        m.add(makePhysicsMenu(prog));
        m.add(makeStyleMenu(prog));
        m.add(makeInfoPrintMenu(prog));
        m.add(makeDiagnosticsMenu(prog));
        m.add(new TPMenuItemBool("smear-mode",
                                 prog::isSmearMode,
                                 prog::setSmearMode));
        m.add(makeInfoLinesMenu(prog));
        m.add(makeBGColorMenu(prog));

        return m;
    }

    private static void addPlayer(TPProgram prog) {
        System.out.println("adding new default player...");
        TPPlayer p = TPFactory.playerLine(centerPt(prog));
        TPGeometry geom = prog.getGeometry();
        double x = 0;
        double y = 0;
        int n = prog.numPlayers() % 4;
        if (n == 0) {
            x = geom.getXOneThird();
            y = geom.getYCenter();
        }
        if (n == 1) {
            x = geom.getXTwoThirds();
            y = geom.getYCenter();
        }
        if (n == 2) {
            x = geom.getXCenter();
            y = geom.getYOneThird();
        }
        if (n == 3) {
            x = geom.getXCenter();
            y = geom.getYTwoThirds();
        }
        p.getArchetype().x = x;
        p.getArchetype().y = y;
        p.reset();
        prog.addPlayer(p);
    }

    private static TPMenu makeScreenGeometryMenu(TPProgram prog) {
        TPMenu m = new TPMenu("screen geometry");
        m.add(new TPMenuItemIncr("x-offset", () -> "" + prog.getGeometry().xOffset,
                                 () -> prog.getGeometry().xOffset -= 10,
                                 () -> prog.getGeometry().xOffset += 10));
        m.add(new TPMenuItemIncr("y-offset", () -> "" + prog.getGeometry().yOffset,
                                 () -> prog.getGeometry().yOffset -= 10,
                                 () -> prog.getGeometry().yOffset += 10));
        m.add(new TPMenuItemIncr("scaling", () -> "" + prog.getGeometry().scale,
                                 () -> prog.getGeometry().scale -= 0.1,
                                 () -> prog.getGeometry().scale += 0.1));
        return m;
    }

    private static TPMenu makeStyleMenu(TPProgram prog) {
        TPMenu m = new TPMenu("presentation style");
        m.add(new TPMenuItemIncr("line-width scaling", () -> "" + prog.getGeometry().lineWidthScale,
                                 () -> prog.getGeometry().lineWidthScale -= 1,
                                 () -> prog.getGeometry().lineWidthScale += 1));
        return m;
    }

    private static TPMenu makePhysicsMenu(TPProgram prog) {
        TPMenu m = new TPMenu("physics type");
        m.add(makePhysicsSwitcherItem(prog, new PBToothpickPhysics()));
        m.add(makePhysicsSwitcherItem(prog, new PBToothpickPhysicsLight()));
        m.add(new TPMenuItemSimple("NO PHYSICS", () -> {
                    System.out.println("Switch to NO PHYSICS!");
                    prog.removeBehaviourGroup(ProgramBehaviour.PHYSICS_MODEL_ID);
        }));
        return m;
    }

    private static TPMenuItem makePhysicsSwitcherItem(TPProgram prog,
                                                      PBToothpickPhysics physics) {
        String name = physics.getClass().getSimpleName();
        return new TPMenuItemSimple(name, () -> {
                prog.addBehaviour(physics);
                System.out.println("Switch to " + name);
        });
    }

    private static TPMenu makeInfoPrintMenu(TPProgram prog) {
        TPMenu m = new TPMenu("print info");
        m.add(new TPMenuItemSimple("print players info", () -> {
                    for (int i = 0; i < prog.numPlayers(); i++) {
                        TPPlayer p = prog.getPlayer(i);
                        System.out.println("==============================");
                        System.out.println("PLAYER " + (i + 1) + " = " + p);
                        System.out.println("INPUT = " + p.getInputHandler() + "\n");
                        System.out.println("ARCHETYPE:\n" + p.getArchetype().infoString());
                        System.out.println("ACTOR:\n" + p.getActor().infoString());
                        System.out.println("==============================");
                    }}));
        m.add(new TPMenuItemSimple("print game info", () -> {
                    System.out.println("==============================");
                    System.out.println("class = " + prog.getClass());
                    System.out.println("title = " + prog.getTitle());
                    System.out.println("geometry = " + prog.getGeometry());
                    System.out.println("bg color = " + prog.getBGColor());
                    System.out.println("smear-mode = " + prog.isSmearMode());
                    System.out.println("show intersection = " + prog.isShowIntersections());
                    System.out.println("pause for menu = " + prog.getPauseForMenu());
                    System.out.println("num actors = " + prog.numActors());
                    System.out.println("total num actors (inc children) = "
                                       + prog.numActorsIncChildren());
                    System.out.println("==============================");
        }));
        return m;
    }

    private static TPMenu makeDiagnosticsMenu(TPProgram prog) {
        TPMenu m = new TPMenu("diagnostic tools");
        m.add(new TPMenuItemBool("show line-intersection points",
                                 prog::isShowIntersections,
                                 prog::setShowIntersections));
        m.add(new TPMenuItemBool("show bounding boxes",
                                 prog::isShowBoundingBoxes,
                                 prog::setShowBoundingBoxes));
        return m;
    }

    private static TPMenu makeInfoLinesMenu(TPProgram prog) {
        TPMenu m = new TPMenu("show info lines");
        m.add(new TPMenuItemBool("program info",
                                 prog::getShowProgramInfo,
                                 prog::setShowProgramInfo));
        m.add(new TPMenuItemBool("diagnostic info",
                                 prog::getShowDiagnosticInfo,
                                 prog::setShowDiagnosticInfo));
        return m;
    }

    private static TPMenu makeBGColorMenu(TPProgram prog) {
        TPMenu m = new TPMenu(() -> "Set BG Color (current: " + rgbStr(prog.getBGColor()) + ")");
        m.add(new TPMenuItemSimple("black", () -> prog.setBGColor(Color.BLACK)));
        m.add(new TPMenuItemSimple("white", () -> prog.setBGColor(Color.WHITE)));
        m.add(new TPMenuItemSimple("blue", () -> prog.setBGColor(Color.BLUE)));
        m.add(new TPMenuItemSimple("grey", () -> prog.setBGColor(Color.GRAY)));
        m.add(new TPMenuItemSimple("random", () -> prog.setBGColor(ColorGetter.randColor())));
        return m;
    }

    private static String rgbStr(Color c) {
        return c.getRed() + ", " + c.getGreen() + ", " + c.getBlue();
    }

    private static Pt centerPt(TPProgram prog) {
        return new Pt(prog.getGeometry().getXCenter(),
                      prog.getGeometry().getYCenter());
    }

    public static class PlayerMenu extends TPMenu {

        private int playerIndex = 0;
        private TPProgram prog;

        public PlayerMenu(TPProgram prog) {
            super("Player Options: (" + prog.getTitle() + ")");
            this.prog = prog;

            add(new TPMenuItemIncr("player number",
                                   () -> "" + (playerIndex + 1) + " of " + prog.numPlayers(),
                                   () -> incrPlayerIndex(-1),
                                   () -> incrPlayerIndex(1)));
            add(makePresetPlayersMenu());
            add(makeInputHandlerMenu());
            add(new TPMenuItemSimple("re-define keys", () -> System.out.println("todo...")));
            add(new TPMenuItemSimple("calibrate input", () -> System.out.println("todo...")));
            add(new TPMenuItemSimple("position", () -> System.out.println("todo...")));
        }

        private void incrPlayerIndex(int amt) {
            playerIndex += amt;
            if (playerIndex < 0)
                playerIndex = 0;
            else if (playerIndex >= prog.numPlayers())
                playerIndex = prog.numPlayers() - 1;
        }

        private TPPlayer getPlayer() {
            if (prog.numPlayers() == 0)
                return null;
            return prog.getPlayer(playerIndex);
        }

        private String getInputHandlerName() {
            TPPlayer p = getPlayer();
            if (p == null)
                return "NULL";
            return p.getInputHandler().getClass().getSimpleName();
        }

        private TPMenu makePresetPlayersMenu() {
            TPMenu m = new TPMenu("preset players");
            m.add(makePresetPlayerItem("line-player", TPMenuFactory::playerPresetLine));
            m.add(makePresetPlayerItem("hexagon-player", TPMenuFactory::playerPresetHexagon));
            return m;
        }

        private TPMenuItem makePresetPlayerItem(String label,
                                                Function<TPProgram, TPPlayer> func) {
            return new TPMenuItemSimple(label, () -> {
                    TPPlayer p = getPlayer();
                    if (p == null) {
                        System.out.println("PLAYER IS NULL!");
                    } else {
                        TPPlayer pp = func.apply(prog);
                        pp.getActor().x = p.getActor().x;
                        pp.getActor().y = p.getActor().y;
                        pp.getActor().angle = p.getActor().angle;
                        prog.setPlayer(playerIndex, pp);
                    }
            });
        }

        private TPMenu makeInputHandlerMenu() {
            TPMenu m = new TPMenu(() -> "Change Input Handler (current = "
                                  + getInputHandlerName() + ")");
            m.add(makeInputSwitcherItem("thrust-inertia", ThrustInertiaInput::new));
            m.add(makeInputSwitcherItem("thrust-&-angle-inertia", ThrustAndAngleInertiaInput::new));
            m.add(makeInputSwitcherItem("eight-way inertia", EightWayInertiaInput::new));
            m.add(makeInputSwitcherItem("thrust", ThrustInput::new));
            m.add(makeInputSwitcherItem("eight-way", EightWayInput::new));
            return m;
        }

        private TPMenuItem makeInputSwitcherItem(String label,
                                                 Supplier<KeyInputHandler> ihFunc) {
            return new TPMenuItemSimple(label, () -> {
                    TPPlayer p = getPlayer();
                    if (p == null)
                        System.out.println("PLAYER IS NULL!");
                    else
                        p.setInputHandler(ihFunc.get());
            });
        }

    }

    public static TPPlayer playerPresetLine(TPProgram prog) {
        return TPFactory.playerLine(centerPt(prog));
    }

    public static TPPlayer playerPresetHexagon(TPProgram prog) {
        TPActor a = new TPActor(TPFactory.regularPolygonForm(30, 6));
        return TPFactory.player(a);
    }

}
