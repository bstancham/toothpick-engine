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
