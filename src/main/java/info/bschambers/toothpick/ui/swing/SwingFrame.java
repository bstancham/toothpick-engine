package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.game.GameProgram;
import info.bschambers.toothpick.ui.ATMenu;
import info.bschambers.toothpick.ui.ATMenuItem;
import info.bschambers.toothpick.ui.GameUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static java.awt.event.KeyEvent.*;

public class SwingFrame extends JFrame implements GameUI, KeyListener {

    private GameProgram program = null;
    private ATMenu menu = null;
    private int xDim = 640;
    private int yDim = 430;
    private SwingPanel panel;

    public SwingFrame(String title) {
        super(title);
        setBounds(50, 50, xDim, yDim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        panel = new SwingPanel();
        panel.setBackground(Color.BLUE);
        setContentPane(panel);
        addKeyListener(this);
    }

    @Override
    public void repaintUI() {
        panel.repaint();
    }

    @Override
    public void setProgram(GameProgram program) {
        this.program = program;
    }

    @Override
    public void setMenu(ATMenu menu) {
        this.menu = menu;
    }

    public void exit() {
        System.out.println("... goodbye!");
        dispose();
        System.exit(0);
    }

    private class SwingPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintBackground(g);
            paintMenu(g);
        }

        private void paintBackground(Graphics g) {
            if (program != null) {
                if (program.getBGImage() != null)
                    g.drawImage(program.getBGImage(), 0, 0, null);
            }
        }

        private void paintMenu(Graphics g) {
            if (menu != null && menu.isActive())
                Gfx.paintMenu(g, menu);
        }
    }

    /*---------------------- KeyListener methods -----------------------*/

    @Override
    public void keyPressed(KeyEvent e) {
        int ck = e.getKeyCode();
        if (menu == null) {
            System.out.println("NO MENU PRESENT!");
        } else {
            if (ck == VK_ESCAPE) {
                menu.setActive(!menu.isActive());
                System.out.println("MENU ACTIVE = " + menu.isActive());
            } else if (ck == VK_ENTER) {
                menu.action(ATMenuItem.Code.RET);
            } else if (ck == VK_UP) {
                menu.action(ATMenuItem.Code.UP);
            } else if (ck == VK_DOWN) {
                menu.action(ATMenuItem.Code.DOWN);
            } else if (ck == VK_LEFT) {
                menu.action(ATMenuItem.Code.LEFT);
            } else if (ck == VK_RIGHT) {
                menu.action(ATMenuItem.Code.RIGHT);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
