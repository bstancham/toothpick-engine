package info.bschambers.toothpick.ui;

public class ATMenuItemSimple implements ATMenuItem {

    private String text;
    private Runnable action;

    public ATMenuItemSimple(String text, Runnable action) {
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
