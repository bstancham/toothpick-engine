package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;

public class TPTextPart extends TPPart {

    public String text = "...";
    public double x = 0;
    public double y = 0;

    public TPTextPart() {}

    public TPTextPart(String text) { this.text = text; }

    @Override
    public void update(TPActor a) {
        this.x = a.x;
        this.y = a.y;
        super.update(a);
    }

    @Override
    public TPTextPart copy() {
        TPTextPart other = new TPTextPart(text);
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
