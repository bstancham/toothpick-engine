package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class TPFormTest {

    private double DELTA = 0.00001;

    @Test
    public void testCopy() {

        // make original form
        Node n1 = new Node(1, 3);
        Node n2 = new Node(4, -2);
        Node n3 = new Node(4, -5);
        Node n4 = new Node(1, 5);
        TPLink ln1 = new TPLink(n1, n2);
        TPLink ln2 = new TPLink(n2, n3);
        TPLink ln3 = new TPLink(n2, n4);
        TPForm form = new TPForm();
        form.addPart(ln1);
        form.addPart(ln2);
        form.addPart(ln3);
        form.housekeeping();

        assertEquals(4, form.numNodes());
        assertEquals(3, form.numLinks());

        // make copy of form
        TPForm formB = form.copy();

        assertEquals(4, formB.numNodes());
        assertEquals(3, formB.numLinks());

        TPLink lnB1 = formB.getLink(0);
        TPLink lnB2 = formB.getLink(1);
        TPLink lnB3 = formB.getLink(2);

        assertNotEquals(ln1, lnB1);
        assertNotEquals(ln2, lnB2);
        assertNotEquals(ln3, lnB3);

        // test that node #2 has been re-used
        
        assertEquals(lnB1.getEndNode(), lnB2.getStartNode());
        assertEquals(lnB1.getEndNode(), lnB3.getStartNode());

        // test that copied node co-ordinates match original

        assertEquals(1, lnB1.getStartNode().getX(), DELTA);
        assertEquals(3, lnB1.getStartNode().getY(), DELTA);
        assertEquals(4, lnB1.getEndNode().getX(), DELTA);
        assertEquals(-2, lnB1.getEndNode().getY(), DELTA);
        assertEquals(4, lnB2.getStartNode().getX(), DELTA);
        assertEquals(-2, lnB2.getStartNode().getY(), DELTA);
        assertEquals(4, lnB2.getEndNode().getX(), DELTA);
        assertEquals(-5, lnB2.getEndNode().getY(), DELTA);
        assertEquals(4, lnB3.getStartNode().getX(), DELTA);
        assertEquals(-2, lnB3.getStartNode().getY(), DELTA);
        assertEquals(1, lnB3.getEndNode().getX(), DELTA);
        assertEquals(5, lnB3.getEndNode().getY(), DELTA);
    }

}
