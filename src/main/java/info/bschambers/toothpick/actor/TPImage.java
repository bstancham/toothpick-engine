package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import java.awt.Image;

public class TPImage extends TPPart {

    public Image image = null;
    public double x = 0;
    public double y = 0;

    public TPImage() {}

    public TPImage(Image image) {
        this.image = image;
    }

    @Override
    public void update(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        super.update(x, y, angle);
    }

    @Override
    public TPImage copy() {
        TPImage other = new TPImage(image);
        other.x = x;
        other.y = y;
        return other;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        return params;
    }

}
