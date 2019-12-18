package info.bschambers.toothpick.geom;

/**
 * <p>Immutable data type representing a 2D line with double values.</p>
 *
 * <p>All methods are without side-effects. Transformation methods return a new
 * {@code Line} object.</p>
 */
public final class Line {

    public final Pt start;
    public final Pt end;

    public Line(Pt start, Pt end) {
        this.start = start;
        this.end = end;
    }

    public Line shift(Pt amt) {
        return new Line(start.add(amt), end.add(amt));
    }

}
