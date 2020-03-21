package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import info.bschambers.toothpick.ui.TPMenu;
import info.bschambers.toothpick.ui.TPMenuItem;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

public class Gfx {

    public static final Stroke STROKE_0 = new BasicStroke(0);
    public static final Stroke STROKE_1 = new BasicStroke(1);
    public static final Stroke STROKE_2 = new BasicStroke(2);
    public static final Stroke STROKE_3 = new BasicStroke(3);
    public static final Stroke STROKE_4 = new BasicStroke(4);
    public static final Stroke STROKE_5 = new BasicStroke(5);

    private static final Stroke[] strokes0To5 = new Stroke[] {
        STROKE_0,
        STROKE_1,
        STROKE_2,
        STROKE_3,
        STROKE_4,
        STROKE_5
    };

    public static Stroke getStrokeForLineStrength(TPLine tpl) {
        int strength = tpl.getStrength();
        if (strength <= 0)
            return STROKE_0;
        if (strength >= 5)
            return STROKE_5;
        return strokes0To5[strength];
    }

    public static void setStroke(Graphics g, Stroke s) {
        if (g instanceof Graphics2D)
            ((Graphics2D) g).setStroke(s);
    }

    public static void line(Graphics g, double x1, double y1, double x2, double y2) {
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    public static void line(Graphics g, TPGeometry geom, Stroke str, Pt start, Pt end) {
        line(g, geom, str, start.x, start.y, end.x, end.y);
    }

    public static void line(Graphics g, TPGeometry geom, Stroke str,
                            double x1, double y1, double x2, double y2) {
        if (g instanceof Graphics2D)
            ((Graphics2D) g).setStroke(str);
        g.drawLine((int) geom.xToScreen(x1), (int) geom.yToScreen(y1),
                   (int) geom.xToScreen(x2), (int) geom.yToScreen(y2));
    }

    public static void rectangle(Graphics g, Rect r) {
        int w = r.x2 - r.x1;
        int h = r.y2 - r.y1;
        rectangle(g, r.x1, r.y1, w, h);
    }

    public static void rectangle(Graphics g, int x, int y, int w, int h) {
        setStroke(g, STROKE_1);
        int x2 = x + w;
        int y2 = y + h;
        line(g, x, y, x2, y);
        line(g, x, y2, x2, y2);
        line(g, x, y, x, y2);
        line(g, x2, y, x2, y2);
    }

    public static void rectangle(Graphics g, TPGeometry geom, Rect r) {
        int w = r.x2 - r.x1;
        int h = r.y2 - r.y1;
        rectangle(g, geom, r.x1, r.y1, w, h);
    }

    public static void rectangle(Graphics g, TPGeometry geom, int x, int y, int w, int h) {
        setStroke(g, STROKE_1);
        int x2 = x + w;
        int y2 = y + h;
        line(g, geom, STROKE_1, x, y, x2, y);
        line(g, geom, STROKE_1, x, y2, x2, y2);
        line(g, geom, STROKE_1, x, y, x, y2);
        line(g, geom, STROKE_1, x2, y, x2, y2);
    }

    public static void rectangle(Graphics g, Rectangle r) {
        setStroke(g, STROKE_1);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    public static void rectangle(Graphics g, TPGeometry geom, Rectangle r) {
        rectangle(g, geom, r.x, r.y, r.width, r.height);
    }

    public static void crosshairs(Graphics g, double x, double y, int size) {
        int s = size / 2;
        line(g, x - s, y, x + s, y);
        line(g, x, y - s, x, y + s);
    }

    public static void crosshairs(Graphics g, TPGeometry geom, double x, double y, int size) {
        int s = size / 2;
        line(g, geom, STROKE_1, x - s, y, x + s, y);
        line(g, geom, STROKE_1, x, y - s, x, y + s);
    }

    public static void centeredSquare(Graphics g, int x, int y, int size) {
        x = x - (size / 2);
        y = y - (size / 2);
        setStroke(g, STROKE_1);
        g.drawRect(x, y, size, size);
    }

    public static void arc(Graphics g, int x, int y, int size,
                           int startAngle, int arcAngle) {
	g.drawArc(x, y, size, size, startAngle, arcAngle);
    }

    public static void actor(Graphics g, TPGeometry geom, TPActor a) {
        g.setColor(a.getColor());
        form(g, geom, a.getForm());
        for (int i = 0; i < a.numChildren(); i++)
            actor(g, geom, a.getChild(i));
    }

    public static void form(Graphics g, TPGeometry geom, TPForm form) {
        for (int i = 0; i < form.numParts(); i++) {
            TPPart part = form.getPart(i);
            if (part instanceof TPLine) {
                tpLine(g, geom, (TPLine) part);
            } else if (part instanceof TPExplosion) {
                explosion(g, geom, (TPExplosion) part);
            } else if (part instanceof TPTextPart) {
                text(g, geom, (TPTextPart) part);
            } else if (part instanceof TPImagePart) {
                image(g, geom, (TPImagePart) part);
            }
        }
    }

    public static void tpLine(Graphics g, TPGeometry geom, TPLine tpl) {
        Color col = g.getColor();
        if (tpl.getColorGetter() != null) {
            g.setColor(tpl.getColorGetter().get());
        }
        line(g, geom, getStrokeForLineStrength(tpl), tpl.getLine().start, tpl.getLine().end);
        g.setColor(col);
    }

    public static void explosion(Graphics g, TPGeometry geom, TPExplosion ex) {
        double scale = geom.scale * 100;
        double mag = Math.sin(Math.PI * ex.getMagnitude());
        int size = (int) (mag * scale);
        int half = size / 2;
        int x = (int) (ex.getPos().x) - half;
        int y = (int) (ex.getPos().y) - half;
        g.fillOval((int) geom.xToScreen(x), (int) geom.yToScreen(y), size, size);
    }

    public static void text(Graphics g, TPGeometry geom, TPTextPart tpt) {
        int x = (int) geom.xToScreen(tpt.x);
        int y = (int) geom.yToScreen(tpt.y);
        g.drawString(tpt.text, x, y);
    }

    public static void image(Graphics g, TPGeometry geom, TPImagePart tpi) {
        int x = (int) geom.xToScreen(tpi.x);
        int y = (int) geom.yToScreen(tpi.y);
        g.drawImage(tpi.image, x, y, null);
    }

    public static void playerIndicator(Graphics g, TPGeometry geom, TPPlayer player) {
        boolean action = player.getActor().getActionTrigger();
        double len1 = 10;
        double len2 = 15;
        double x1 = player.getActor().x;
        double y1 = player.getActor().y;
        double angle = (player.getActor().angle + 1.5) * Math.PI;
        double x2 = x1 + Math.cos(angle) * len1;
        double y2 = y1 + Math.sin(angle) * len1;
        double x3 = x1 + Math.cos(angle) * len2;
        double y3 = y1 + Math.sin(angle) * len2;
        g.setColor(Color.CYAN);
        line(g, geom, STROKE_1, x1, y1, x2, y2);
        g.setColor((action ? Color.YELLOW : Color.RED));
        line(g, geom, STROKE_1, x2, y2, x3, y3);
    }

    public static void menu(Graphics g, TPMenu menu) {
        menu(g, menu, 30, 30);
    }

    public static void menu(Graphics g, TPMenu menu, int posX, int posY) {
        TextBox box = new TextBox();
        if (menu.isHidden()) {
            box.add("press 'h' to show menu");
            box.paint(g);
        } else {
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
