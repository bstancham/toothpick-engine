package info.bschambers.toothpick.geom;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeomTest {

    private double DELTA = 0.00001;

    @Test
    public void testMidPoint() {
        Pt a = new Pt(4, 3);
        Pt b = new Pt(14, 7);
        assertEquals(new Pt(9, 5), Geom.midPoint(a, b));
    }

    @Test
    public void testAngle() {
        Pt start = new Pt(36.1, -3.5);

        // right
        Pt a = new Pt(149.4, -3.5);
        assertEquals(0.0, Geom.angle(start, a), DELTA);

        // diagonal down-right
        Pt b = new Pt(68.1, 28.5);
        assertEquals(Math.PI / 4, Geom.angle(start, b), DELTA);

        // down
        Pt c = new Pt(36.1, 110);
        assertEquals(Math.PI / 2, Geom.angle(start, c), DELTA);

        // diagonal down-left
        Pt d = new Pt(21.9, 10.7);
        assertEquals(Math.PI * 0.75, Geom.angle(start, d), DELTA);

        // left
        Pt e = new Pt(21.9, -3.5);
        assertEquals(Math.PI, Geom.angle(start, e), DELTA);

        // diagonal up-left
        Pt f = new Pt(28.4, -11.2);
        assertEquals(Math.PI * 1.25, Geom.angle(start, f), DELTA);

        // up
        Pt g = new Pt(36.1, -9.0);
        assertEquals(Math.PI * 1.5, Geom.angle(start, g), DELTA);

        // diagonal up-right
        Pt h = new Pt(48.1, -15.5);
        assertEquals(Math.PI * 1.75, Geom.angle(start, h), DELTA);
    }

    @Test
    public void testFractionDistFromCenter() {
        Pt a = new Pt(4, 3);
        Pt b = new Pt(14, 7);
        Line ln = new Line(a, b);
        Pt center = ln.center(); // should be (9, 5)
        assertEquals(0, Geom.fractionDistFromCenter(ln, center), DELTA);
        assertEquals(1, Geom.fractionDistFromCenter(ln, b), DELTA);
        assertEquals(2, Geom.fractionDistFromCenter(ln, new Pt(19, 9)), DELTA);
        assertEquals(3, Geom.fractionDistFromCenter(ln, new Pt(24, 11)), DELTA);
        assertEquals(3.5, Geom.fractionDistFromCenter(ln, new Pt(26.5, 12)), DELTA);
    }

}
