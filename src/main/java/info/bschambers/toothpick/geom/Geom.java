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
    public static int midVal(int a, int b) {
        int difference = Math.abs(a - b);
        return Math.min(a, a) + (difference / 2);
    }

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

    public static double angle(Line ln) {
        return angle(ln.start, ln.end);
    }

    /**
     * <p>Calculates the angle in radians of the line from point a to point b.</p>
     *
     * <p>Angles are measured in a clockwise direction:</p>
     *
     * <ul>
     * <li>Horizontal Line pointing to the right is 0.0.</li>
     * <li>Vertical line pointing down is Math.PI / 2.</li>
     * <li>Horizontal Line pointing to the left is Math.PI.</li>
     * <li>Vertical line pointing up is Math.PI * 1.5.</li>
     * </ul>
     */
    public static double angle(Pt a, Pt b) {
	// get x & y lengths
	double xLen = b.x - a.x;
	double yLen = b.y - a.y;
	// length of hypotenuse (Pythagoras' theroem ---> a2 + b2 = c2)
	double hLen = Math.sqrt((xLen * xLen) + (yLen * yLen));
	// get angle (compensate for negative y-direction)
	if (yLen < 0) hLen = -hLen;
	double angle = Math.acos(xLen / hLen);
        if (yLen < 0) angle += Math.PI;
        return angle;
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

    public static Pt rotate(Pt p, double amount, Pt center) {
	// get x & y lengths
	double xLen = p.x - center.x;
	double yLen = p.y - center.y;
	// get h length (hypotenuse)
	// Pythagoras' theroem ---> a2 + b2 = c2
	double hLen = Math.sqrt((xLen * xLen) + (yLen * yLen));
	// negative y direction compensation!!!!
	if (yLen < 0)
            hLen = -hLen;
	// get angle and increment
	double angle = Math.acos(xLen / hLen);
	angle += Math.PI * amount;
	// calculate new position
	double x = center.x + (hLen * (Math.cos(angle)));
	double y = center.y + (hLen * (Math.sin(angle)));
        return new Pt(x, y);
    }

    public static Line rotate(Line ln, double amount, Pt center) {
        return new Line(rotate(ln.start, amount, center),
                        rotate(ln.end, amount, center));
    }

    /**
     * Calculates the angle going clockwise starting from the line heading
     * vertically upwards from the 'home' point.
     */
    public static double angle(double x1, double y1, double x2, double y2) {
	// get x & y lengths
	double xLen = x2 - x1;
        double yLen = y2 - y1;
	// get hypotenuse length: Pythagoras' theroem ---> a2 + b2 = c2
	double hLen = Math.sqrt((xLen * xLen) + (yLen * yLen));
	// negative y direction compensation!
	if (yLen < 0)
            hLen = -hLen;
        // calculate angle
	double angle = Math.acos(xLen / hLen);
	if (yLen < 0)
            angle += Math.PI;
	return angle;
    }

}
