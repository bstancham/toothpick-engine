package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.geom.Pt;

public class TPExplosion extends TPPart {

    private Pt pos = Pt.ZERO;
    private int lifetime = 50;
    private int count = 1;

    public TPExplosion() {}

    public TPExplosion(Pt pos) {
        this.pos = pos;
    }

    public Pt getPos() {
        return pos;
    }

    public void setPos(Pt pos) {
        this.pos = pos;
    }

    public double getMagnitude() {
        return (double) lifetime / (double) count;
    }

    @Override
    public TPExplosion copy() {
        TPExplosion e = new TPExplosion(pos);
        e.lifetime = lifetime;
        e.count = count;
        return e;
    }

    @Override
    public void update(double x, double y, double angle) {
        count++;
        if (count >= lifetime)
            die();
        super.update(x, y, angle);
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Pt.class, getPos(), "setPos");
        return params;
    }

}
