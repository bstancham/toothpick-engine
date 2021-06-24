package info.bschambers.toothpick.geom;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private double DELTA = 0.00001;

    @Test
    public void testRotate() {
        Node n = new Node(1, 3);
        // quarter turn
        n.update(Math.PI * 0.5, 0, 0);
        assertEquals(-3, n.getX(), DELTA);
        assertEquals(1, n.getY(), DELTA);
        // half turn
        n.update(Math.PI, 0, 0);
        assertEquals(-1, n.getX(), DELTA);
        assertEquals(-3, n.getY(), DELTA);
        // three quarter turn
        n.update(Math.PI * 1.5, 0, 0);
        assertEquals(3, n.getX(), DELTA);
        assertEquals(-1, n.getY(), DELTA);
        // full turn
        n.update(Math.PI * 2, 0, 0);
        assertEquals(1, n.getX(), DELTA);
        assertEquals(3, n.getY(), DELTA);
    }

    @Test
    public void testTranslate() {
        Node n = new Node(1, 3);
        // just X dimension
        n.update(0, 10, 0);
        assertEquals(11, n.getX(), DELTA);
        assertEquals(3, n.getY(), DELTA);
        // X and Y
        n.update(0, 12, -4);
        assertEquals(13, n.getX(), DELTA);
        assertEquals(-1, n.getY(), DELTA);
        // X and Y
        n.update(0, -7, 102);
        assertEquals(-6, n.getX(), DELTA);
        assertEquals(105, n.getY(), DELTA);
    }

    @Test
    public void testRotateAndTranslate() {
        Node n = new Node(1, 3);

        n.update(Math.PI * 0.5, 5, -20);
        assertEquals(2, n.getX(), DELTA);
        assertEquals(-19, n.getY(), DELTA);

        n.update(Math.PI * 1.5, -12, 2036);
        assertEquals(-9, n.getX(), DELTA);
        assertEquals(2035, n.getY(), DELTA);
    }

}
