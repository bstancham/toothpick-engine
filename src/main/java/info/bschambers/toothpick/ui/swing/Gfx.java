package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.actor.Actor;
import info.bschambers.toothpick.actor.ActorForm;
import info.bschambers.toothpick.actor.LinesForm;
import info.bschambers.toothpick.actor.ImageForm;
import info.bschambers.toothpick.actor.TextForm;
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

    public static void paintActor(Graphics g, Actor a) {
        Pt pos = a.getController().position();
        if (a.getForm() instanceof LinesForm) {
            paintLinesForm(g, (LinesForm) a.getForm(), pos);
        } else if (a.getForm() instanceof ImageForm) {
            paintImageForm(g, (ImageForm) a.getForm(), pos);
        } else if (a.getForm() instanceof TextForm) {
            paintTextForm(g, (TextForm) a.getForm(), pos);
        }
    }

    public static void paintLinesForm(Graphics g, LinesForm form, Pt pos) {
        g.setColor(Color.PINK);
        for (int i = 0; i < form.numLines(); i++)
            paintLine(g, form.getLine(i).getLine().shift(pos));
    }

    public static void paintImageForm(Graphics g, ImageForm form, Pt pos) {
    }

    public static void paintTextForm(Graphics g, TextForm form, Pt pos) {
    }

    public static void paintMenu(Graphics g, TPMenu menu) {
        paintMenu(g, menu, 30, 30);
    }

    public static void paintMenu(Graphics g, TPMenu menu, int posX, int posY) {
        TextBox box = new TextBox();
        box.add(menu.text());
        box.add("");
        for (int i = 0; i < menu.getNumItems(); i++)
            box.add(menu.getItem(i).text());
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

        public int posX = 30;
        public int posY = 30;
        public Color bgColor = Color.BLUE;
        public Color defaultColor = Color.WHITE;
        public int padLeft = 10;
        public int padRight = 10;
        public int padTop = 10;
        public int padBot = 10;
        // guess at size of font
        public int charWidth = 8;
        public int lineHeight = 20;
        public int textWidth = 0;
        public int textHeight = 0;

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
            g.setColor(bgColor);
            g.fillRect(posX, posY, width, height);
            g.setColor(defaultColor);
            g.drawRect(posX, posY, width, height);
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
