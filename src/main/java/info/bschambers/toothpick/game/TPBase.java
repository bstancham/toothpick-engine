package info.bschambers.toothpick.game;

import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPUI;

public class TPBase {

    private TPProgram program = TPProgram.NULL;
    private TPUI ui = null;
    private TPMenu menu = null;
    private boolean running = true;
    private long iterGoal = 10;
    private int fpsGoal = 20;
    private long fps = 0;

    private long iterTime = 0;

    public void setProgram(TPProgram program) {
        this.program = program;
        if (ui != null)
            ui.setProgram(program);
        System.out.println("TPBase.setProgram() --> " + program.getClass());
    }

    public void setUI(TPUI ui) {
        this.ui = ui;
        if (program != null) {
            ui.setProgram(program);
            ui.addInfoGetter(() -> "fps: " + getFps());
        }
        System.out.println("TPBase.setUI() --> " + ui.getClass());
    }

    public void setMenu(TPMenu menu) {
        this.menu = menu;
        if (ui != null)
            ui.setMenu(menu);
        System.out.println("TPBase.setMenu() --> " + menu.getClass());
    }

    public void hideMenu() {
        if (menu != null)
            menu.setActive(false);
    }

    /**
     * Runs the main-loop.<br/>
     *
     * This will fail if the ui and program are not both set.
     */
    public void run() {
        System.out.println("TPBase.run()");
        if (program == null) {
            System.out.println("can't run: no program loaded");
        } else {
            while (running) {

                if (!menu.isActive() || !program.getPauseForMenu())
                    program.update();

                ui.repaintUI();

                long thisIter = System.currentTimeMillis() - iterTime;
                iterTime = System.currentTimeMillis();

                fps = (thisIter > 0 ? 1000L / thisIter : 0);

                long thisSleep = iterGoal - thisIter;
                thisSleep = Math.max(0, thisSleep);

                try {
                    Thread.sleep(thisSleep);
                } catch (Exception ex) {}
            }
        }
    }

    public long getFps() { return fps; }

    public int getFpsGoal() { return fpsGoal; }

    public void setFpsGoal(int val) {
        fpsGoal = val;
        iterGoal = (long) 1000 / fpsGoal;
    }

}
