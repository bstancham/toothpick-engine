package info.bschambers.toothpick.geom;

public final class Geom {

    private Geom() {}

    public static final double QUARTER_TURN = Math.PI * 0.5;
    public static final double HALF_TURN = Math.PI;
    public static final double THREE_QUARTER_TURN = Math.PI * 1.5;
    public static final double FULL_TURN = Math.PI * 2.0;

    /**
     * Returns the mid point between the two values.
     */
    public static double midVal(double d1, double d2) {
        double difference = Math.abs(d1 - d2);
        return Math.min(d1, d2) + (difference / 2);
    }

    public static Pt midPoint(Line ln) {
	return midPoint(ln.start, ln.end);
    }

    public static Pt midPoint(Pt a, Pt b) {
	return new Pt(midVal(a.x, b.x), midVal(a.y, b.y));
    }

    /** @return The distance between the two points. */
    public static double distance(Pt a, Pt b) {
	/*
	 * Pythagoras' theorem:
	 * In a right angle triangle, the squatre of the hypotenuse is equal to
	 * the sum of the squares of the other two sides.
	 *
	 * x2 + y2 = h2
	 */
	double xDist = Math.abs(a.x - b.x);
	double yDist = Math.abs(a.y - b.y);
	return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }

    public static Pt orthoIntersection(Line ln, Pt p) {
        // get relative point values
        double xRelative = p.x - ln.constant().x;
        double yRelative = p.y - ln.constant().y;
        // final x & y values using formula... x = xConst + (xGrad * yRelative)
        double xVal = ln.constant().x + (ln.xGradient() * yRelative);
        double yVal = ln.constant().y + (ln.yGradient() * xRelative);
        return new Pt(xVal, yVal);
    }

    /**
     * Returns the intersection point of two lines.
     */
    public static Pt lineIntersection(Line l1, Line l2) {
	Pt oPt1 = orthoIntersection(l2, l1.constant());
	Pt oPt2 = orthoIntersection(l2, l1.end);
	double yGap1 = oPt1.y - l1.constant().y;
	double yGap2 = oPt2.y - l1.end.y;
	double yDiff = yGap1 - yGap2;
	double xGap1 = oPt1.x - l1.constant().x;
	double xGap2 = oPt2.x - l1.end.x;
	double xDiff = xGap1 - xGap2;
	double yRatio = yGap1 / yDiff;
	double xRatio = xGap1 / xDiff;
	double xVal = l1.constant().x + (l1.xVector() * yRatio);
	double yVal = l1.constant().y + (l1.yVector() * xRatio);
	return new Pt(xVal, yVal);
    }

    /**
     * <p>Calculates distance from the given point to the center of the specified Line.</p>
     *
     * <p>Return value is given as a fraction of the distance from the center point of the
     * line to either end.</p>
     *
     * <p>Distance of either end point to center will return val of 1.0.</p>
     *
     * <p>Exact center pos returns val of 0.0.</p>
     */
    public static double fractionDistFromCenter(Line ln, Pt p) {
        // get Line center
        Pt center = ln.center();
        // get Line distance from center to end
        double halfLength = distance(center, ln.end);
        // get Line distance from center to input point
        double ptLength = distance(center, p);
        // System.out.println("halfLen=" + halfLength + " ptLen=" + ptLength + " midPt=" + center.toString());
        return ptLength / halfLength;
    }

}
