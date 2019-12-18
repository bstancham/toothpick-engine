package info.bschambers.toothpick.game;

import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.GameUI;

public class GameBase {

    private GameProgram program = null;
    private GameUI ui = null;
    private TPMenu menu = null;
    private boolean running = true;
    private int fpsGoal = 10;

    public void setProgram(GameProgram program) {
        this.program = program;
        if (ui != null)
            ui.setProgram(program);
        System.out.println("GameBase.setProgram() --> " + program.getClass());
    }

    public void setUI(GameUI ui) {
        this.ui = ui;
        if (program != null)
            ui.setProgram(program);
        System.out.println("GameBase.setUI() --> " + ui.getClass());
    }

    public void setMenu(TPMenu menu) {
        this.menu = menu;
        if (ui != null)
            ui.setMenu(menu);
        System.out.println("GameBase.setMenu() --> " + menu.getClass());
    }

    /**
     * Runs the main-loop.<br/>
     *
     * This will fail if the ui and program are not both set.
     */
    public void run() {
        System.out.println("GameBase.run()");
        if (program == null) {
            System.out.println("can't run: no program loaded");
        } else {
            while (running) {
                program.update();
                ui.repaintUI();
                try {
                    Thread.sleep(50);
                } catch (Exception ex) {}
            }
        }
    }

    public int getFpsGoal() { return fpsGoal; }

    public void setFpsGoal(int val) { fpsGoal = val; }

}
