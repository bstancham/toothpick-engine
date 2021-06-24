package info.bstancham.toothpick.ui;

import info.bstancham.toothpick.*;
import info.bstancham.toothpick.actor.*;
import info.bstancham.toothpick.geom.Node;
import info.bstancham.toothpick.geom.Pt;
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
        addPlayer(prog, TPFactory.playerLine(centerPt(prog)));
    }

    public static void addPlayer(TPProgram prog, TPPlayer p) {
        System.out.println("adding new default player...");
        TPGeometry geom = prog.getGeometry();
        double x = 0;
        double y = 0;
        int n = prog.numPlayers() % 4;
        if (n == 0) {
            x = geom.getXOneThird();
            y = geom.getYCenter();
            TPFactory.setPlayerKeysQAWE(p.getInputHandler());
        }
        if (n == 1) {
            x = geom.getXTwoThirds();
            y = geom.getYCenter();
            TPFactory.setPlayerKeysIKOP(p.getInputHandler());
        }
        if (n == 2) {
            x = geom.getXCenter();
            y = geom.getYOneThird();
            TPFactory.setPlayerKeysQAWE(p.getInputHandler());
        }
        if (n == 3) {
            x = geom.getXCenter();
            y = geom.getYTwoThirds();
            TPFactory.setPlayerKeysIKOP(p.getInputHandler());
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
        m.add(makePhysicsSwitcherItem(prog, new ProgToothpickPhysics()));
        m.add(makePhysicsSwitcherItem(prog, new ProgToothpickPhysicsLight()));
        m.add(new TPMenuItemSimple("NO PHYSICS", () -> {
                    System.out.println("Switch to NO PHYSICS!");
                    prog.removeBehaviourGroup(ProgramBehaviour.PHYSICS_MODEL_ID);
        }));
        return m;
    }

    private static TPMenuItem makePhysicsSwitcherItem(TPProgram prog,
                                                      ProgToothpickPhysics physics) {
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
        m.add(new TPMenuItemBool("players diagnostic info",
                                 prog::getShowPlayersDiagnosticInfo,
                                 prog::setShowPlayersDiagnosticInfo));
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
            add(makeDefineKeysMenu());
            add(makeInputCalibrationMenu());
            add(makeMenuConvertDroneToPlayer());
            add(makeMenuPlayerColor());
            add(makeMenuPowerups());
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

        private TPMenu makePresetPlayersMenu() {
            TPMenu m = new TPMenu("preset players");
            m.add(makePresetPlayerItem("line-player", TPMenuFactory::playerPresetLine));
            m.add(makePresetPlayerItem("hexagon-player", TPMenuFactory::playerPresetHexagon));
            m.add(makePresetPlayerItem("shooter-line", TPMenuFactory::playerPresetShooterLine));
            m.add(makePresetPlayerItem("shooter ship", TPMenuFactory::playerPresetShooterShip));
            m.add(makePresetPlayerItem("rapid-shooter-ship-1", TPMenuFactory::playerPresetShooterShipRapid1));
            m.add(makePresetPlayerItem("rapid-shooter-ship-2", TPMenuFactory::playerPresetShooterShipRapid2));
            m.add(makePresetPlayerItem("rapid-shooter-ship-3", TPMenuFactory::playerPresetShooterShipRapid3));
            m.add(makePresetPlayerItem("spread-shooter-ship-1", TPMenuFactory::playerPresetSpreadShooterShip1));
            m.add(makePresetPlayerItem("spread-shooter-ship-2", TPMenuFactory::playerPresetSpreadShooterShip2));
            m.add(makePresetPlayerItem("spread-shooter-ship-3", TPMenuFactory::playerPresetSpreadShooterShip3));
            return m;
        }

        private TPMenuItem makePresetPlayerItem(String label, Supplier<TPPlayer> func) {
            return new TPMenuItemSimple(label, () -> {
                    TPPlayer p = getPlayer();
                    if (p == null) {
                        System.out.println("PLAYER IS NULL!");
                    } else {
                        TPPlayer pp = func.get();
                        // set current position
                        pp.getActor().x = p.getActor().x;
                        pp.getActor().y = p.getActor().y;
                        pp.getActor().angle = p.getActor().angle;
                        // set restart position
                        pp.getArchetype().x = p.getArchetype().x;
                        pp.getArchetype().y = p.getArchetype().y;
                        pp.getArchetype().angle = p.getArchetype().angle;
                        prog.setPlayer(playerIndex, pp);
                    }
            });
        }

        private TPMenu makeInputHandlerMenu() {
            TPMenu m = new TPMenu(() -> "Change Input Handler (current = "
                                  + getInputHandlerName() + ")");
            m.add(makeInputSwitcherItem("thrust-inertia", KeyInputThrustInertia::new));
            m.add(makeInputSwitcherItem("thrust-&-angle-inertia", KeyInputThrustAndAngleInertia::new));
            m.add(makeInputSwitcherItem("eight-way inertia", KeyInputEightWayInertia::new));
            m.add(makeInputSwitcherItem("thrust", KeyInputThrust::new));
            m.add(makeInputSwitcherItem("eight-way", KeyInputEightWay::new));
            return m;
        }

        private String getInputHandlerName() {
            TPPlayer p = getPlayer();
            if (p == null)
                return "NULL";
            return p.getInputHandler().getClass().getSimpleName();
        }

        private TPMenuItem makeInputSwitcherItem(String label,
                                                 Supplier<KeyInputHandler> ihFunc) {
            return new TPMenuItemSimple(label, () -> {
                    TPPlayer p = getPlayer();
                    if (p == null) {
                        System.out.println("PLAYER IS NULL!");
                    } else {
                        // duplicate key-bindings before switching
                        KeyInputHandler oldKeys = p.getInputHandler();
                        KeyInputHandler newKeys = ihFunc.get();
                        newKeys.bindUp.setCode(oldKeys.bindUp.code());
                        newKeys.bindDown.setCode(oldKeys.bindDown.code());
                        newKeys.bindLeft.setCode(oldKeys.bindLeft.code());
                        newKeys.bindRight.setCode(oldKeys.bindRight.code());
                        newKeys.bindAction.setCode(oldKeys.bindAction.code());
                        newKeys.bindZoomIn.setCode(oldKeys.bindZoomIn.code());
                        newKeys.bindZoomOut.setCode(oldKeys.bindZoomOut.code());
                        p.setInputHandler(newKeys);
                    }
            });
        }

        private TPMenu makeDefineKeysMenu() {
            TPMenu m = new TPMenu("define keys");
            m.add(new TPMenuItemKeyInput(() -> "UP = " + getPlayer().getInputHandler().bindUp.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindUp.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "DOWN = " + getPlayer().getInputHandler().bindDown.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindDown.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "LEFT = " + getPlayer().getInputHandler().bindLeft.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindLeft.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "RIGHT = " + getPlayer().getInputHandler().bindRight.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindRight.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "ACTION = " + getPlayer().getInputHandler().bindAction.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindAction.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "ZOOM IN = " + getPlayer().getInputHandler().bindZoomIn.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindZoomIn.setCode(c)));
            m.add(new TPMenuItemKeyInput(() -> "ZOOM OUT = " + getPlayer().getInputHandler().bindZoomOut.code(),
                                         (Integer c) -> getPlayer().getInputHandler().bindZoomOut.setCode(c)));
            return m;
        }

        private TPMenu makeInputCalibrationMenu() {
            TPMenu m = new TPMenu("input calibration");
            m.add(new TPMenuItemIncr("xy-step", () -> getXYStepString(),
                                     () -> incrXYStep(-1),
                                     () -> incrXYStep(1)));
            m.add(new TPMenuItemIncr("angle-step", () -> getAngleStepString(),
                                     () -> incrAngleStep(-1),
                                     () -> incrAngleStep(1)));
            return m;
        }

        private String getXYStepString() {
            TPPlayer p = getPlayer();
            if (p == null)
                return "NULL";
            else
                return "" + p.getInputHandler().getXYStep();
        }

        private String getAngleStepString() {
            TPPlayer p = getPlayer();
            if (p == null)
                return "NULL";
            else
                return "" + p.getInputHandler().getAngleStep();
        }

        private void incrXYStep(double amt) {
            TPPlayer p = getPlayer();
            if (p != null) {
                double step = getIncrStep(p.getInputHandler().getXYStep(), amt);
                p.getInputHandler().incrXYStep(step);
            }
        }

        private void incrAngleStep(double amt) {
            TPPlayer p = getPlayer();
            if (p != null) {
                double step = getIncrStep(p.getInputHandler().getAngleStep(), amt);
                p.getInputHandler().incrAngleStep(step);
            }
        }

        private double getIncrStep(double current, double dir) {
            if (current == 0)
                current = 0.01;
            double step = current / 10;
            if (dir < 0)
                step = -step;
            return step;
        }

        private TPMenu makeMenuConvertDroneToPlayer() {
            TPMenu m = new TPMenu("convert drone to player");
            m.setInitAction(() -> {
                    m.clear();
                    for (TPActor a : prog) {
                        if (!a.isPlayer()) {
                            m.add(makeMenuItemDroneToPlayer(a));
                        }
                    }
                });
            return m;
        }

        private TPMenuItem makeMenuItemDroneToPlayer(TPActor drone) {
            return new TPMenuItemSimple(drone.name + "(" + drone + ")",
                                        () -> {
                                            System.out.println("convert '" + drone.name + "' to player");
                                            getPlayer().setActor(drone);
                                            // getPlayer().setArchetype(drone);
            });
        }

        private TPMenu makeMenuPlayerColor() {
            TPMenu m = new TPMenu("player color");
            m.add(makePlayerColorSwitcher("random", ColorRandom::new));
            m.add(makePlayerColorSwitcher("random (mono)", ColorRandomMono::new));
            m.add(makePlayerColorSwitcher("smooth", ColorSmoothColor::new));
            m.add(makePlayerColorSwitcher("smooth (mono)", ColorSmoothMono::new));
            m.add(makePlayerColorSwitcher("flat color (white)", () -> ColorMono.WHITE));
            m.add(makePlayerColorSwitcher("flat color (black)", () -> ColorMono.BLACK));
            m.add(makePlayerColorSwitcher("flat color (red)", () -> ColorMono.RED));
            m.add(makePlayerColorSwitcher("flat color (green)", () -> ColorMono.GREEN));
            m.add(makePlayerColorSwitcher("flat color (blue)", () -> ColorMono.BLUE));
            m.add(makePlayerColorSwitcher("flat color (cyan)", () -> ColorMono.CYAN));
            m.add(makePlayerColorSwitcher("flat color (magenta)", () -> ColorMono.MAGENTA));
            m.add(makePlayerColorSwitcher("flat color (yellow)", () -> ColorMono.YELLOW));
            m.add(makePlayerColorSwitcher("flat color (RANDOM)", ColorMono::new));
            return m;
        }

        private TPMenuItem makePlayerColorSwitcher(String label, Supplier<ColorGetter> colorFunc) {
            return new TPMenuItemSimple(label, () -> {
                    ColorGetter cg = colorFunc.get();
                    getPlayer().getActor().setColorGetter(cg);
                    getPlayer().getArchetype().setColorGetter(cg);
            });
        }

        private TPMenu makeMenuPowerups() {
            TPMenu m = new TPMenu("powerups");
            m.add(makeMenuItemPowerup("shooting", new PowerupBehaviourShooting()));
            m.add(makeMenuItemPowerup("strong", new PowerupBehaviourStrong()));
            m.add(makeMenuItemPowerup("sticky", new PowerupBehaviourSticky()));
            return m;
        }

        private TPMenuItem makeMenuItemPowerup(String label, PowerupBehaviour powerup) {
            return new TPMenuItemSimple(label, () -> {
                    powerup.applyPowerup(getPlayer().getActor());
                    System.out.println("POWERUP APPLIED: " + label);
            });
        }
    }

    public static TPPlayer playerPresetLine() {
        TPForm form = new TPForm();
        form.addPart(new TPLink(new Node(0, -40), new Node(0, 40)));
        form.housekeeping();
        TPActor actor = new TPActor(form);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_AT_BOUNDS);
        actor.setColorGetter(new ColorMono(Color.GREEN));
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new KeyInputThrustInertia());
        return player;
    }

    public static TPPlayer playerPresetHexagon() {
        TPActor a = new TPActor(TPFactory.regularPolygonForm(30, 6));
        return TPFactory.player(a);
    }

    public static TPPlayer playerPresetShooterLine() {
        TPPlayer p = playerPresetLine();
        p.getArchetype().setTriggerBehaviour(new TrigActionShooter());
        p.reset();
        return p;
    }

    public static TPPlayer playerPresetShooterShip() {
        return makePlayerShooterShip(200);
    }

    public static TPPlayer playerPresetShooterShipRapid1() {
        return makePlayerShooterShip(50);
    }

    public static TPPlayer playerPresetShooterShipRapid2() {
        return makePlayerShooterShip(20);
    }

    public static TPPlayer playerPresetShooterShipRapid3() {
        return makePlayerShooterShip(5);
    }

    public static TPPlayer makePlayerShooterShip(int delay) {
        TPActor a = new TPActor(TPFactory.shipForm1(40));
        a.setTriggerBehaviour(new TrigActionShooter(delay));
        TPPlayer p = new TPPlayer(a);
        p.setInputHandler(new KeyInputThrustInertia());
        return p;
    }

    public static TPPlayer playerPresetSpreadShooterShip1() {
        return makePlayerSpreadShooterShip(100, 3, Math.PI * 0.05);
    }

    public static TPPlayer playerPresetSpreadShooterShip2() {
        return makePlayerSpreadShooterShip(100, 5, Math.PI * 0.05);
    }

    public static TPPlayer playerPresetSpreadShooterShip3() {
        return makePlayerSpreadShooterShip(100, 7, Math.PI * 0.08);
    }

    public static TPPlayer makePlayerSpreadShooterShip(int delay, int numBullets, double spreadWidth) {
        TPActor a = new TPActor(TPFactory.shipForm1(40));
        a.setTriggerBehaviour(new TrigActionSpreadShooter(delay, numBullets, spreadWidth));
        TPPlayer p = new TPPlayer(a);
        p.setInputHandler(new KeyInputThrustInertia());
        return p;
    }

}
