package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncoding;
import info.bstancham.toothpick.TPProgram;
import info.bstancham.toothpick.geom.Geom;
import info.bstancham.toothpick.geom.Node;
import info.bstancham.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class TPLink extends TPPart {

    public static final int STRENGTH_LIGHT = 1;
    public static final int STRENGTH_HEAVY = 5;

    private Node start;
    private Node end;
    private int strength = STRENGTH_LIGHT;
    private ColorGetter colorGet = null;
    private double xTranspose = 0;
    private double yTranspose = 0;

    public TPLink(Node start, Node end) {
        this.start = start;
        this.end = end;
        start.incrementReferenceCount();
        end.incrementReferenceCount();
    }

    @Override
    public TPLink copy() {
        TPLink ln = new TPLink(start.copy(), end.copy());
        ln.strength = strength;
        if (colorGet != null)
            ln.colorGet = colorGet.copy();
        ln.copyPartProperties(this);
        return ln;
    }

    /*---------------------- DISPLAY CO-ORDINATES ----------------------*/

    public double getDisplayXStart() { return getStartNode().getX() + xTranspose; }

    public double getDisplayYStart() { return getStartNode().getY() + yTranspose; }

    public double getDisplayXEnd() { return getEndNode().getX() + xTranspose; }

    public double getDisplayYEnd() { return getEndNode().getY() + yTranspose; }

    public void setXTranspose(double val) { xTranspose = val; }

    public void setYTranspose(double val) { yTranspose = val; }

    /*------------------------- LINE GEOMETRY --------------------------*/

    public Node getStartNode() { return start; }

    public Node getEndNode() { return end; }

    public void setStartNode(Node n) { start = n; }

    public void setEndNode(Node n) { end = n; }

    public boolean isVertical() {
        return start.getX() == end.getX() && start.getY() != end.getY();
    }

    public boolean isHorizontal() {
        return start.getX() != end.getX() && start.getY() == end.getY();
    }

    public Pt getCenterPoint() {
	return new Pt(Geom.midVal(getStartNode().getX(), getEndNode().getX()),
                      Geom.midVal(getStartNode().getY(), getEndNode().getY()));
    }

    public double xConstant() { return start.getX(); }

    public double yConstant() { return start.getY(); }

    /**
     * Vector is directional, and will return a negative value if end point val
     * is lower than start point val.
     */
    public double xVector() { return end.getX() - start.getX(); }

    public double yVector() { return end.getY() - start.getY(); }

    public double xGradient() {
	if (yVector() == 0) { return 0; } // don't want to divide by zero!
	return xVector() / yVector();
    }

    public double yGradient() {
	if (xVector() == 0) { return 0; } // don't want to divide by zero!
	return yVector() / xVector();
    }

    /*------------------------ LINK PROPERTIES -------------------------*/


    public int getStrength() { return strength; }

    /**
     * <p>Use the static line-strength variables.</p>
     */
    public void setStrength(int val) { strength = val; }

    public ColorGetter getColorGetter() { return colorGet; }

    public void setColorGetter(ColorGetter val) { colorGet = val; }

    /*----------------------------- OTHER ------------------------------*/

    public void forceApplied(TPProgram prog, Pt p, TPLink protagonist,
                             TPActor selfActor, TPActor protagonistActor) {
        prog.triggerSfx();

        // this link will die if strength is the same or weaker...
        // ... if protagonist is weaker then it will die instead
        if (getStrength() <= protagonist.getStrength()) {

            die(selfActor, protagonist, protagonistActor, p);
            protagonist.kills(protagonistActor, this, selfActor, p);

            // send messages so that stats can be updated
            selfActor.deathEvent(protagonist, p);
            protagonistActor.killEvent(this, p);

        } else {
            protagonist.die(protagonistActor, this, selfActor, p);
            kills(selfActor, protagonist, protagonistActor, p);
        }

    }

    @Override
    public void die(TPActor selfActor) {
        super.die(selfActor);
        selfActor.getForm().addPart(new TPExplosion(getCenterPoint()));
    }

    @Override
    public void die(TPActor selfActor, TPPart killerPart, TPActor killerActor, Pt p) {
        super.die(selfActor, killerPart, killerActor, p);
        selfActor.getForm().addPart(new TPExplosion(p));
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        return params;
    }

}
