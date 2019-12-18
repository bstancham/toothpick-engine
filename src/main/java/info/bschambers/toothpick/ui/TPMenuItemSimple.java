package info.bschambers.toothpick.ui;

public class TPMenuItemSimple implements TPMenuItem {

    private String text;
    private Runnable action;

    public TPMenuItemSimple(String text, Runnable action) {
        this.text = text;
        this.action = action;
    }

    @Override
    public String text() { return text; }

    @Override
    public void action(Code c) {
        if (c == Code.RET)
            action.run();
    }

}
