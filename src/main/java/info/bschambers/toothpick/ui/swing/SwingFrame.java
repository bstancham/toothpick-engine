package info.bschambers.toothpick.ui.swing;

import info.bschambers.toothpick.game.GameProgram;
import info.bschambers.toothpick.ui.GameUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingFrame extends JFrame implements GameUI {

    private GameProgram program = null;
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
    }

    @Override
    public void repaintUI() {
        panel.repaint();
    }

    @Override
    public void setProgram(GameProgram program) {
        this.program = program;
    }

    private class SwingPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (program != null) {
                if (program.getBGImage() != null)
                    g.drawImage(program.getBGImage(), 0, 0, null);
            }
        }
    }

}
