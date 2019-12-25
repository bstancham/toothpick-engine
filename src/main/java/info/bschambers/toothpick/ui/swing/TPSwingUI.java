package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import info.bschambers.toothpick.ui.TPUI;
import info.bschambers.toothpick.ui.swing.Gfx.TextBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static java.awt.event.KeyEvent.*;

public class TPSwingUI extends JFrame implements TPUI, KeyListener {

    private TPProgram program = TPProgram.NULL;
    private TPMenu menu = new TPMenu("EMPTY MENU");
    private int xDim = 1000;
    private int yDim = 800;
    private TPSwingPanel panel;
    private List<Supplier<String>> infoGetters = new ArrayList<>();
    private Color bgColor = Color.BLUE;

    public TPSwingUI(String title) {
        super(title);
        setBounds(50, 50, xDim, yDim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        panel = new TPSwingPanel();
        setContentPane(panel);
        addKeyListener(this);
    }

    @Override
    public void repaintUI() {
        panel.paintImmediately(0, 0, xDim, yDim);
    }

    public TPProgram getProgram() {
        return program;
    }

    @Override
    public void setProgram(TPProgram program) {
        this.program = program;
    }

    @Override
    public void setMenu(TPMenu menu) {
        this.menu = menu;
    }

    @Override
    public void addInfoGetter(Supplier<String> getter) {
        infoGetters.add(getter);
    }

    public void exit() {
        System.out.println("... goodbye!");
        dispose();
        System.exit(0);
    }

    private class TPSmearImage extends BufferedImage {

        public TPSmearImage() {
            super(xDim, yDim, BufferedImage.TYPE_INT_RGB);
        }

        public void updateImage(boolean paintBG) {
            Graphics2D g = createGraphics();
            if (paintBG) {
                g.setColor(bgColor);
                g.fillRect(0, 0, xDim, yDim);
            }
            paintBackground(g);
            paintActors(g);
            paintOverlay(g);
        }
    }

    private class TPSwingPanel extends JPanel {

        private TPSmearImage img;
        private boolean firstSmear = true;

        public TPSwingPanel() {
            img = new TPSmearImage();
        }

        @Override
        public void paintComponent(Graphics g) {

            // check whether background color has changed
            if (bgColor != program.getBGColor()) {
                bgColor = program.getBGColor();
                setBackground(bgColor);
                firstSmear = true;
            }

            super.paintComponent(g);

            if (program.isSmearMode()) {
                img.updateImage(firstSmear);
                g.drawImage(img, 0, 0, null);
                firstSmear = false;
            } else {
                firstSmear = true;
                paintBackground(g);
                paintActors(g);
                paintOverlay(g);
            }

            paintInfo(g);
            paintMenu(g);
        }
    }

    protected void paintBackground(Graphics g) {
        if (program.getBGImage() != null)
            g.drawImage(program.getBGImage(), 0, 0, null);
    }

    protected void paintActors(Graphics g) {
        for (int i = 0; i < program.numActors(); i++)
            Gfx.paintActor(g, program.getActor(i));
    }

    protected void paintOverlay(Graphics g) {
        if (program.isShowIntersections()) {
            g.setColor(Color.YELLOW);
            for (Pt p : program.getIntersectionPoints())
                Gfx.paintCrosshairs(g, p, 10);
        }
    }

    protected void paintInfo(Graphics g) {
        Gfx.TextBox box = new Gfx.TextBox(null, Color.WHITE, null);
        box.posX = getWidth() - 150;
        box.posY = 15;
        for (String line : program.getInfoLines())
            box.add(line);
        for (Supplier<String> getter : infoGetters)
            box.add(getter.get());
        box.paint(g);
    }

    protected void paintMenu(Graphics g) {
        if (menu.isActive())
            Gfx.paintMenu(g, menu);
    }

    /*---------------------- KeyListener methods -----------------------*/

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == VK_ESCAPE) {
            menu.setActive(!menu.isActive());
        } else if (menu.isActive()) {
            keyPressedMenu(e);
        } else {
            keyPressedGame(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!menu.isActive())
            keyReleasedGame(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void keyPressedMenu(KeyEvent e) {
        switch (e.getKeyCode()) {
        case VK_ENTER:
            menu.action(TPMenuItem.Code.RET);
            break;
        case VK_BACK_SPACE:
            menu.action(TPMenuItem.Code.CANCEL);
            break;
        case VK_UP:
            menu.action(TPMenuItem.Code.UP);
            break;
        case VK_DOWN:
            menu.action(TPMenuItem.Code.DOWN);
            break;
        case VK_LEFT:
            menu.action(TPMenuItem.Code.LEFT);
            break;
        case VK_RIGHT:
            menu.action(TPMenuItem.Code.RIGHT);
            break;
        }
    }

    private void keyPressedGame(KeyEvent e) {
        program.getPlayer().getInputHandler().setKey(e.getKeyCode(), true);
    }

    private void keyReleasedGame(KeyEvent e) {
        program.getPlayer().getInputHandler().setKey(e.getKeyCode(), false);
    }

}
