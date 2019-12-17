package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.GameBase;
import java.util.ArrayList;
import java.util.List;

public class ATMenu implements ATMenuItem {

    private String title;
    private boolean active = false;
    private List<ATMenuItem> options = new ArrayList<>();
    private int selected = 0;

    public ATMenu(String title) {
        this.title = title;
    }

    @Override
    public String text() { return title; }

    @Override
    public void action(Code c) {
        if (c == Code.RET) {
            getSelectedItem().action(c);
        } else if (c == Code.UP) {
            incrSelected(-1);
        } else if (c == Code.DOWN) {
            incrSelected(1);
        } else if (c == Code.LEFT) {
        } else if (c == Code.RIGHT) {
        }
    }

    public boolean isActive() { return active; }
    public void setActive(boolean val) { active = val; }

    public void add(ATMenuItem item) {
        options.add(item);
    }

    /**
     * The number of items in the menu, including non-usable items such as spacers.
     */
    public int getNumItems() {
        return options.size();
    }

    public ATMenuItem getItem(int index) {
        return options.get(index);
    }

    public int getSelectedIndex() { return selected; }

    private void incrSelected(int amt) {
        selected += amt;
        if (selected >= options.size())
            selected = 0;
        else if (selected < 0)
            selected = options.size() - 1;
    }

    public ATMenuItem getSelectedItem() {
        return options.get(selected);
    }

}
