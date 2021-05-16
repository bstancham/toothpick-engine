package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.*;
import java.awt.Color;

/**
 * Static methods for making TPActors.
 */
public final class TPFactory {
    
    public static final double NEARLY_ZERO = 0.00000001;

    /*---------------------------- players -----------------------------*/

    public static TPPlayer playerLine(Pt pos) {
        return playerLine(70, pos);
    }

    public static TPPlayer playerLine(double length, Pt pos) {
        TPActor actor = new TPActor(singleLineForm(length));
        actor.name = "player";
        actor.setPos(pos);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_AT_BOUNDS);
        actor.setColorGetter(new ColorMono(Color.PINK));
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new ThrustInertiaInput());
        return player;
    }

    public static TPPlayer player(TPActor actor) {
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new ThrustInertiaInput());
        return player;
    }

    /*----------------------------- drones -----------------------------*/

    /**
     * Creates a new {@code TPActor} with single-line form of random length.
     */
    public static TPActor lineActor(TPProgram prog) {
        TPForm form = singleLineForm(randLineLength());
        return droneActor("line", form, prog);
    }

    public static TPActor regularPolygonActor(TPProgram prog) {
        double size = rand(30, 100);
        int numSides = randInt(3, 8);
        return regularPolygonActor(prog, size, numSides);
    }

    public static TPActor regularPolygonActor(TPProgram prog, double size, int numSides) {
        TPForm form = regularPolygonForm(size, numSides);
        return droneActor("regular polygon (" + numSides + " sides)", form, prog);
    }

    public static TPActor regularThistleActor(TPProgram prog) {
        double size = rand(20, 100);
        int numSides = randInt(3, 16);
        TPForm form = regularThistleForm(size, numSides);
        return droneActor("regular thistle (" + numSides + " spines)", form, prog);
    }

    public static TPActor segmentedPolygonActor(TPProgram prog) {
        double size = rand(30, 100);
        int numSides = randInt(3, 8);
        TPForm form = segmentedPolygonForm(size, numSides);
        return droneActor("segmented polygon (" + numSides + " sides)", form, prog);
    }

    public static TPActor zigzagActor(TPProgram prog) {
        double width = rand(40, 160);
        double sectionLength = rand(20, 80);
        int numSections = randInt(4, 12);
        TPForm form = zigzagForm(width, sectionLength, numSections);
        TPActor actor = new TPActor(form);
        actor.name = "zigzag (" + numSections + " sections)";
        actor.setColorGetter(randColorGetter());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        // random angle and heading
        actor.angle = Math.random() * Math.PI;
        setRandHeading(actor);
        return actor;
    }

    public static TPActor regularPolygonActorWithKeyPart(TPProgram prog) {
        TPActor actor = regularPolygonActor(prog);
        setStrongWithRandomKeyLine(actor);
        return actor;
    }

    public static TPActor regularThistleActorWithKeyPart(TPProgram prog) {
        TPActor actor = regularThistleActor(prog);
        setStrongWithRandomKeyLine(actor);
        return actor;
    }

    public static TPActor zigzagActorWithKeyPart(TPProgram prog) {
        TPActor actor = zigzagActor(prog);
        setStrongWithRandomKeyLine(actor);
        return actor;
    }

    public static TPActor droneActor(String name, TPForm form, TPProgram prog) {
        TPActor actor = new TPActor(form);
        actor.name = name;
        actor.setColorGetter(randColorGetter());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        setRandHeading(actor);
        actor.angleInertia = randAngleInertia();
        actor.setPos(randBoundaryPos(prog));
        return actor;
    }

    public static TPActor shooterActor(TPProgram prog) {
        Spawning spawn = new Spawning();
        spawn.setArchetype(new TPActor(singleLineForm(50)));
        spawn.setInterval(randInt(10, 500));
        if (Math.random() < 0.5)
            spawn.setRelativeRotation(0.5);
        TPActor actor = lineActor(prog);
        actor.name = "shooter";
        actor.addBehaviour(spawn);
        return actor;
    }

    /*------------------------- powerup drones -------------------------*/

    public static TPActor powerupActor(String name, TPForm form, TPProgram prog) {
        TPActor actor = new TPActor(form);
        actor.name = name;
        actor.setColorGetter(randColorGetter());
        // actor.angleInertia = randAngleInertia(0.001);
        actor.angle = Math.random() * Math.PI;
        actor.setPos(randBoundaryPos(prog));
        setRandHeading(actor, 0.005, 0.5);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        return actor;
    }

    public static TPActor powerupActorShooting(TPProgram prog) {
        TPForm form = powerupFormShooting();
        return powerupActor("powerup: shooting", form, prog);
    }

    public static TPActor powerupActorSticky(TPProgram prog) {
        TPForm form = powerupFormSticky();
        return powerupActor("powerup: shooting", form, prog);
    }

    public static TPActor powerupActorStrong(TPProgram prog) {
        TPForm form = powerupFormStrong();
        return powerupActor("powerup: shooting", form, prog);
    }

    public static TPForm powerupForm(TPLine[] lines, PowerupBehaviour powerup) {
        boolean first = true;
        for (TPLine tpl : lines) {
            if (first) {
                tpl.addBehaviour(new SuicidePactBehaviour());
                tpl.addBehaviour(powerup);
                first = false;
            } else {
                tpl.setStrength(TPLine.STRENGTH_HEAVY);
            }
        }
        return new TPForm(lines);
    }

    public static TPForm powerupFormShooting() {
        int unit = 10;
        TPLine[] lines = new TPLine[5];
        lines[0] = new TPLine(new Line(0, -unit, 0, unit));
        lines[1] = new TPLine(new Line(unit * -6, unit * -2, 0, -unit));
        lines[2] = new TPLine(new Line(unit * -6, unit * 2, 0, unit));
        lines[3] = new TPLine(new Line(unit * 6, unit * -2, 0, -unit));
        lines[4] = new TPLine(new Line(unit * 6, unit * 2, 0, unit));
        return powerupForm(lines, new PowerupBehaviourShooting());
    }

    public static TPForm powerupFormSticky() {
        int unit = 10;
        TPLine[] lines = new TPLine[6];
        lines[0] = new TPLine(new Line(0, -unit, 0, unit));
        lines[1] = new TPLine(new Line(unit * -1, unit * -2, unit * -1, unit * 2));
        lines[2] = new TPLine(new Line(unit * -1, unit * -2, 0, -unit));
        lines[3] = new TPLine(new Line(unit * -1, unit * 2, 0, unit));
        lines[4] = new TPLine(new Line(unit * 6, unit * -2, 0, -unit));
        lines[5] = new TPLine(new Line(unit * 6, unit * 2, 0, unit));
        return powerupForm(lines, new PowerupBehaviourSticky());
    }

    public static TPForm powerupFormStrong() {
        int unit = 10;
        TPLine[] lines = new TPLine[5];
        lines[0] = new TPLine(new Line(0.001, 0, 0, unit * 3));
        lines[1] = new TPLine(new Line(0.001, 0, unit * 6, unit * -6));
        lines[2] = new TPLine(new Line(0.001, 0, unit * -6, unit * -6));
        lines[3] = new TPLine(new Line(0.001, unit * 3, unit * 6, unit * -3));
        lines[4] = new TPLine(new Line(0.001, unit * 3, unit * -6, unit * -3));
        return powerupForm(lines, new PowerupBehaviourStrong());
    }

    /*------------------------------ form ------------------------------*/

    public static double randLineLength() {
        return 20 + (Math.random() * 150);
    }

    public static TPForm singleLineForm(double length) {
        double half = length / 2;
        Pt start = new Pt(-half, 0);
        Pt end = new Pt(half, 0);
        return new TPForm(new TPPart[] { new TPLine(new Line(start, end)) });
    }

    public static TPForm singleLineForm(double x1, double y1, double x2, double y2) {
        Pt start = new Pt(x1, y1);
        Pt end = new Pt(x2, y2);
        return new TPForm(new TPPart[] { new TPLine(new Line(start, end)) });
    }

    public static TPForm rectangleForm(double w, double h) {
        double halfW = w / 2;
        double halfH = h / 2;
        Pt a = new Pt(-halfW, -halfH); // top-left
        Pt b = new Pt(-halfW, halfH);  // bottom-left
        Pt c = new Pt(halfW, halfH);   // bottom-right
        Pt d = new Pt(halfW, -halfH);  // top-right
        TPLine lineA = new TPLine(new Line(a, b));
        TPLine lineB = new TPLine(new Line(b, c));
        TPLine lineC = new TPLine(new Line(c, d));
        TPLine lineD = new TPLine(new Line(d, a));
        return new TPForm(new TPPart[] { lineA, lineB, lineC, lineD });
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

    public static TPForm closedLoopForm(Pt[] points) {
        TPLine[] lines = new TPLine[points.length];
        for (int i = 0; i < (lines.length - 1); i++)
            lines[i] = new TPLine(new Line(points[i], points[i + 1]));
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

    /*--------------------------- text-actor ---------------------------*/

    public static TPActor textActor(TPProgram prog, String text) {
        TPActor a = new TPActor(textFormCentered(text));
        // a.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_AT_BOUNDS);
        // TPFactory.setRandHeading(a);
        return a;
    }

    /**
     * todo: make text centered
     */
    public static TPForm textFormCentered(String text) {
        TPForm form = new TPForm();
        form.addPart(new TPTextPart(text));
        return form;
    }

    /*------------------------------ line ------------------------------*/

    public static TPLine lineStandard(double x1, double y1, double x2, double y2) {
        return lineStandard(x1, y1, x2, y2, null);
    }

    public static TPLine lineStandard(double x1, double y1, double x2, double y2,
                                      ColorGetter col) {
        TPLine tpl = new TPLine(new Line(x1, y1, x2, y2));
        tpl.setColorGetter(col);
        return tpl;
    }

    public static TPLine lineStrong(double x1, double y1, double x2, double y2) {
        return lineStrong(x1, y1, x2, y2, null);
    }

    public static TPLine lineStrong(double x1, double y1, double x2, double y2,
                                    ColorGetter col) {
        TPLine tpl = new TPLine(new Line(x1, y1, x2, y2));
        tpl.setStrength(TPLine.STRENGTH_HEAVY);
        tpl.setColorGetter(col);
        return tpl;
    }

    /*----------------------------- color ------------------------------*/

    public static ColorGetter randColorGetter() {
        ColorGetter[] items = new ColorGetter[] {
            // new ColorMono(),
            new ColorRandom(),
            new ColorRandomMono(),
            new ColorSmoothMono(),
            new ColorSmoothColor(),
        };
        int index = (int) (Math.random() * items.length);
        return items[index];
    }

    /*--------------------- position and movement ----------------------*/

    public static void setRandHeading(TPActor actor) {
        setRandHeading(actor, 0.02, 1);
    }

    public static void setRandHeading(TPActor actor, double min, double max) {
        double magnitude = rand(min, max);
        double angle = rand(Math.PI * 2);
        actor.xInertia = Math.sin(angle) * magnitude;
        actor.yInertia = Math.cos(angle) * magnitude;
    }

    public static void setRandAngle(TPActor actor) {
        actor.angle = Math.random() * Math.PI;
    }

    public static double randAngleInertia() {
        // return randAngleInertia(0.007);
        return rand(0.007);
    }

    public static void setRandAngleAndHeading(TPActor actor) {
        setRandHeading(actor);
        setRandAngle(actor);
    }

    // public static double randAngleInertia(double max) {
    public static double rand(double max) {
        return Math.random() * max;
    }

    public static Pt centerPos(TPProgram prog) {
        return new Pt(prog.getGeometry().getXCenter(),
                      prog.getGeometry().getXCenter());
    }

    /**
     * <p>Choose a random position within bounds.</p>
     */
    public static Pt randPos(TPProgram prog) {
        return new Pt(rand(0, prog.getGeometry().getWidth()),
                      rand(0, prog.getGeometry().getHeight()));
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

    public static void anchorToPath(TPActor actor, TPActor path) {
        PathAnchor anchor = new PathAnchor();
        anchor.makePath(path);
        actor.addBehaviour(anchor);
        // add as child, so that they don't destroy one-another
        path.addChild(actor);
    }

    public static void anchorToRandPoint(TPActor actor, TPActor anchorActor) {
        TPForm form = anchorActor.getForm();
        TPLine line = null;

        // count lines in form, then choose one
        int n = 0;
        for (int i = 0; i < form.numParts(); i++)
            if (form.getPart(i) instanceof TPLine)
                n++;

        int chosen = (int) (Math.random() * n);

        for (int i = 0; i < form.numParts(); i++)
            if (form.getPart(i) instanceof TPLine)
                if (i == chosen)
                    line = (TPLine) form.getPart(i);

        PointAnchor anchor = new PointAnchor();
        anchor.setAnchor(line, form);
        actor.addBehaviour(anchor);
        // add as child, so that they don't destroy one-another
        anchorActor.addChild(actor);
    }

    /*--------------------------- properties ---------------------------*/

    public static void setStrongWithRandomKeyLine(TPActor actor) {
        TPForm form = actor.getForm();
        // count number of line-parts
        int lineCount = 0;
        for (int i = 0; i < form.numParts(); i++) {
            if (form.getPart(i) instanceof TPLine) {
                lineCount++;
            }
        }
        // choose one at random
        int chosen = randInt(lineCount);
        lineCount = 0;
        for (int i = 0; i < form.numParts(); i++) {
            if (form.getPart(i) instanceof TPLine) {
                TPLine tpl = (TPLine) form.getPart(i);
                if (lineCount == chosen) {
                    tpl.setStrength(TPLine.STRENGTH_LIGHT);
                    tpl.addBehaviour(new SuicidePactBehaviour());
                } else {
                    tpl.setStrength(TPLine.STRENGTH_HEAVY);
                }
                lineCount++;
            }
        }
    }

    /*------------------------- helper methods -------------------------*/

    // private static double rand(double max) {
    //     return Math.random() * max;
    // }

    public static double rand(double min, double max) {
        double dist = max - min;
        return min + (Math.random() * dist);
    }

    public static int randInt(int max) {
        return (int) (Math.random() * max);
    }

    public static int randInt(int min, int max) {
        double dist = max - min;
        return (int) (min + (Math.random() * dist));
    }

}
