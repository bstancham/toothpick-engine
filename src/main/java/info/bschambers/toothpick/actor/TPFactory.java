package info.bschambers.toothpick.game;

import info.bschambers.toothpick.actor.*;
import info.bschambers.toothpick.geom.*;

/**
 * Static methods for making TPActors.
 */
public final class TPFactory {

    /*---------------------------- players -----------------------------*/

    public static TPActor playerLine(double length) {
        return new TPActor(singleLineForm(length), new ThrustInertiaController());
    }

    /*----------------------------- drones -----------------------------*/

    public static TPActor randSingleLineEdgePos(Rect bounds) {
        TPForm form = singleLineForm(randLineLength());
        TPController ctrl = new SimpleController(randHeading(), randSpin());
        ctrl.setPos(randBoundaryPos(bounds));
        return new TPActor(form, ctrl);
    }

    /*--------------------------- line-forms ---------------------------*/

    public static LinesForm singleLineForm(double length) {
        double half = length / 2;
        Pt start = new Pt(-half, 0);
        Pt end = new Pt(half, 0);
        return new LinesForm(new TPLine(new Line(start, end)));
    }

    public static double randLineLength() {
        return 20 + (Math.random() * 200);
    }

    /*-------------------------- controllers ---------------------------*/

    public static Pt randHeading() {
        return randHeading(1, 7);
    }

    public static Pt randHeading(double min, double max) {
        double magnitude = rand(min, max);
        double angle = rand(Math.PI * 2);
        return new Pt(Math.sin(angle) * magnitude,
                      Math.cos(angle) * magnitude);
    }

    public static double randSpin() {
        return randSpin(0.1);
    }

    public static double randSpin(double max) {
        return Math.random() * max;
    }

    public static Pt randBoundaryPos(Rect bounds) {
        if (Math.random() < 0.5) {
            // horizontal edges
            return new Pt(rand(bounds.x1, bounds.x2),
                          (Math.random() < 0.5 ? bounds.y1 : bounds.y2));
        } else {
            // vertical edges
            return new Pt((Math.random() < 0.5 ? bounds.x1 : bounds.x2),
                          rand(bounds.y1, bounds.y2));
        }
    }

    /*------------------------- helper methods -------------------------*/

    private static double rand(double max) {
        return Math.random() * max;
    }

    private static double rand(double min, double max) {
        double dist = max - min;
        return min + (Math.random() * dist);
    }

}
