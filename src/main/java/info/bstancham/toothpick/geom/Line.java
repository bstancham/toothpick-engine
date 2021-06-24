package info.bstancham.toothpick.geom;

/**
 * <p>Immutable data type representing a 2D line with double values.</p>
 *
 * <p>All methods are without side-effects. Transformation methods return a new
 * {@code Line} object.</p>
 */
public final class Line {

    public final Pt start;
    public final Pt end;

    public Line(double x1, double y1, double x2, double y2) {
        this(new Pt(x1, y1), new Pt(x2, y2));
    }

    public Line(Pt start, Pt end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Line that = (Line) obj;
        if (!that.start.equals(this.start)) return false;
        if (!that.end.equals(this.end)) return false;
        return true;
    }

    public boolean equalsIgnorePolarity(Line ln) {
        if (this.equals(ln))
            return true;
        if (this.start.equals(ln.end) && this.end.equals(ln.start))
            return true;
        return false;
    }

    public boolean isVertical() {
        return start.x == end.x && start.y != end.y;
    }

    public boolean isHorizontal() {
        return start.x != end.x && start.y == end.y;
    }

    public Line shift(double x, double y) {
        return new Line(start.add(x, y), end.add(x, y));
    }

    public Line shift(Pt amt) {
        return new Line(start.add(amt), end.add(amt));
    }

    public Line rotate(double amount) {
        return rotate(amount, Pt.ZERO);
    }

    public Line rotate(double amount, Pt center) {
        return new Line(Geom.rotate(start, amount, center),
                        Geom.rotate(end, amount, center));
    }

    public Pt center() { return Geom.midPoint(this); }

    /** May return a negative number. */
    public double xDist() { return end.x - start.x; }

    /** May return a negative number. */
    public double yDist() { return end.y - start.y; }

    public Pt constant() { return start; }

    /**
     * Vector is directional, and will return a negative value if end point val
     * is lower than start point val.
     */
    public double xVector() { return end.x - start.x; }
    public double yVector() { return end.y - start.y; }

    public double xGradient() {
	if (yVector() == 0) { return 0; } // don't want to divide by zero!
	return xVector() / yVector();
    }

    public double yGradient() {
	if (xVector() == 0) { return 0; } // don't want to divide by zero!
	return yVector() / xVector();
    }

}
