package info.bschambers.toothpick.geom;

/**
 * <p>Immutable data type representing a 2D cartesian co-ordinate with double values.</p>
 *
 * <p>All methods are without side-effects. Transformation methods return a new
 * {@code Pt} object.</p>
 */
public final class Pt {

    public static final Pt ZERO = new Pt(0.0, 0.0);
    public static final Pt ONE = new Pt(1.0, 1.0);

    public final double x;
    public final double y;

    public Pt(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    /**
     * @return True, if x and y both equal zero.
     */
    public boolean isZero() {
        return (x == 0 && y == 0);
    }

    /**
     * @return True, if x and y values are identical.
     */
    @Override
    public boolean equals(Object x) {
        if (x == this) return true;
        if (x == null) return false;
        if (x.getClass() != this.getClass()) return false;
        Pt that = (Pt) x;
        if (this.x == that.x &&
            this.y == that.y) return true;
        return false;
    }

    public boolean equals(Pt p, double delta) {
        if (p.x < x - delta) return false;
        if (p.y < y - delta) return false;
        if (p.x > x + delta) return false;
        if (p.y > y + delta) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) + Double.hashCode(y);
    }

    public Pt setX(double val) {
        return new Pt(val, y);
    }

    public Pt setY(double val) {
        return new Pt(x, val);
    }

    public Pt addX(double amt) {
        return new Pt(x + amt, y);
    }

    public Pt addY(double amt) {
        return new Pt(x, y + amt);
    }

    public Pt add(Pt p) {
        return new Pt(x + p.x, y + p.y);
    }

    public Pt add(double x, double y) {
        return new Pt(this.x + x, this.y + y);
    }

    /**
     * @return A new {@code Pt} with x and y values inverted.
     */
    public Pt invert() { return new Pt(-x, -y); }

    /**
     * @return A new {@code Pt} which is a reflection of this point.
     */
    public Pt reflect(Pt center) {
	double xDist = center.x - x;
	double yDist = center.y - y;
        return new Pt(center.x + xDist, center.y + yDist);
    }

    public static Pt midPoint(Pt a, Pt b) {
        double xdist = b.x - a.x;
        double ydist = b.y - a.y;
        return new Pt(a.x + (0.5f * xdist), a.y + (0.5f * ydist));
    }

}
