package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.geom.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class TPActorTest {

    private double DELTA = 0.00001;

    @Test
    public void testMoveAndRotateNodes() {

        Node n1 = new Node(1, 3);
        Node n2 = new Node(4, -2);
        Node n3 = new Node(4, -5);

        TPLink ln1 = new TPLink(n1, n2);
        TPLink ln2 = new TPLink(n2, n3);

        TPForm form = new TPForm();
        form.addPart(ln1);
        form.addPart(ln2);

        TPActor a = new TPActor(form);
        a.updateForm();

        assertEquals(1, a.getForm().getLink(0).getStartNode().getX(), DELTA);
        assertEquals(3, a.getForm().getLink(0).getStartNode().getY(), DELTA);
        assertEquals(4, a.getForm().getLink(0).getEndNode().getX(), DELTA);
        assertEquals(-2, a.getForm().getLink(0).getEndNode().getY(), DELTA);

        // move actor
        a.x = 6.5;
        a.y = -4;
        a.updateForm();

        // test that Link instances are still the same
        assertEquals(ln1, a.getForm().getLink(0));
        assertEquals(ln2, a.getForm().getLink(1));

        // test that nodes have been moved correctly
        assertEquals(7.5,  a.getForm().getLink(0).getStartNode().getX(), DELTA);
        assertEquals(-1,   a.getForm().getLink(0).getStartNode().getY(), DELTA);
        assertEquals(10.5, a.getForm().getLink(0).getEndNode().getX(), DELTA);
        assertEquals(-6, a.getForm().getLink(0).getEndNode().getY(), DELTA);

    }

}
