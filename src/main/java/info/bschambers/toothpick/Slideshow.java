package info.bschambers.toothpick;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Slideshow implements ProgramBehaviour {

    private List<Image> images = new ArrayList<>();
    private int index = 0;
    private int delay = 400;
    private int counter = 0;

    @Override
    public void update(TPProgram prog) {
        if (images.size() > 0) {
            if (prog.getBGImage() == null) {
                prog.setBGImage(images.get(index));
            } else {
                counter++;
                if (counter >= delay) {
                    counter = 0;
                    index++;
                    if (index >= images.size())
                        index = 0;
                    prog.setBGImage(images.get(index));
                }
            }
        }
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
    }

}
