package info.bstancham.toothpick;

import info.bstancham.toothpick.actor.TPActor;
import info.bstancham.toothpick.actor.TPForm;
import info.bstancham.toothpick.actor.TPLink;
import info.bstancham.toothpick.geom.Geom;
import info.bstancham.toothpick.geom.Line;
import info.bstancham.toothpick.geom.Pt;

public class ProgToothpickPhysics implements ProgramBehaviour {

    @Override
    public ProgToothpickPhysics copy() { return this; }

    @Override
    public String getSingletonGroup() {
        return ProgramBehaviour.PHYSICS_MODEL_ID;
    }

    @Override
    public void update(TPProgram prog) {
        // compare each pair or actors only once
        for (int a = 0; a < prog.numActors() - 1; a++)
            for (int b = a + 1; b < prog.numActors(); b++)
                interact(prog, prog.getActor(a), prog.getActor(b));
    }

    protected void interact(TPProgram prog, TPActor actorA, TPActor actorB) {
        TPForm formA = actorA.getForm();
        TPForm formB = actorB.getForm();
        for (int a = 0; a < formA.numLinks(); a++) {
            TPLink linkA = formA.getLink(a);
            if (!linkA.isPassive()) {
                for (int b = 0; b < formB.numLinks(); b++) {
                    TPLink linkB = formB.getLink(b);
                    if (!linkB.isPassive()) {
                        collisionDetection(prog, linkA, linkB, actorA, actorB);
                    }
                }
            }
        }
        // interact children
        for (int n = 0; n < actorA.numChildren(); n++)
            interact(prog, actorB, actorA.getChild(n));
        for (int n = 0; n < actorB.numChildren(); n++)
            interact(prog, actorA, actorB.getChild(n));
    }

    /**
     * <p>Compares two lines for intersection and takes appropriate action. If an
     * intersection is detected then the losing line's forceApplied method is called.</p>
     *
     * @return The result of {@code Geom.lineIntersection(a, b)}.
     */
    private Pt collisionDetection(TPProgram prog, TPLink linkA, TPLink linkB,
                                  TPActor actorA, TPActor actorB) {
        Pt iPt = Geom.lineIntersection(linkA, linkB);
        prog.addIntersectionPoint(iPt);
        double dist1 = Geom.fractionDistFromCenter(linkA, iPt);
        double dist2 = Geom.fractionDistFromCenter(linkB, iPt);

        if (dist1 < 1.0 && dist2 < 1.0) {
            if (dist1 > dist2) {
                // A is the winner!
                linkB.forceApplied(prog, iPt, linkA, actorB, actorA);
            } else {
                // B is the winner!
                linkA.forceApplied(prog, iPt, linkB, actorA, actorB);
            }
        }

        return iPt;
    }

}
