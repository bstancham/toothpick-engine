package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPGeometry;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Node;
import info.bschambers.toothpick.geom.Pt;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>To build a form, simply use {@link addLink}. The nodes will be added automatically,
 * unless they have already been added.</p>
 */
public class TPForm implements TPEncodingHelper {

    private static final double NODE_COMPARISON_DELTA = 0.00000001;

    private TPActor actor = null;
    protected boolean alive = true;
    public Rectangle bounds = new Rectangle();
    private List<TPPart> parts = new ArrayList<>();
    private List<TPPart> partsToAdd = new ArrayList<>();
    private List<TPPart> partsToRemove = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Node> nodesToRemove = new ArrayList<>();
    private List<TPLink> links = new ArrayList<>();

    public TPForm() {}

    public TPForm copy() {
        TPForm form = new TPForm();
        for (TPPart p : parts) {
            if (p instanceof TPLink) {
                form.addLinkReuseNodes((TPLink) p.copy());
                form.housekeeping();
            } else {
                form.addPart(p.copy());
            }
        }
        form.housekeeping();
        form.actor = actor;
        form.alive = alive;
        return form;
    }

    public TPActor getActor() { return actor; }

    public void setActor(TPActor a) { actor = a; }

    public boolean isAlive() { return alive; }

    public void setAlive(boolean val) { alive = val; }

    public int numParts() { return parts.size(); }

    public TPPart getPart(int index) { return parts.get(index); }

    /**
     * Schedule part to be added on next update.
     */
    public void addPart(TPPart p) { partsToAdd.add(p); }

    /**
     * Schedule part to be removed on next update.
     */
    public void removePart(TPPart p) { partsToRemove.add(p); }

    public int numLinks() { return links.size(); }

    public TPLink getLink(int i) { return links.get(i); }

    /**
     * <p>Add link to form, but first check whether either node is the same as an existing
     * node and substitute if so.</p>
     *
     * <p>NOTE: if adding multiples links, must call {@link housekeeping} after each call
     * to this method, otherwise new nodes will not have been added yet for subsequent
     * calls.</p>
     */
    public void addLinkReuseNodes(TPLink ln) {
        ln.setStartNode(reuseNode(ln.getStartNode(), NODE_COMPARISON_DELTA));
        ln.setEndNode(reuseNode(ln.getEndNode(), NODE_COMPARISON_DELTA));
        addPart(ln);
    }

    /**
     * <p>Add a new link to form, but first check whether either node is the same as an
     * existing node and substitute if so.</p>
     *
     * <p>NOTE: if adding multiples links, must call {@link housekeeping} after each call
     * to this method, otherwise new nodes will not have been added yet for subsequent
     * calls.</p>
     */
    public void addLinkReuseNodes(double x1, double y1, double x2, double y2) {
        addPart(new TPLink(reuseNode(x1, y1, NODE_COMPARISON_DELTA),
                           reuseNode(x2, y2, NODE_COMPARISON_DELTA)));
    }

    /**
     * <p>if input node matches an existing node in form, return that node, else return
     * the input node. </p>
     *
     * @param inputNode The input node.
     * @param delta Allowed deviation from given co-ordinates.
     */
    private Node reuseNode(Node inputNode, double delta) {
        for (Node n : nodes) {
            if (equals(inputNode.getX(), n.getX(), delta) &&
                equals(inputNode.getY(), n.getY(), delta)) {
                System.out.println("TPForm.reuseNode: substituting node " + n);
                return n;
            }
        }
        return inputNode;
    }

    /**
     * <p>if input co-ordinates match co-ordinates of an existing node, return that node,
     * else return a new node. </p>
     *
     * @param x X co-ordinate of the new node.
     * @param y Y co-ordinate of the new node.
     * @param delta Allowed deviation from given co-ordinates.
     */
    private Node reuseNode(double x, double y, double delta) {
        for (Node n : nodes) {
            if (equals(x, n.getX(), delta) &&
                equals(y, n.getY(), delta)) {
                System.out.println("TPForm.reuseNode: substituting node " + n);
                return n;
            }
        }
        return new Node(x, y);
    }

    private boolean equals(double a, double b, double delta) {
        if (a < b - delta) return false;
        if (a > b + delta) return false;
        return true;
    }

    private void addNode(Node n) {
        if (!nodes.contains(n))
            nodes.add(n);
    }

    public int numNodes() { return nodes.size(); }

    public Node getNode(int i) { return nodes.get(i); }

    public Rectangle getBoundingBox() { return bounds; }

    public void housekeeping() {

        // REMOVE DEAD PARTS (INC LINKS)
        for (TPPart p : parts) {
            if (!p.isAlive()) {
                partsToRemove.add(p);
            }
        }
        for (TPPart p : partsToRemove) {
            parts.remove(p);
            if (p instanceof TPLink) {
                TPLink ln = (TPLink) p;
                links.remove(ln);
                ln.getStartNode().decrementReferenceCount();
                ln.getEndNode().decrementReferenceCount();
            }
        }
        partsToRemove.clear();
        // REMOVE DEAD NODES
        for (Node n : nodes)
            if (n.getReferenceCount() <= 0)
                nodesToRemove.add(n);
        for (Node n : nodesToRemove) {
            nodes.remove(n);
        }
        nodesToRemove.clear();

        // ADD NEW PARTS (INC LINKS)
        for (TPPart p : partsToAdd) {
            parts.add(p);
            if (p instanceof TPLink) {
                TPLink ln = (TPLink) p;
                links.add(ln);
                addNode(ln.getStartNode());
                addNode(ln.getEndNode());
            }
        }
        partsToAdd.clear();

        // is form dead?
        if (parts.size() <= 0)
            alive = false;
    }

    /**
     * <p>Does housekeeping, updates the form for the actor position and rotation, updates
     * the bounding box.</p>
     */
    public void update(TPActor a) {
        housekeeping();

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        boolean first = true;

        for (TPPart p : parts)
            p.update(a);

        // update nodes (and find bounds)
        for (Node n : nodes) {
            n.update(a.angle, a.x, a.y);
            if (first) {
                x1 = (int) n.getX();
                y1 = (int) n.getY();
                x2 = (int) n.getX();
                y2 = (int) n.getY();
                first = false;
            } else {
                if (n.getX() < x1) x1 = (int) n.getX();
                if (n.getY() < y1) y1 = (int) n.getY();
                if (n.getX() > x2) x2 = (int) n.getX();
                if (n.getY() > y2) y2 = (int) n.getY();
            }
        }

        bounds.setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * <p>For any line outside of bounds, wrap around to the other side.</p>
     */
    public void wrapAtBounds(TPGeometry geom) {
        for (TPLink ln : links) {
            Pt c = ln.getCenterPoint();
            // wrap horizontal?
            if (c.x < 0)
                ln.setXTranspose(geom.getWidth());
            else if (c.x > geom.getWidth())
                ln.setXTranspose(-geom.getWidth());
            else
                ln.setXTranspose(0);
            // wrap vertical?
            if (c.y < 0)
                ln.setYTranspose(geom.getHeight());
            else if (c.y > geom.getHeight())
                ln.setYTranspose(-geom.getHeight());
            else
                ln.setYTranspose(0);
        }
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addListMethod(TPPart.class, parts, "addPart");
        return params;
    }

}
