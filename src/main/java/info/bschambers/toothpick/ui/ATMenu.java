package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.GameBase;
import java.util.ArrayList;
import java.util.List;

public class ATMenu implements ATMenuItem {

    private String title;
    private ATMenu parent = null;
    private boolean active = false;
    private boolean delegating = false;
    private List<ATMenuItem> options = new ArrayList<>();
    private int selected = 0;

    public ATMenu(String title) {
        this.title = title;
    }

    @Override
    public String text() { return title; }

    @Override
    public void action(Code c) {
        ATMenuItem item = getSelectedItem();
        if (delegating && item instanceof ATMenu) {
            item.action(c);
        } else if (delegating) {
            System.out.println("ERROR - can't delegate to menu-item type: "
                               + item.getClass());
        } else if (c == Code.RET) {
            if (item instanceof ATMenu) {
                setDelegating(true);
            } else {
                getSelectedItem().action(c);
            }
        } else if (c == Code.CANCEL) {
            cancel();
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

    public void cancel() {
        if (parent != null) {
            parent.setDelegating(false);
        }
    }

    public boolean isDelegating() { return delegating; }
    public void setDelegating(boolean val) { delegating = val; }

    public void add(ATMenuItem item) {
        options.add(item);
        if (item instanceof ATMenu) {
            ((ATMenu) item).parent = this;
        }
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
