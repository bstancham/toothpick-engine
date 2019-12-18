package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.game.GameProgram;
import info.bschambers.toothpick.ui.GameUI;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static java.awt.event.KeyEvent.*;

public class SwingUI extends JFrame implements GameUI, KeyListener {

    private GameProgram program = null;
    private TPMenu menu = null;
    private int xDim = 640;
    private int yDim = 430;
    private SwingPanel panel;

    public SwingUI(String title) {
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
    public void setMenu(TPMenu menu) {
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
            paintActors(g);
            paintMenu(g);
        }

        private void paintBackground(Graphics g) {
            if (program != null) {
                if (program.getBGImage() != null)
                    g.drawImage(program.getBGImage(), 0, 0, null);
            }
        }

        private void paintActors(Graphics g) {
            if (program != null) {
                for (int i = 0; i < program.numActors(); i++) {
                    Gfx.paintActor(g, program.getActor(i));
                }
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
        } else if (ck == VK_ESCAPE) {
            menu.setActive(!menu.isActive());
            System.out.println("MENU ACTIVE = " + menu.isActive());
        } else if (menu.isActive()) {
            if (ck == VK_ENTER) {
                menu.action(TPMenuItem.Code.RET);
            } else if (ck == VK_BACK_SPACE) {
                menu.action(TPMenuItem.Code.CANCEL);
            } else if (ck == VK_UP) {
                menu.action(TPMenuItem.Code.UP);
            } else if (ck == VK_DOWN) {
                menu.action(TPMenuItem.Code.DOWN);
            } else if (ck == VK_LEFT) {
                menu.action(TPMenuItem.Code.LEFT);
            } else if (ck == VK_RIGHT) {
                menu.action(TPMenuItem.Code.RIGHT);
            }
        } else {
            System.out.println("MENU NOT ACTIVE!");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
