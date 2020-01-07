package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;

public class TPText extends TPPart {

    public String text = "...";
    public double x = 0;
    public double y = 0;

    public TPText() {}

    public TPText(String text) {
        this.text = text;
    }

    @Override
    public void update(double x, double y, double angle) {
        this.x = x;
        this.y = y;
    }

    @Override
    public TPText copy() {
        TPText other = new TPText(text);
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
