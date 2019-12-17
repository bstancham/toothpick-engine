package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.ui.ATMenu;
import info.bschambers.toothpick.ui.ATMenuItem;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Gfx {

    public static void paintMenu(Graphics g, ATMenu menu) {
        paintMenu(g, menu, 30, 30);
    }

    public static void paintMenu(Graphics g, ATMenu menu, int posX, int posY) {
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
            ATMenuItem item = menu.getSelectedItem();
            if (item instanceof ATMenu) {
                paintMenu(g, (ATMenu) item, posX + 30, posY + 30);
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
