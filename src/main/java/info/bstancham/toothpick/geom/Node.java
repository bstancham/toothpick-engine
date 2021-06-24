package info.bstancham.toothpick.geom;

public class Node {

    // the node archetype
    private double xArch;
    private double yArch;
    // the current node co-ordinates
    private double x;
    private double y;

    private int referenceCount = 0;

    public Node(double x, double y) {
        xArch = x;
        yArch = y;
        this.x = x;
        this.y = y;
    }

    public Node copy() {
        Node n = new Node(xArch, yArch);
        n.x = x;
        n.y = y;
        return n;
    }

    /**
     * <p>Updates rotation and translation of node. Makes new X and Y co-ordinates by
     * rotating then translating the node archetype. The center of rotation is the origin
     * point (0, 0).</p>
     *
     * @param xTranslate Translate the node archetype by this much in the x dimension.
     * @param yTranslate Translate the node archetype by this much in the y dimension.
     * @param rotate Rotate the node archetype by this amount in radians.
     */
    public void update(double rotate, double xTranslate, double yTranslate) {
        
        if (rotate != 0) {
            // ROTATE
            // get hypotenuse length
            // Pythagoras' theroem ---> (a * a) + (b * b) = (h * h)
            double hLen = Math.sqrt((xArch * xArch) + (yArch * yArch));
            // negative y direction compensation!!!!
            if (yArch < 0)
                hLen = -hLen;
            // get angle and increment
            double angle = Math.acos(xArch / hLen);
            angle += rotate;
            // calculate new position
            x = hLen * Math.cos(angle);
            y = hLen * Math.sin(angle);
        } else {
            x = xArch;
            y = yArch;
        }

        // TRANSLATE
        x = x + xTranslate;
        y = y + yTranslate;
    }

    public double getXArchetype() { return xArch; }
    
    public double getYArchetype() { return yArch; }
    
    public double getX() { return x; }
    
    public double getY() { return y; }

    /**
     * Set archetype to current X/Y values.
     */
    public void resetArchetype() {
        xArch = x;
        yArch = y;
    }

    public void setArchetype(double x, double y) {
        xArch = x;
        yArch = y;
    }

    public int getReferenceCount() { return referenceCount; }

    public void incrementReferenceCount() { referenceCount++; }

    public void decrementReferenceCount() { referenceCount--; }

}
