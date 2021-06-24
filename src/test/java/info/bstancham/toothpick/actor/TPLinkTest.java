package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.geom.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class TPLinkTest {

    private double DELTA = 0.00001;

    @Test
    public void testAddNodeReference() {
        Node n1 = new Node(-3, 1);
        Node n2 = new Node(5, 0);
        Node n3 = new Node(6, -2);

        // test that node reference-count starts at zero
        assertEquals(0, n1.getReferenceCount());
        assertEquals(0, n2.getReferenceCount());
        assertEquals(0, n3.getReferenceCount());
            
        TPLink ln1 = new TPLink(n1, n2);
        // test that node reference-count has been incremented
        assertEquals(1, n1.getReferenceCount());
        assertEquals(1, n2.getReferenceCount());
        assertEquals(0, n3.getReferenceCount());

        TPLink ln2 = new TPLink(n2, n3);
        // test that node reference-count has been incremented
        assertEquals(1, n1.getReferenceCount());
        assertEquals(2, n2.getReferenceCount());
        assertEquals(1, n3.getReferenceCount());
    }

}
