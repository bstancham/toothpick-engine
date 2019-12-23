package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Pt;

public class TPExplosion extends TPPart {

    private Pt pos;
    private int lifetime = 50;
    private int count = 1;

    public TPExplosion(Pt pos) {
        this.pos = pos;
    }

    public Pt getPos() { return pos; }

    public double getMagnitude() {
        return (double) lifetime / (double) count;
    }

    @Override
    public TPExplosion copy() {
        return new TPExplosion(pos);
    }

    @Override
    public void update(double x, double y, double angle) {
        count++;
        if (count >= lifetime)
            if (getForm() != null)
                getForm().removePart(this);
    }

}
