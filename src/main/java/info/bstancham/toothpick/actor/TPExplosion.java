package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncoding;
import info.bstancham.toothpick.geom.Pt;

public class TPExplosion extends TPPart {

    private Pt pos = Pt.ZERO;
    private int lifetime = 50;
    private int count = 1;

    public TPExplosion() {}

    public TPExplosion(Pt pos) { this.pos = pos; }

    public Pt getPos() { return pos; }

    public void setPos(Pt pos) { this.pos = pos; }

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
    public void update(TPActor a) {
        count++;
        if (count >= lifetime)
            die(a);
        super.update(a);
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Pt.class, getPos(), "setPos");
        return params;
    }

}
