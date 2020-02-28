package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import java.awt.Image;

public class TPImagePart extends TPPart {

    public Image image = null;
    public double x = 0;
    public double y = 0;

    public TPImagePart() {}

    public TPImagePart(Image image) {
        this.image = image;
    }

    @Override
    public void update(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        super.update(x, y, angle);
    }

    @Override
    public TPImagePart copy() {
        TPImagePart other = new TPImagePart(image);
        other.x = x;
        other.y = y;
        other.copyBehaviours(this);
        return other;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        return params;
    }

}
