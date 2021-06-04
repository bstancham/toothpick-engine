package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.TPPlatform;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import info.bschambers.toothpick.ui.TPMenuItemSimple;
import info.bschambers.toothpick.ui.TPUI;
import info.bschambers.toothpick.ui.swing.Gfx.TextBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static java.awt.event.KeyEvent.*;

public class TPSwingUI extends JFrame
    implements TPUI, KeyListener, MouseListener, MouseMotionListener, ComponentListener {

    private TPPlatform platform = new TPPlatform("DEFAULT PLATFORM");
    private TPMenu menu = new TPMenu("EMPTY MENU");
    private TPSwingPanel panel;
    private List<Supplier<String>> infoGetters = new ArrayList<>();
    private Color bgColor = Color.BLUE;

    public TPSwingUI(String title) {
        super(title);
        setBounds(100, 50, 1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        panel = new TPSwingPanel();
        setContentPane(panel);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
    }

    @Override
    public void updateUI() {
        if (getPlatform().programChanged()) {
            getProgram().fitUI(this);
            getPlatform().setProgramChanged(false);
        }
        panel.paintImmediately(0, 0, getWidth(), getHeight());
    }

    protected TPPlatform getPlatform() {
        return platform;
    }

    public TPProgram getProgram() {
        return getPlatform().getProgram();
    }

    @Override
    public void setPlatform(TPPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void setMenu(TPMenu menu) {
        this.menu = menu;
    }

    @Override
    public int getUIWidth() {
        return Math.max(10, getWidth());
    }

    @Override
    public int getUIHeight() {
        return Math.max(10, getHeight());
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
            super(TPSwingUI.this.getWidth(), TPSwingUI.this.getHeight(),
                  BufferedImage.TYPE_INT_RGB);
        }

        public void updateImage(boolean paintBG) {
            Graphics2D g = createGraphics();
            if (paintBG) {
                g.setColor(bgColor);
                g.fillRect(0, 0, TPSwingUI.this.getWidth(), TPSwingUI.this.getHeight());
            }
            paintBackground(g);
            paintActors(g);
        }
    }

    private class TPSwingPanel extends JPanel {

        private TPSmearImage img;
        private boolean firstSmear = true;
        private boolean resizeSmear = false;

        public TPSwingPanel() {
            img = new TPSmearImage();
        }

        public void schedulteResizeSmearImage() {
            resizeSmear = true;
        }

        @Override
        public void paintComponent(Graphics g) {
            // check whether background color has changed
            if (bgColor != getProgram().getBGColor()) {
                bgColor = getProgram().getBGColor();
                setBackground(bgColor);
                firstSmear = true;
            }

            super.paintComponent(g);

            if (getProgram().isSmearMode()) {
                // if window has been resized then need to resize the smear-image
                if (resizeSmear) {
                    img = new TPSmearImage();
                    paintBackground(g);
                    resizeSmear = false;
                    firstSmear = true; // will repaint background
                }
                img.updateImage(firstSmear);
                g.drawImage(img, 0, 0, null);
                firstSmear = false;
            } else {
                firstSmear = true;
                paintBackground(g);
                paintActors(g);
            }

            paintOverlay(g);
            paintInfo(g);
            paintMenu(g);
        }
    }

    protected void paintBackground(Graphics g) {
        // background image
        if (getProgram().getBGImage() != null)
            g.drawImage(getProgram().getBGImage(), 0, 0,
                        panel.getWidth(), panel.getHeight(), null);
    }

    protected void paintActors(Graphics g) {
        for (int i = 0; i < getProgram().numActors(); i++)
            Gfx.actor(g, getProgram().getGeometry(), getProgram().getActor(i));
    }

    protected void paintOverlay(Graphics g) {
        // player indicator
        for (int i = 0; i < getProgram().numPlayers(); i++)
            if (getProgram().getPlayer(i).getActor().isAlive())
                Gfx.playerIndicator(g, getProgram().getGeometry(), getProgram().getPlayer(i));
        // boundary rectangle
        g.setColor(Color.GRAY);
        TPGeometry geom = getProgram().getGeometry();
        Gfx.rectangle(g, geom, 0, 0, geom.getWidth(), geom.getHeight());
        // line intersection points
        if (getProgram().isShowIntersections()) {
            g.setColor(Color.YELLOW);
            for (Pt p : getProgram().getIntersectionPoints())
                Gfx.crosshairs(g, getProgram().getGeometry(), (int) p.x, (int) p.y, 10);
        }
        // bounding boxes
        if (getProgram().isShowBoundingBoxes()) {
            g.setColor(Color.CYAN);
            for (TPActor a : getProgram()) {
                Gfx.rectangle(g, getProgram().getGeometry(), a.getForm().getBoundingBox());
                Gfx.crosshairs(g, getProgram().getGeometry(), (int) a.x, (int) a.y, 20);
            }
        }
    }

    protected void paintInfo(Graphics g) {
        Gfx.TextBox box = new Gfx.TextBox(null, Color.WHITE, null);
        box.posX = getWidth() - 150;
        box.posY = 15;
        for (String line : getProgram().getInfoLines())
            box.add(line);
        for (Supplier<String> getter : infoGetters)
            box.add(getter.get());
        box.paint(g);
    }

    protected void paintMenu(Graphics g) {
        if (menu.isActive())
            Gfx.menu(g, menu);
    }

    /*------------------------- Keyboard Input -------------------------*/

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
        menu.keyPressed(e);
    }

    private void keyPressedGame(KeyEvent e) {
        getProgram().keyPressed(e);
    }

    private void keyReleasedGame(KeyEvent e) {
        getProgram().keyReleased(e);
    }

    /*-------------------------- Mouse Input ---------------------------*/

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    /*------------------------ window resizing -------------------------*/

    @Override
    public void componentResized(ComponentEvent e) {
        getProgram().fitUI(this);
        panel.schedulteResizeSmearImage();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    /*-------------------------- default menu --------------------------*/

    public static TPMenu makeDefaultMenu(TPSwingUI ui) {
        TPMenu m = new TPMenu("MAIN MENU");
        m.add(new TPMenuItemSimple("EXIT", () -> ui.exit()));
        return m;
    }

}
