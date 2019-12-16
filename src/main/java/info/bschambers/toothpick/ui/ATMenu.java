package info.bschambers.toothpick.ui;

import info.bschambers.toothpick.game.GameBase;

public class ATMenu implements ATMenuItem {

    private String title;
    private GameBase base;

    public ATMenu(String title) {
        this.title = title;
    }

    public void setGameBase(GameBase base) {
        this.base = base;
    }

    @Override
    public String text() { return title; }

    @Override
    public void action() {
        System.out.println("ATMenu.action() --> menu-title = " + text());
    }

}
