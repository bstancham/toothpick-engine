package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.TPBase;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TPMenu implements TPMenuItem {

    private Supplier<String> titleSupplier;
    private TPMenu parent = null;
    private boolean active = false;
    private boolean hidden = false;
    private boolean delegating = false;
    private List<TPMenuOption> options = new ArrayList<>();
    private int selected = 0;
    private Runnable initAction = () -> {};

    public TPMenu(String title) {
        this(() -> title);
    }

    public TPMenu(Supplier<String> titleSupplier) {
        this.titleSupplier = titleSupplier;
    }

    /**
     * Discards all menu-options.
     */
    public void clear() { options.clear(); }

    @Override
    public String text() {
        return titleSupplier.get();
    }

    @Override
    public void action(Code c) {
        if (c == Code.HIDE) {
            hidden = !hidden;
        } else if (!hidden) {
            TPMenuOption opt = getSelectedOption();
            TPMenuItem item = opt.get();
            if (delegating && item instanceof TPMenu) {
                item.action(c);
            } else if (delegating) {
                System.out.println("ERROR - can't delegate to menu-item type: "
                                   + item.getClass());
            } else if (c == Code.RET) {
                if (item instanceof TPMenu) {
                    setDelegating(true);
                    opt.update();
                    TPMenu m = (TPMenu) opt.get();
                    m.setParent(this);
                    m.runInitAction();
                } else {
                    getSelectedOption().get().action(c);
                }
            } else if (c == Code.CANCEL) {
                cancel();
            } else if (c == Code.UP) {
                incrSelected(-1);
            } else if (c == Code.DOWN) {
                incrSelected(1);
            } else if (c == Code.LEFT) {
                getSelectedOption().get().action(c);
            } else if (c == Code.RIGHT) {
                getSelectedOption().get().action(c);
            }
        }
    }

    /**
     * <p>Sets an action to be run when this menu is selected from it's parent menu. Note
     * that this action is called by the parent on selecting this menu, therefore if this
     * is the root menu this action will never be called.</p>
     */
    public void setInitAction(Runnable initAction) {
        this.initAction = initAction;
    }

    public void runInitAction() {
        System.out.println("TPMenu.runInitAction()");
        initAction.run();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean val) {
        active = val;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void cancel() {
        if (parent != null) {
            parent.setDelegating(false);
        }
    }

    public void setParent(TPMenu parent) {
        this.parent = parent;
    }

    /** @return True, if this menu is delegating to a sub-menu. */
    public boolean isDelegating() {
        return delegating;
    }

    public void setDelegating(boolean val) {
        delegating = val;
    }

    public void add(TPMenuItem item) {
        options.add(new TPMenuOption(item));
    }

    /**
     * Add a dynamic menu item - the menu item will be fetched anew at the time when it is
     * selected.
     */
    public void add(Supplier<TPMenuItem> itemSupplier) {
        options.add(new TPMenuOption(itemSupplier));
    }

    /**
     * The number of items in the menu, including non-usable items such as spacers.
     */
    public int getNumItems() {
        return options.size();
    }

    public TPMenuItem getItem(int index) {
        return options.get(index).get();
    }

    public int getSelectedIndex() {
        return selected;
    }

    private void incrSelected(int amt) {
        selected += amt;
        if (selected >= options.size())
            selected = 0;
        else if (selected < 0)
            selected = options.size() - 1;
    }

    public TPMenuOption getSelectedOption() {
        return options.get(selected);
    }

    public TPMenuItem getSelectedItem() {
        return options.get(selected).get();
    }

    /**
     * Facilitates dynamic menu items.
     */
    private class TPMenuOption {

        private TPMenuItem item = null;
        private Supplier<TPMenuItem> itemSupplier;

        public TPMenuOption(TPMenuItem item) {
            this(() -> item);
        }

        public TPMenuOption(Supplier<TPMenuItem> itemSupplier) {
            this.itemSupplier = itemSupplier;
            update();
        }

        public void update() {
            item = itemSupplier.get();
        }

        public TPMenuItem get() {
            return item;
        }
    }

}
