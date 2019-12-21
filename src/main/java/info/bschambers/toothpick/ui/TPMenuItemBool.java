package info.bschambers.toothpick.ui;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TPMenuItemBool implements TPMenuItem {

    private String text;
    private Supplier<Boolean> getFunc;
    private Consumer<Boolean> setFunc;

    public TPMenuItemBool(String text,
                          Supplier<Boolean> getFunc,
                          Consumer<Boolean> setFunc) {
        this.text = text;
        this.getFunc = getFunc;
        this.setFunc = setFunc;
    }

    @Override
    public String text() { return text + ": < " + getFunc.get() + " >"; }

    @Override
    public void action(Code c) {
        switch (c) {
        case LEFT:
        case RIGHT:
        case RET:
            setFunc.accept(!getFunc.get());
            break;
        }
    }

}
