package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Node;
import java.util.ArrayList;
import java.util.List;

public class PathAnchor implements ActorBehaviour {

    private List<TPLink> path = new ArrayList<>();
    private int currentIndex = 0;
    private double currentDist = 0;
    private double distStep = 0.3;
    private boolean loop = true;
    private boolean reverse = false;

    public void addToPath(TPLink pathPart) {
        path.add(pathPart);
    }

    /**
     * <p>Makes path by adding each line of actor's form in the same order in which they
     * appear in the form.</p>
     */
    public void makePath(TPActor actor) {
        path = new ArrayList<TPLink>();
        for (int i = 0; i < actor.getForm().numLinks(); i++)
            addToPath(actor.getForm().getLink(i));
        currentIndex = 0;
    }

    public void update(TPProgram prog, TPActor a) {

        if (path.size() > 0) {
            TPLink currentLink = path.get(currentIndex);
            if (currentLink.isAlive()) {

                // increment distance
                currentDist += distStep;
                Node start = getStart(currentLink);
                Node end = getEnd(currentLink);
                double lineDist = Geom.distance(start, end);

                // if end of line is passed, go to next line
                if (currentDist > lineDist) {
                    currentDist -= lineDist;
                    endOfLine();
                    currentLink = path.get(currentIndex);
                    start = getStart(currentLink);
                    end = getEnd(currentLink);
                }

                // calculate current position
                double angle = Geom.angle(start.getX(), start.getY(), end.getX(), end.getY());
                double x = start.getX() + (currentDist * (Math.cos(angle)));
                double y = start.getY() + (currentDist * (Math.sin(angle)));
                a.x = x;
                a.y = y;
            }
        }
    }

    private Node getStart(TPLink ln) {
        if (reverse)
            return ln.getEndNode();
        return ln.getStartNode();
    }

    private Node getEnd(TPLink ln) {
        if (reverse)
            return ln.getStartNode();
        return ln.getEndNode();
    }

    private void endOfLine() {
        incrementLineIndex();
        if (!path.get(currentIndex).isAlive()) {
            reverse = !reverse;
            incrementLineIndex();
        }
    }

    private void incrementLineIndex() {
        if (reverse) {
            currentIndex--;
            if (currentIndex < 0)
                currentIndex = path.size() - 1;
        } else {
            currentIndex++;
            if (currentIndex >= path.size())
                currentIndex = 0;
        }
    }

}
