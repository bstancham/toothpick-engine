package info.bstancham.toothpick.ui;

import java.util.function.Supplier;

public class TPMenuItemIncr implements TPMenuItem {

    private String text;
    private Supplier<String> getFunc;
    private Runnable leftFunc;
    private Runnable rightFunc;

    public TPMenuItemIncr(String text, Supplier<String> getFunc,
                          Runnable leftFunc, Runnable rightFunc) {
        this.text = text;
        this.getFunc = getFunc;
        this.leftFunc = leftFunc;
        this.rightFunc = rightFunc;
    }

    @Override
    public String text() { return text + ": < " + getFunc.get() + " >"; }

    @Override
    public void action(Code c, int keyCode) {
        switch (c) {
        case LEFT:
            leftFunc.run();
            break;
        case RIGHT:
            rightFunc.run();
            break;
        }
    }

}
