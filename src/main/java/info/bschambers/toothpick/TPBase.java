package info.bschambers.toothpick;

import info.bschambers.toothpick.sound.TPSound;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPUI;

public class TPBase {

    private TPPlatform platform = new TPPlatform("DEFAULT PLATFORM");
    private TPUI ui = TPUI.NULL;
    private TPMenu menu = null;
    private TPSound sound = TPSound.NULL;
    private boolean running = true;
    private long iterGoal = 10;
    private int fpsGoal = 20;
    private long fps = 0;
    private long iterTime = 0;

    public void setPlatform(TPPlatform platform) {
        this.platform = platform;
        if (ui != null)
            ui.setPlatform(platform);
        System.out.println("TPBase.setPlatform() --> " + platform.getClass());
    }

    /**
     * <p>Convenience method - wraps {@code program} in a {@link TPPlatform} instance and
     * then passes to {@link setPlatform}.</p>
     */
    public void setProgram(TPProgram program) {
        setPlatform(new TPPlatform("platform", program));
    }

    public void setUI(TPUI ui) {
        this.ui = ui;
        if (platform != null) {
            ui.setPlatform(platform);
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

    public void setSound(TPSound sound) {
        this.sound = sound;
        System.out.println("TPBase.setSound() --> " + sound.getClass());
    }

    private TPProgram getProgram() {
        return platform.getProgram();
    }

    /**
     * Runs the main-loop.<br/>
     *
     * This will fail if the ui and program are not both set.
     */
    public void run() {
        System.out.println("TPBase.run()");

        if (ui == TPUI.NULL)
            System.out.println("WARNING: running with NULL-UI!");

        while (running) {

            if (!menu.isActive() || !getProgram().getPauseForMenu())
                platform.update();

            if (getProgram().isSfxTriggered()) {
                sound.sfxExplode();
                getProgram().sfxReset();
            }

            ui.updateUI();

            // regulate timing

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

    public long getFps() { return fps; }

    public int getFpsGoal() { return fpsGoal; }

    public void setFpsGoal(int val) {
        fpsGoal = val;
        iterGoal = (long) 1000 / fpsGoal;
    }

}
