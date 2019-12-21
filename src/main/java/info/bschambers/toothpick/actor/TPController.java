package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;

public class TPController {

    // controller stats
    private int statsNumDeaths = 0;
    private int statsNumKills = 0;

    protected Pt pos = Pt.ZERO;
    protected double angle = 0.0;

    public Pt pos() { return pos; }

    public void setPos(Pt val) { pos = val; }

    public double angle() { return angle; }

    public void setAngle(double val) { angle = val; }

    public void update() {}

    public void boundaryCheck(Rect bounds) {
        // wrap-around behaviour
        if (pos.x < bounds.x1)
            pos = pos.setX(bounds.x2);
        else if (pos.x > bounds.x2)
            pos = pos.setX(bounds.x1);
        else if (pos.y < bounds.y1)
            pos = pos.setY(bounds.y2);
        else if (pos.y > bounds.y2)
            pos = pos.setY(bounds.y1);
    }

    public int getNumKills() { return statsNumKills; }

    public int getNumDeaths() { return statsNumDeaths; }

    public void killEvent(Object victim, Pt p) {
        statsNumKills++;
    }

    public void deathEvent(Object killer, Pt p) {
        statsNumDeaths++;
    }

}
