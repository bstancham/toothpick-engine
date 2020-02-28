package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;

public class TPTextPart extends TPPart {

    public String text = "...";
    public double x = 0;
    public double y = 0;

    public TPTextPart() {}

    public TPTextPart(String text) {
        this.text = text;
    }

    @Override
    public void update(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        super.update(x, y, angle);
    }

    @Override
    public TPTextPart copy() {
        TPTextPart other = new TPTextPart(text);
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
