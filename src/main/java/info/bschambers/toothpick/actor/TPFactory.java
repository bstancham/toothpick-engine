package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.*;
import java.awt.Color;

/**
 * Static methods for making TPActors.
 */
public final class TPFactory {

    /*---------------------------- players -----------------------------*/

    public static TPPlayer playerLine(Pt pos) {
        return playerLine(70, pos);
    }

    public static TPPlayer playerLine(double length, Pt pos) {
        TPActor actor = new TPActor(singleLineForm(length));
        actor.setPos(pos);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_AT_BOUNDS);
        actor.setColorGetter(new ColorMono(Color.PINK));
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new ThrustInertiaInput());
        return player;
    }

    /*----------------------------- drones -----------------------------*/

    public static TPActor lineActor(TPProgram prog) {
        TPForm form = singleLineForm(randLineLength());
        return droneActor(form, prog);
    }

    public static TPActor regularPolygonActor(TPProgram prog) {
        double size = rand(30, 100);
        int numSides = randInt(3, 8);
        TPForm form = regularPolygonForm(size, numSides);
        return droneActor(form, prog);
    }

    public static TPActor regularThistleActor(TPProgram prog) {
        double size = rand(20, 100);
        int numSides = randInt(3, 16);
        TPForm form = regularThistleForm(size, numSides);
        return droneActor(form, prog);
    }

    public static TPActor segmentedPolygonActor(TPProgram prog) {
        double size = rand(30, 100);
        int numSides = randInt(3, 8);
        TPForm form = segmentedPolygonForm(size, numSides);
        return droneActor(form, prog);
    }

    public static TPActor zigzagActor(TPProgram prog) {
        double width = rand(40, 160);
        double sectionLength = rand(20, 80);
        int numSections = randInt(4, 12);
        TPForm form = zigzagForm(width, sectionLength, numSections);

        TPActor actor = new TPActor(form);
        actor.setColorGetter(randColorGetter());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        // random angle and heading
        actor.angle = Math.random() * Math.PI;
        setRandHeading(actor);
        return actor;
    }

    public static TPActor droneActor(TPForm form, TPProgram prog) {
        TPActor actor = new TPActor(form);
        actor.setColorGetter(randColorGetter());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        setRandHeading(actor);
        actor.angleInertia = randAngleInertia();
        actor.setPos(randBoundaryPos(prog));
        return actor;
    }

    public static TPActor shooterActor(TPProgram prog) {
        TPActor actor = lineActor(prog);
        Spawning spawn = new Spawning();
        spawn.setArchetype(new TPActor(singleLineForm(50)));
        actor.addBehaviour(spawn);
        spawn.setInterval(randInt(10, 500));
        if (Math.random() < 0.5)
            spawn.setRelativeAngle(0.5);
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
        return 20 + (Math.random() * 150);
    }

    public static TPForm regularPolygonForm(double size, int numSides) {
        Pt[] points = regularPolygonPoints(size, numSides);
        TPLine[] lines = new TPLine[points.length];
        for (int i = 0; i < (lines.length - 1); i++)
            lines[i] = new TPLine(new Line(points[i], points[i + 1]));
        lines[lines.length - 1] = new TPLine(new Line(points[points.length - 1], points[0]));
        return new TPForm(lines);
    }

    public static TPForm regularThistleForm(double size, int numSides) {
        Pt zero = new Pt(0.000001, 0.000001);
        Pt[] points = regularPolygonPoints(size, numSides);
        TPLine[] lines = new TPLine[points.length];
        for (int i = 0; i < lines.length; i++)
            lines[i] = new TPLine(new Line(zero, points[i]));
        return new TPForm(lines);
    }

    public static TPForm segmentedPolygonForm(double size, int numSides) {
        Pt zero = new Pt(0.000001, 0.000001);
        Pt[] points = regularPolygonPoints(size, numSides);
        TPLine[] lines = new TPLine[points.length * 2];
        int index = 0;
        // thistle points
        for (int i = 0; i < points.length; i++) {
            lines[index] = new TPLine(new Line(zero, points[i]));
            index++;
        }
        // polygon points
        for (int i = 0; i < (points.length - 1); i++) {
            lines[index] = new TPLine(new Line(points[i], points[i + 1]));
            index++;
        }
        lines[lines.length - 1] = new TPLine(new Line(points[points.length - 1], points[0]));

        return new TPForm(lines);
    }

    public static Pt[] regularPolygonPoints(double size, int numSides) {
        Pt[] points = new Pt[numSides];
        for (int i = 0; i < points.length; i++) {
            double amt = (double) i / (double) numSides;
            double angle = amt * (2 * Math.PI);
            points[i] = new Pt(Math.sin(angle) * size,
                               Math.cos(angle) * size);
        }
        return points;
    }

    public static TPForm zigzagForm(double width, double sectionLength, int numSections) {
        Pt[] points = zigzagPoints(width, sectionLength, numSections);
        TPLine[] lines = new TPLine[points.length - 1];
        for (int i = 0; i < (lines.length); i++)
            lines[i] = new TPLine(new Line(points[i], points[i + 1]));
        // lines[lines.length - 1] = new TPLine(new Line(points[points.length - 1], points[0]));
        return new TPForm(lines);
    }

    public static Pt[] zigzagPoints(double width, double sectionLength, int numSections) {
        double totalLength = numSections * sectionLength;
        double x = -(totalLength / 2);
        double y = width / 2;
        Pt[] points = new Pt[numSections + 1];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Pt(x, y);
            x += sectionLength;
            y = -y;
        }
        return points;
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
        return randAngleInertia(0.007);
    }

    public static double randAngleInertia(double max) {
        return Math.random() * max;
    }

    public static Pt randBoundaryPos(TPProgram prog) {
        return randBoundaryPos(prog.getGeometry().getWidth(),
                               prog.getGeometry().getHeight());
    }

    public static Pt randBoundaryPos(int width, int height) {
        if (Math.random() < 0.5) {
            // horizontal edges
            return new Pt(rand(0, width),
                          (Math.random() < 0.5 ? 0 : height));
        } else {
            // vertical edges
            return new Pt((Math.random() < 0.5 ? 0 : width),
                          rand(0, height));
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

    private static int randInt(int max) {
        return (int) (Math.random() * max);
    }

    private static int randInt(int min, int max) {
        double dist = max - min;
        return (int) (min + (Math.random() * dist));
    }

}
