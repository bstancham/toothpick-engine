package info.bschambers.toothpick.ui;

import java.util.function.Supplier;

public class TPMenuItemSimple implements TPMenuItem {

    private Supplier<String> textSupplier;
    private Runnable action;

    public TPMenuItemSimple(String text, Runnable action) {
        this(() -> text, action);
    }

    public TPMenuItemSimple(Supplier<String> textSupplier, Runnable action) {
        this.textSupplier = textSupplier;
        this.action = action;
    }

    @Override
    public String text() {
        return textSupplier.get();
    }

    @Override
    public void action(Code c, int keyCode) {
        if (c == Code.RET)
            action.run();
    }

}
