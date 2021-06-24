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
    public void update(TPActor a) {
        this.x = a.x;
        this.y = a.y;
        super.update(a);
    }

    @Override
    public TPImagePart copy() {
        TPImagePart other = new TPImagePart(image);
        other.x = x;
        other.y = y;
        other.copyPartProperties(this);
        return other;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        return params;
    }

}
