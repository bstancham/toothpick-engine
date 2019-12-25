package info.bschambers.toothpick;

import info.bschambers.toothpick.ui.TPUI;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class SlideShowProgram extends TPProgram {

    private List<Image> images = new ArrayList<>();
    private int index = 0;
    private int delay = 80; // 4 seconds at 20 fps
    private int counter = 0;

    public SlideShowProgram(String title) {
        super(title);
    }

    /** Sets image index to zero. */
    @Override
    public void init() {
        index = 0;
    }

    public void addImage(String filename) {
        try {

            File f = new File(filename);
            if (!f.exists()) {
                System.out.println("ERROR - FILE DOES NOT EXIST: " + f);
                String dir = new File(".").getCanonicalPath();
                System.out.println("... in working dir: " + dir);
            }

            Image img = ImageIO.read(f);
            addImage(img);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Image img) {
        images.add(img);
        if (getBGImage() == null) {
            index = images.size() - 1;
            setBGImage(images.get(index));
        }
    }

    @Override
    public void update() {
        counter++;
        if (counter >= delay) {
            counter = 0;
            index++;
            if (index >= images.size()) {
                index = 0;
            }
            if (images.size() > 0) {
                setBGImage(images.get(index));
            }
        }
    }

}
