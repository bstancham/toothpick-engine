package info.bschambers.toothpick.ui;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TPMenuItemKeyInput implements TPMenuItem {

    private Supplier<String> textSupplier;

    Consumer<Integer> codeFunc;

    public TPMenuItemKeyInput(Supplier<String> textSupplier, Consumer<Integer> codeFunc) {
        this.textSupplier = textSupplier;
        this.codeFunc = codeFunc;
    }

    @Override
    public String text() {
        return textSupplier.get();
    }

    @Override
    public void action(Code c, int keyCode) {
        codeFunc.accept(keyCode);
    }

}
