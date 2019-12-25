package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Gfx {

    public static void paintLine(Graphics g, Line ln) {
        g.drawLine((int) ln.start.x, (int) ln.start.y, (int) ln.end.x, (int) ln.end.y);
    }

    public static void paintCrosshairs(Graphics g, Pt p, int size) {
        int x = (int) p.x;
        int y = (int) p.y;
        int s = size / 2;
        g.drawLine(x - s, y, x + s, y);
        g.drawLine(x, y - s, x, y + s);
    }

    public static void centeredSquare(Graphics g, int x, int y, int size) {
        x = x - (size / 2);
        y = y - (size / 2);
        g.drawRect(x, y, size, size);
    }

    public static void paintActor(Graphics g, TPActor a) {
        g.setColor(a.getColor());
        paintForm(g, a.getForm());
    }

    public static void paintForm(Graphics g, TPForm form) {
        for (int i = 0; i < form.numParts(); i++) {
            TPPart part = form.getPart(i);
            if (part instanceof TPLine) {
                paintLine(g, ((TPLine) part).getLine());
            } else if (part instanceof TPExplosion) {
                paintExplosion(g, (TPExplosion) part);
            }
        }
    }

    public static void paintExplosion(Graphics g, TPExplosion ex) {
        double scale = 70;
        double mag = Math.sin(Math.PI * ex.getMagnitude());
        int size = (int) (mag * scale);
        int half = size / 2;
        int x = (int) (ex.getPos().x) - half;
        int y = (int) (ex.getPos().y) - half;
        g.fillOval(x, y, size, size);
    }

    public static void paintMenu(Graphics g, TPMenu menu) {
        paintMenu(g, menu, 30, 30);
    }

    public static void paintMenu(Graphics g, TPMenu menu, int posX, int posY) {
        TextBox box = new TextBox();
        box.add(menu.text());
        box.add("");
        for (int i = 0; i < menu.getNumItems(); i++) {
            TPMenuItem item = menu.getItem(i);
            String line = item.text();
            if (item instanceof TPMenu)
                line = ">>> " + line;
            box.add(line);
        }
        box.posX = posX;
        box.posY = posY;
        box.paint(g);
        // mark selected item
        int sel = menu.getSelectedIndex() + 2;
        int x = 6 + box.posX;
        int y = 5 + box.posY + box.padTop + (sel * box.lineHeight);
        int w = box.textWidth;
        int h = box.lineHeight;
        g.setColor(Color.CYAN);
        g.drawRect(x, y, w, h);
        // sub-menu
        if (menu.isDelegating()) {
            TPMenuItem item = menu.getSelectedItem();
            if (item instanceof TPMenu) {
                paintMenu(g, (TPMenu) item, posX + 30, posY + 30);
            }
        }
    }

    public static class TextBox {

        private List<String> lines = new ArrayList<>();
        private List<Color> colors = new ArrayList<>();
        private int longestLine = 0;

        public Color bgColor;
        public Color defaultColor;
        public Color borderColor;

        public int posX = 30;
        public int posY = 30;
        public int padLeft = 10;
        public int padRight = 10;
        public int padTop = 10;
        public int padBot = 10;
        // guess at size of font
        public int charWidth = 8;
        public int lineHeight = 20;
        public int textWidth = 0;
        public int textHeight = 0;

        public TextBox() {
            this(Color.BLUE, Color.WHITE, Color.WHITE);
        }

        public TextBox(Color bgColor, Color defaultColor, Color borderColor) {
            this.bgColor = bgColor;
            this.defaultColor = defaultColor;
            this.borderColor = borderColor;
        }

        public void add(String str) {
            lines.add(str);
            colors.add(defaultColor);
            if (str.length() > longestLine) {
                longestLine = str.length();
                textWidth = charWidth * longestLine;
            }
            textHeight = lineHeight * lines.size();
        }

        public void paint(Graphics g) {
            // calculate overall size
            int height = textHeight + padTop + padBot;
            int width = textWidth + padLeft + padRight;
            // box
            if (bgColor != null) {
                g.setColor(bgColor);
                g.fillRect(posX, posY, width, height);
            }
            if (borderColor != null) {
                g.setColor(defaultColor);
                g.drawRect(posX, posY, width, height);
            }
            // text
            int x = posX + padLeft;
            int y = posY + padTop + lineHeight;
            for (int i = 0; i < lines.size(); i++) {
                g.setColor(colors.get(i));
                g.drawString(lines.get(i), x, y);
                y += lineHeight;
            }
        }
    }

}
