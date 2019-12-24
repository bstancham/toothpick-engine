package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.geom.*;
import java.awt.Color;

/**
 * Static methods for making TPActors.
 */
public final class TPFactory {

    public static final WrapAtBounds WRAP_AT_BOUNDS = new WrapAtBounds();

    /*---------------------------- players -----------------------------*/

    public static TPPlayer playerLine(Pt pos) {
        return playerLine(70, pos);
    }

    public static TPPlayer playerLine(double length, Pt pos) {
        TPActor actor = new TPActor(singleLineForm(length));
        actor.setPos(pos);
        actor.addBehaviour(WRAP_AT_BOUNDS);
        actor.setColorGetter(randColorGetter());
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new ThrustInertiaInput());
        return player;
    }

    /*----------------------------- drones -----------------------------*/

    public static TPActor randSingleLineEdgePos(Rect bounds) {
        TPForm form = singleLineForm(randLineLength());
        TPActor actor = new TPActor(form);
        actor.setColorGetter(randColorGetter());
        actor.addBehaviour(WRAP_AT_BOUNDS);
        setRandHeading(actor);
        actor.angleInertia = randAngleInertia();
        actor.setPos(randBoundaryPos(bounds));
        return actor;
    }

    /*------------------------------ form ------------------------------*/

    public static TPForm singleLineForm(double length) {
        double half = length / 2;
        Pt start = new Pt(-half, 0);
        Pt end = new Pt(half, 0);
        TPForm form = new TPForm(new TPPart[] { new TPLine(new Line(start, end)) });
        return form;
    }

    public static double randLineLength() {
        return 20 + (Math.random() * 200);
    }

    /*----------------------------- color ------------------------------*/

    public static ColorGetter randColorGetter() {
        ColorGetter[] items = new ColorGetter[] {
            new ColorMono(),
            new ColorRandom(),
            new ColorRandomMono(),
            new ColorSmoothMono(),
            new ColorSmoothColor(),
        };
        int index = (int) (Math.random() * items.length);
        return items[index];
    }

    /*-------------------------- controllers ---------------------------*/

    public static void setRandHeading(TPActor actor) {
        setRandHeading(actor, 0.02, 1);
    }

    public static void setRandHeading(TPActor actor, double min, double max) {
        double magnitude = rand(min, max);
        double angle = rand(Math.PI * 2);
        actor.xInertia = Math.sin(angle) * magnitude;
        actor.yInertia = Math.cos(angle) * magnitude;
    }

    public static double randAngleInertia() {
        return randAngleInertia(0.01);
    }

    public static double randAngleInertia(double max) {
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
