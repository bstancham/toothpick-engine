package info.bstancham.toothpick.ui;

import info.bstancham.toothpick.TPBase;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import static java.awt.event.KeyEvent.*;

public class TPMenu implements TPMenuItem {

    private Supplier<String> titleSupplier;
    private TPMenu parent = null;
    private boolean active = false;
    private boolean hidden = false;
    private boolean delegating = false;
    private List<TPMenuOption> options = new ArrayList<>();
    private int selected = 0;
    private Runnable initAction = () -> {};

    private boolean mouseInside = false;
    private int mouseOverIndex = -1;

    private List<Color> colors = new ArrayList<>();

    public Color bgColor = Color.BLUE;
    public Color defaultColor = Color.WHITE;
    public Color borderColor = Color.WHITE;
    public Color mouseOverColor = Color.RED;
    private String subMenuPrefix = ">>> ";

    public int posX = 30;
    public int posY = 30;
    public int padLeft = 10;
    public int padRight = 10;
    public int padTop = 20;
    public int padBot = 10;
    private int subMenuOffset = 50;

    // guess at size of font
    public int charWidth = 8;
    public int lineHeight = 20;

    // dynamically assigned variables
    private int longestLine = 0;
    public int textWidth = 0;
    public int textHeight = 0;
    public int width = 0;
    public int height = 0;

    private void recalculateDimensions() {

        // find longest line
        longestLine = text().length(); // title
        for (TPMenuOption opt : options) {
            if (opt == null) {
                System.out.println("NULL OPTION");
            } else {
                // System.out.println(opt);
                // System.out.println(opt.get());
                if (opt.get() == null) {
                    System.out.println("NULL ITEM");
                } else {

                    // System.out.println(opt.get().text());
                    // String str = opt.get().text();
                    if (opt.get().text() == null) {
                        System.out.println("NULL STRING");
                    } else {
                        int len = opt.get().text().length();
                        if (opt.get() instanceof TPMenu)
                            len += subMenuPrefix.length();
                        if (len > longestLine)
                            longestLine = len;
                    }
                }
            }
        }

        textWidth = charWidth * longestLine;
        // add extra lineHeight to allow for menu title
        textHeight = lineHeight * (options.size() + 1);
        width = textWidth + padLeft + padRight;
        height = textHeight + padTop + padBot;

    }

    public int optionsPosY() {
       // add 2*lineHeight to allow for menu title
        return posY + padTop + lineHeight + 5;
    }

    public int posYForItem(int index) {
        return optionsPosY() + (index * lineHeight);
    }

    /**
     / Sets {@code mouseInside} and {@code mouseOverIndex}.
    */
    public void mouseMoved(int mouseX, int mouseY) {

        if (delegating) {
            TPMenuItem item = getSelectedItem();
            if (item instanceof TPMenu) {
                ((TPMenu) item).mouseMoved(mouseX, mouseY);
            }
        }

        // need to set mouseInside, whether delegating or not
        if (mouseX >= posX &&
            mouseY >= posY &&
            mouseX <= posX + width &&
            mouseY <= posY + height) {

            mouseInside = true;

            // if NOT delegating, need to check whether mouse is over any item
            if (!delegating) {
                int y = mouseY - optionsPosY();
                mouseOverIndex = y / lineHeight;
                if (mouseOverIndex >= getNumItems())
                    mouseOverIndex = getNumItems() - 1;
            }

        } else {
            // mouse pointer NOT inside menu
            mouseInside = false;
            mouseOverIndex = -1;
        }
    }

    public int getMouseOverIndex() {
        return mouseOverIndex;
    }

    /**
     * If mouse is over an item, invoke it's action.
     *
     * If delegating, pass click on to selected item using recursion.
     *
     * If delegating, click on old menu to go back.
     *
     * @return True, if mouse pointer is inside menu.
     */
    public boolean mouseClicked() {

        if (delegating) {
            boolean ret = false;
            TPMenuItem item = getSelectedItem();
            if (item instanceof TPMenu) {
                ret = ((TPMenu) item).mouseClicked();
            }
            if (ret) {
                return true;
            } else {
                if (mouseInside) {
                    delegating = false;
                    return true;
                }
                return false;
            }
        } else {

            if (mouseInside) {
                if (mouseOverIndex >= 0 &&
                    mouseOverIndex < getNumItems()) {
                    selected = mouseOverIndex;
                    action(Code.RET, -1);
                }
                return true;
            }

            return false;
        }
    }

    public TPMenu(String title) {
        this(() -> title);
    }

    public TPMenu(Supplier<String> titleSupplier) {
        this.titleSupplier = titleSupplier;
    }

    /**
     * Discards all menu-options.
     */
    public void clear() {
        options.clear();
        colors.clear();
    }

    @Override
    public String text() {
        return titleSupplier.get();
    }

    @Override
    public void action(Code c, int keyCode) {
        if (c == Code.HIDE) {
            hidden = !hidden;
        } else if (!hidden) {
            TPMenuOption opt = getSelectedOption();
            TPMenuItem item = opt.get();
            if (delegating && item instanceof TPMenu) {
                item.action(c, keyCode);
            } else if (delegating) {
                System.out.println("ERROR - can't delegate to menu-item type: "
                                   + item.getClass());
            } else if (c == Code.RET) {
                if (item instanceof TPMenu) {
                    setDelegating(true);
                    opt.update();
                    TPMenu m = (TPMenu) opt.get();
                    m.setParent(this);

                    m.posX = posX + subMenuOffset;
                    m.posY = posY + subMenuOffset;

                    m.runInitAction();
                } else {
                    getSelectedOption().get().action(c, keyCode);
                }
            } else if (c == Code.CANCEL) {
                cancel();
            } else if (c == Code.UP) {
                incrSelected(-1);
            } else if (c == Code.DOWN) {
                incrSelected(1);
            } else if (c == Code.LEFT) {
                getSelectedOption().get().action(c, keyCode);
            } else if (c == Code.RIGHT) {
                getSelectedOption().get().action(c, keyCode);
            } else {
                getSelectedOption().get().action(c, keyCode);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
        case VK_ENTER:
            action(TPMenuItem.Code.RET, keyCode);
            break;
        case VK_BACK_SPACE:
            action(TPMenuItem.Code.CANCEL, keyCode);
            break;
        case VK_H:
            action(TPMenuItem.Code.HIDE, keyCode);
            break;
        case VK_UP:
            action(TPMenuItem.Code.UP, keyCode);
            break;
        case VK_DOWN:
            action(TPMenuItem.Code.DOWN, keyCode);
            break;
        case VK_LEFT:
            action(TPMenuItem.Code.LEFT, keyCode);
            break;
        case VK_RIGHT:
            action(TPMenuItem.Code.RIGHT, keyCode);
            break;
        default:
            action(TPMenuItem.Code.UNDEFINED, keyCode);
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
        colors.add(defaultColor);
        recalculateDimensions();
    }

    /**
     * Add a dynamic menu item - the menu item will be fetched anew at the time when it is
     * selected.
     */
    public void add(Supplier<TPMenuItem> itemSupplier) {
        options.add(new TPMenuOption(itemSupplier));
        colors.add(defaultColor);
        recalculateDimensions();
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

    public String getMenuText(int index) {
        TPMenuItem item = getItem(index);
        if (item instanceof TPMenu)
            return subMenuPrefix + item.text();
        else
            return item.text();
    }

    public Color getColor(int index) {
        return colors.get(index);
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
    public static class TPMenuOption {

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
