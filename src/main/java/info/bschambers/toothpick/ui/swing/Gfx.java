package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Gfx {

    private static final TPGeometry TPG = new TPGeometry();

    public static void line(Graphics g, Line ln) {
        line(g, TPG, ln);
    }

    public static void line(Graphics g, TPGeometry geom, Line ln) {
        line(g, geom, ln.start, ln.end);
    }

    public static void line(Graphics g, Pt start, Pt end) {
        line(g, TPG, start, end);
    }

    public static void line(Graphics g, TPGeometry geom, Pt start, Pt end) {
        line(g, geom, start.x, start.y, end.x, end.y);
    }

    public static void line(Graphics g, TPGeometry geom,
                            double x1, double y1, double x2, double y2) {
        g.drawLine((int) geom.getX(x1), (int) geom.getY(y1),
                   (int) geom.getX(x2), (int) geom.getY(y2));
    }

    public static void rectangle(Graphics g, Rect r) {
        rectangle(g, TPG, r);
    }

    public static void rectangle(Graphics g, TPGeometry geom, Rect r) {
        int w = r.x2 - r.x1;
        int h = r.y2 - r.y1;
        rectangle(g, geom, r.x1, r.y1, w, h);
    }

    public static void rectangle(Graphics g, TPGeometry geom, int x, int y, int w, int h) {
        int x2 = x + w;
        int y2 = y + h;
        // manually drawing the lines of the rectangle - for some reason I couldn't get
        // g.drawRect to behave properly with scaling...
        line(g, geom, x, y, x2, y);
        line(g, geom, x, y2, x2, y2);
        line(g, geom, x, y, x, y2);
        line(g, geom, x2, y, x2, y2);
    }

    public static void rectangle(Graphics g, Rectangle r) {
        rectangle(g, TPG, r);
    }

    public static void rectangle(Graphics g, TPGeometry geom, Rectangle r) {
        g.drawRect((int) geom.getX(r.x), (int) geom.getY(r.y),
                   (int) geom.scale * r.width, (int) geom.scale * r.height);
    }



    public static void crosshairs(Graphics g, Pt p, int size) {
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

    public static void arc(Graphics g, int x, int y, int size,
                           int startAngle, int arcAngle) {
	g.drawArc(x, y, size, size, startAngle, arcAngle);
    }

    public static void actor(Graphics g, TPGeometry geom, TPActor a) {
        g.setColor(a.getColor());
        form(g, geom, a.getForm());
    }

    public static void form(Graphics g, TPGeometry geom, TPForm form) {
        for (int i = 0; i < form.numParts(); i++) {
            TPPart part = form.getPart(i);
            if (part instanceof TPLine) {
                line(g, geom, ((TPLine) part).getLine());
            } else if (part instanceof TPExplosion) {
                explosion(g, geom, (TPExplosion) part);
            }
        }
    }

    public static void explosion(Graphics g, TPGeometry geom, TPExplosion ex) {
        double scale = 70;
        double mag = Math.sin(Math.PI * ex.getMagnitude());
        int size = (int) (mag * scale);
        int half = size / 2;
        int x = (int) (ex.getPos().x) - half;
        int y = (int) (ex.getPos().y) - half;
        g.fillOval((int) geom.getX(x), (int) geom.getY(y), size, size);
    }

    public static void menu(Graphics g, TPMenu menu) {
        menu(g, menu, 30, 30);
    }

    public static void menu(Graphics g, TPMenu menu, int posX, int posY) {
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
                menu(g, (TPMenu) item, posX + 30, posY + 30);
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
