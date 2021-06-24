package info.bstancham.toothpick;

import info.bstancham.toothpick.actor.TPActor;
import info.bstancham.toothpick.actor.TPFactory;
import info.bstancham.toothpick.geom.Pt;
import org.junit.Test;
import static org.junit.Assert.*;

public class TPProgramTest {

    private double DELTA = 0.00001;
    private TPProgram prog = new TPProgram("TEST PROGRAM");
    private TPActor a;
    private TPActor b;
    private TPActor c;

    public TPProgramTest() {
        a = TPFactory.lineActor(prog);
        b = TPFactory.lineActor(prog);
        c = TPFactory.lineActor(prog);
        prog.addActor(a);
        prog.addActor(b);
        prog.addActor(c);
        prog.update();
    }

    @Test
    public void testGetNearest() {
        prog.setPlayArea(600, 300);
        a.setPos(new Pt(10, 80));
        b.setPos(new Pt(200, 80));
        c.setPos(new Pt(100, 110));

        assertEquals(c, prog.getNearest(a, true));

        // wrapped distance is shorter
        b.setPos(new Pt(570, 70));
        assertEquals(b, prog.getNearest(a, true));

        // return null if input actor is not on program's actor-list
        TPActor d = TPFactory.lineActor(prog);
        assertNull(prog.getNearest(d, true));

        // return null if there are no other actors
        prog.removeActor(b);
        prog.removeActor(c);
        prog.update();
        assertNull(prog.getNearest(a, true));
    }

    @Test
    public void testGetDistance() {
        prog.setPlayArea(600, 300);

        a.setPos(new Pt(10, 80));
        b.setPos(new Pt(200, 80));
        assertEquals(190, prog.getDistance(a, b, true), DELTA);
        assertEquals(190, prog.getDistance(b, a, true), DELTA);

        // wrapped distance is shorter
        b.setPos(new Pt(570, 80));
        assertEquals(40, prog.getDistance(a, b, true), DELTA);
        assertEquals(40, prog.getDistance(b, a, true), DELTA);
    }

}
