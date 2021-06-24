package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPProgram;
import info.bstancham.toothpick.geom.Node;

/**
 * <p>Anchors actor's position to the specified node as long as that node is alive.</p>
 */
public class PointAnchor implements ActorBehaviour {

    private Node anchor = null;

    /**
     * @param anchor Anchor actor to this node.
     */
    public void setAnchor(Node anchor) { this.anchor = anchor; }

    public void update(TPProgram prog, TPActor a) {
        if (anchor != null) {
            if (anchor.getReferenceCount() <= 0) {
                anchor = null;
            } else {
                a.x = anchor.getX();
                a.y = anchor.getY();
            }
        }
    }

}
