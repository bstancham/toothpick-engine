package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.TPBase;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TPMenu implements TPMenuItem {

    private Supplier<String> titleSupplier;
    private TPMenu parent = null;
    private boolean active = false;
    private boolean delegating = false;
    private List<TPMenuItem> options = new ArrayList<>();
    private int selected = 0;

    public TPMenu(String title) {
        this(() -> title);
    }

    public TPMenu(Supplier<String> titleSupplier) {
        this.titleSupplier = titleSupplier;
    }

    @Override
    public String text() { return titleSupplier.get(); }

    @Override
    public void action(Code c) {
        TPMenuItem item = getSelectedItem();
        if (delegating && item instanceof TPMenu) {
            item.action(c);
        } else if (delegating) {
            System.out.println("ERROR - can't delegate to menu-item type: "
                               + item.getClass());
        } else if (c == Code.RET) {
            if (item instanceof TPMenu) {
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
            getSelectedItem().action(c);
        } else if (c == Code.RIGHT) {
            getSelectedItem().action(c);
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

    public void add(TPMenuItem item) {
        options.add(item);
        if (item instanceof TPMenu) {
            ((TPMenu) item).parent = this;
        }
    }

    /**
     * The number of items in the menu, including non-usable items such as spacers.
     */
    public int getNumItems() {
        return options.size();
    }

    public TPMenuItem getItem(int index) {
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

    public TPMenuItem getSelectedItem() {
        return options.get(selected);
    }

}
