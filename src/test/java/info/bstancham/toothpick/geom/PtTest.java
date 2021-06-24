package info.bstancham.toothpick.geom;

import org.junit.Test;
import static org.junit.Assert.*;

public class PtTest {

    private Pt p1 = new Pt(3.0, 2.4);

    @Test
    public void testEquals() {
        Pt a = new Pt(4.1, 27.6);
        Pt b = new Pt(4.1, 27.6);
        Pt c = new Pt(-12, -3.9);
        assertEquals(a, b);
    }

}
