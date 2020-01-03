package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPForm;
import info.bschambers.toothpick.actor.TPLine;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;

public class ToothpickPhysics implements ProgramBehaviour {

    @Override
    public void update(TPProgram prog) {
        // compare each pair or actors only once
        for (int a = 0; a < prog.numActors() - 1; a++)
            for (int b = a + 1; b < prog.numActors(); b++)
                interact(prog, prog.getActor(a), prog.getActor(b));
    }

    protected void interact(TPProgram prog, TPActor a, TPActor b) {
        // interact forms
	interact(prog, a.getForm(), b.getForm());
        // interact children
        for (int n = 0; n < a.numChildren(); n++)
            interact(prog, b, a.getChild(n));
        for (int n = 0; n < b.numChildren(); n++)
            interact(prog, a, b.getChild(n));
    }

    private void interact(TPProgram prog, TPForm formA, TPForm formB) {
        // Compare every line of form A with every line of form B.
        for (int a = 0; a < formA.numParts(); a++) {
            if (formA.getPart(a) instanceof TPLine) {
                TPLine lineA = (TPLine) formA.getPart(a);
                for (int b = 0; b < formB.numParts(); b++) {
                    if (formB.getPart(b) instanceof TPLine) {
                        collisionDetection(prog, lineA, (TPLine) formB.getPart(b));
                    }
                }
            }
        }
    }

    /**
     * <p>Compares two lines for intersection and takes appropriate action. If an
     * intersection is detected then the losing line's forceApplied method is called.</p>
     *
     * @return The result of {@code Geom.lineIntersection(a, b)}.
     */
    private Pt collisionDetection(TPProgram prog, TPLine a, TPLine b) {
        Line ln1 = a.getLine();
        Line ln2 = b.getLine();
        Pt iPt = Geom.lineIntersection(ln1, ln2);
        prog.addIntersectionPoint(iPt);
        double dist1 = Geom.fractionDistFromCenter(ln1, iPt);
        double dist2 = Geom.fractionDistFromCenter(ln2, iPt);
        if (dist1 < 1.0 && dist2 < 1.0) {
            if (dist1 > dist2) {
                // line A is the winner!
                b.forceApplied(iPt, a);
            } else {
                // line B is the winner!
                a.forceApplied(iPt, b);
            }
        }
        return iPt;
    }

}
