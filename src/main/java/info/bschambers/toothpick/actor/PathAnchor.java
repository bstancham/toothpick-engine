package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Geom;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import java.util.ArrayList;
import java.util.List;

public class PathAnchor implements ActorBehaviour {

    private List<TPLine> path = new ArrayList<>();
    private int currentIndex = 0;
    private double currentDist = 0;
    private double distStep = 0.3;
    private boolean loop = true;
    private boolean reverse = false;

    public void addToPath(TPLine pathPart) {
        path.add(pathPart);
    }

    /**
     * <p>Makes path by adding each line of actor's form in the same order in which they
     * appear in the form.</p>
     */
    public void makePath(TPActor actor) {
        path = new ArrayList<TPLine>();
        for (int i = 0; i < actor.getForm().numParts(); i++)
            if (actor.getForm().getPart(i) instanceof TPLine)
                addToPath((TPLine) actor.getForm().getPart(i));
        currentIndex = 0;
    }

    public void update(TPProgram prog, TPActor a) {

        if (path.size() > 0) {
            TPLine currentLine = path.get(currentIndex);
            if (currentLine.isAlive()) {

                // increment distance
                currentDist += distStep;
                Pt start = getStart(currentLine);
                Pt end = getEnd(currentLine);
                double lineDist = Geom.distance(start, end);

                // if end of line is passed, go to next line
                if (currentDist > lineDist) {
                    currentDist -= lineDist;
                    endOfLine();
                    currentLine = path.get(currentIndex);
                    start = getStart(currentLine);
                    end = getEnd(currentLine);
                }

                // calculate current position
                double angle = Geom.angle(start, end);
                double x = start.x + (currentDist * (Math.cos(angle)));
                double y = start.y + (currentDist * (Math.sin(angle)));
                a.x = x;
                a.y = y;
            }
        }
    }

    private Pt getStart(TPLine tpl) {
        if (reverse)
            return tpl.getLine().end;
        return tpl.getLine().start;
    }

    private Pt getEnd(TPLine tpl) {
        if (reverse)
            return tpl.getLine().start;
        return tpl.getLine().end;
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
