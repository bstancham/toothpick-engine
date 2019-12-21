package info.bschambers.toothpick.geom;

public class Rect {

    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;

    public Rect(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Pt center() {
        int xLen = x2 - x1;
        int yLen = y2 - y1;
        return new Pt(x1 + (xLen / 2), y1 + (yLen / 2));
    }

}
