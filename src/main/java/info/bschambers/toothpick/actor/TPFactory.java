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
        TPActor actor = new TPActor(singleLineFormVert(length));
        actor.name = "player";
        actor.setPos(pos);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_AT_BOUNDS);
        actor.setColorGetter(new ColorMono(Color.PINK));
        // actor.setColorGetter(new ColorRandom());
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new KeyInputThrustInertia());
        return player;
    }

    public static TPPlayer player(TPActor actor) {
        TPPlayer player = new TPPlayer(actor);
        player.setInputHandler(new KeyInputThrustInertia());
        return player;
    }

    public static void setPlayerKeysQAWE(KeyInputHandler keys) {
        keys.bindUp.setCode(81);      // q
        keys.bindDown.setCode(65);    // a
        keys.bindLeft.setCode(87);    // w
        keys.bindRight.setCode(69);   // e
        keys.bindAction.setCode(90);  // z
        keys.bindZoomIn.setCode(50);  // 2
        keys.bindZoomOut.setCode(51); // 3
    }

    public static void setPlayerKeysIKOP(KeyInputHandler keys) {
        keys.bindUp.setCode(73);      // i
        keys.bindDown.setCode(75);    // k
        keys.bindLeft.setCode(79);    // o
        keys.bindRight.setCode(80);   // p
        keys.bindAction.setCode(44);  // ,
        keys.bindZoomIn.setCode(57);  // 9
        keys.bindZoomOut.setCode(58); // 0
    }

    /*----------------------------- drones -----------------------------*/

    /**
     * Creates a new {@code TPActor} with single-line form of random length.
     */
    public static TPActor lineActor(TPProgram prog) {
        TPForm form = singleLineFormVert(randLineLength());
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
        actor.setColorGetter(randColorGetterDynamic());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        // random angle and heading
        actor.angle = Math.random() * Math.PI;
        setRandHeading(actor);
        return actor;
    }

    public static TPActor regularPolygonActorWithKeyPart(TPProgram prog) {
        TPActor actor = regularPolygonActor(prog);
        setStrongWithRandomKeyLink(actor.getForm());
        return actor;
    }

    public static TPActor regularThistleActorWithKeyPart(TPProgram prog) {
        TPActor actor = regularThistleActor(prog);
        setStrongWithRandomKeyLink(actor.getForm());
        return actor;
    }

    public static TPActor zigzagActorWithKeyPart(TPProgram prog) {
        TPActor actor = zigzagActor(prog);
        setStrongWithRandomKeyLink(actor.getForm());
        return actor;
    }

    public static TPActor droneActor(String name, TPForm form, TPProgram prog) {
        TPActor actor = new TPActor(form);
        actor.name = name;
        actor.setColorGetter(randColorGetterDynamic());
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        setRandHeading(actor);
        actor.angleInertia = randAngleInertia();
        actor.setPos(randBoundaryPos(prog));
        return actor;
    }

    public static TPActor shooterActor(TPProgram prog) {
        Spawning spawn = new Spawning();
        spawn.setArchetype(new TPActor(singleLineFormHoriz(50)));
        spawn.setInterval(randInt(10, 500));
        if (Math.random() < 0.3)
            spawn.setRelativeRotation(Geom.QUARTER_TURN);
        TPActor actor = lineActor(prog);
        actor.name = "shooter";
        actor.addBehaviour(spawn);
        return actor;
    }

    /*------------------------- powerup drones -------------------------*/

    public static TPActor powerupActor(TPProgram prog, String name, TPForm form, PowerupBehaviour powerup) {
        setPowerupForm(form, powerup);
        TPActor actor = new TPActor(form);
        actor.name = name;
        actor.setColorGetter(randColorGetterDynamic());
        // actor.angleInertia = randAngleInertia(0.001);
        actor.angle = Math.random() * Math.PI;
        actor.setPos(randBoundaryPos(prog));
        setRandHeading(actor, 0.005, 0.5);
        actor.setBoundaryBehaviour(TPActor.BoundaryBehaviour.WRAP_PARTS_AT_BOUNDS);
        return actor;
    }

    public static TPActor powerupActorShooting(TPProgram prog) {
        return powerupActor(prog, "powerup: shooting",
                            powerupFormShooting(), new PowerupBehaviourShooting());
    }

    public static TPActor powerupActorSticky(TPProgram prog) {
        return powerupActor(prog, "powerup: shooting",
                            powerupFormSticky(), new PowerupBehaviourSticky());
    }

    public static TPActor powerupActorStrong(TPProgram prog) {
        return powerupActor(prog, "powerup: shooting",
                            powerupFormStrong(), new PowerupBehaviourStrong());
    }

    public static TPForm powerupFormShooting() {
        double unit = 10;
        TPForm form = new TPForm();
        Node n1 = new Node(0, -unit);
        Node n2 = new Node(0, unit);
        Node n3 = new Node(unit * -6, unit * -2);
        Node n4 = new Node(unit * -6, unit * 2);
        Node n5 = new Node(unit * 6, unit * -2);
        Node n6 = new Node(unit * 6, unit * 2);
        form.addPart(new TPLink(n1, n2));
        form.addPart(new TPLink(n3, n1));
        form.addPart(new TPLink(n4, n2));
        form.addPart(new TPLink(n5, n1));
        form.addPart(new TPLink(n6, n2));
        form.housekeeping();
        return form;
    }

    public static TPForm powerupFormSticky() {
        double unit = 10;
        TPForm form = new TPForm();
        Node n1 = new Node(0, -unit);
        Node n2 = new Node(0, unit);
        Node n3 = new Node(-unit, unit * -2);
        Node n4 = new Node(-unit, unit * 2);
        Node n5 = new Node(unit * 6, unit * -2);
        Node n6 = new Node(unit * 6, unit * 2);
        form.addPart(new TPLink(n1, n2));
        form.addPart(new TPLink(n3, n4));
        form.addPart(new TPLink(n3, n1));
        form.addPart(new TPLink(n4, n2));
        form.addPart(new TPLink(n5, n1));
        form.addPart(new TPLink(n6, n2));
        form.housekeeping();
        return form;
    }

    public static TPForm powerupFormStrong() {
        double zero = 0.00000001;
        double unit = 10;
        TPForm form = new TPForm();
        Node n1 = new Node(zero, 0);
        Node n2 = new Node(0, unit * 3);
        Node n3 = new Node(unit * 6, unit * -6);
        Node n4 = new Node(unit * -6, unit * -6);
        Node n5 = new Node(zero, unit * 3);
        Node n6 = new Node(unit * 6, unit * -3);
        Node n7 = new Node(unit * -6, unit * -3);
        form.addPart(new TPLink(n1, n2));
        form.addPart(new TPLink(n1, n3));
        form.addPart(new TPLink(n1, n4));
        form.addPart(new TPLink(n5, n6));
        form.addPart(new TPLink(n5, n7));
        form.housekeeping();
        return form;
    }

    /*------------------------------ form ------------------------------*/

    public static double randLineLength() {
        return 20 + (Math.random() * 150);
    }

    public static TPForm singleLineFormHoriz(double length) {
        double half = length / 2;
        TPForm form = new TPForm();
        form.addPart(new TPLink(new Node(-half, 0), new Node(half, 0)));
        form.housekeeping();
        return form;
    }

    public static TPForm singleLineFormVert(double length) {
        double half = length / 2;
        TPForm form = new TPForm();
        form.addPart(new TPLink(new Node(0, -half), new Node(0, half)));
        form.housekeeping();
        return form;
    }

    public static TPForm singleLineForm(double x1, double y1, double x2, double y2) {
        TPForm form = new TPForm();
        form.addPart(new TPLink(new Node(x1, y1), new Node(x2, y2)));
        form.housekeeping();
        return form;
    }

    public static TPForm rectangleForm(double w, double h) {
        double halfW = w / 2;
        double halfH = h / 2;
        Node a = new Node(-halfW, -halfH);
        Node b = new Node(-halfW, halfH);
        Node c = new Node(halfW, halfH);
        Node d = new Node(halfW, -halfH);
        TPForm form = new TPForm();
        form.addPart(new TPLink(a, b));
        form.addPart(new TPLink(b, c));
        form.addPart(new TPLink(c, d));
        form.addPart(new TPLink(d, a));
        form.housekeeping();
        return form;
    }

    public static TPForm regularPolygonForm(double size, int numSides) {
        Node[] nodes = pointsToNodes(regularPolygonPoints(size, numSides));
        TPForm form = new TPForm();
        for (int i = 0; i < (nodes.length - 1); i++)
            form.addPart(new TPLink(nodes[i], nodes[i + 1]));
        form.addPart(new TPLink(nodes[nodes.length - 1], nodes[0]));
        form.housekeeping();
        return form;
    }

    public static TPForm regularThistleForm(double size, int numSides) {
        Node zeroNode = makeZeroNode();
        Node[] nodes = pointsToNodes(regularPolygonPoints(size, numSides));
        TPForm form = new TPForm();
        for (Node n : nodes)
            form.addPart(new TPLink(zeroNode, n));
        form.housekeeping();
        return form;
    }

    public static TPForm segmentedPolygonForm(double size, int numSides) {
        Node zeroNode = makeZeroNode();
        Node[] nodes = pointsToNodes(regularPolygonPoints(size, numSides));
        TPForm form = new TPForm();
        // thistle nodes
        for (Node n : nodes)
            form.addPart(new TPLink(zeroNode, n));
        // polygon nodes
        for (int i = 0; i < (nodes.length - 1); i++)
            form.addPart(new TPLink(nodes[i], nodes[i + 1]));
        form.addPart(new TPLink(nodes[nodes.length - 1], nodes[0]));
        form.housekeeping();
        return form;
    }

    public static TPForm zigzagForm(double width, double sectionLength, int numSections) {
        return openPathForm(zigzagPoints(width, sectionLength, numSections));
    }

    /**
     * @param size Size in greatest dimension (length).
     */
    public static TPForm shipForm1(double size) {
        double half = size / 2;
        double quart = size / 4;
        Node[] nodes = new Node[3];
        nodes[0] = new Node(half, 0);
        nodes[1] = new Node(-half, quart);
        nodes[2] = new Node(-half, -quart);
        TPForm form = closedLoopForm(nodes);
        form.housekeeping();
        setSuicidePact(form);
        return form;
    }

    public static TPForm openPathForm(Pt[] points) {
        return openPathForm(pointsToNodes(points));
    }

    public static TPForm openPathForm(Node[] nodes) {
        TPForm form = new TPForm();
        for (int i = 0; i < (nodes.length - 1); i++)
            form.addPart(new TPLink(nodes[i], nodes[i + 1]));
        form.housekeeping();
        return form;
    }

    public static TPForm closedLoopForm(Pt[] points) {
        return closedLoopForm(pointsToNodes(points));
    }

    public static TPForm closedLoopForm(Node[] nodes) {
        TPForm form = new TPForm();
        for (int i = 0; i < (nodes.length - 1); i++)
            form.addPart(new TPLink(nodes[i], nodes[i + 1]));
        form.addPart(new TPLink(nodes[nodes.length - 1], nodes[0]));
        form.housekeeping();
        return form;
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

    public static TPLink linkStandard(double x1, double y1, double x2, double y2) {
        return linkStandard(x1, y1, x2, y2, null);
    }

    public static TPLink linkStandard(double x1, double y1, double x2, double y2,
                                      ColorGetter col) {
        TPLink ln = new TPLink(new Node(x1, y1), new Node(x2, y2));
        ln.setColorGetter(col);
        return ln;
    }

    public static TPLink linkStrong(double x1, double y1, double x2, double y2) {
        return linkStrong(x1, y1, x2, y2, null);
    }

    public static TPLink linkStrong(double x1, double y1, double x2, double y2,
                                    ColorGetter col) {
        TPLink ln = new TPLink(new Node(x1, y1), new Node(x2, y2));
        ln.setStrength(TPLink.STRENGTH_HEAVY);
        ln.setColorGetter(col);
        return ln;
    }

    /*----------------------------- color ------------------------------*/

    public static ColorGetter randColorGetterDynamic() {
        ColorGetter[] items = new ColorGetter[] {
            new ColorRandom(),
            new ColorRandomMono(),
            new ColorSmoothMono(),
            new ColorSmoothColor(),
        };
        int index = (int) (Math.random() * items.length);
        return items[index];
    }

    public static ColorGetter randColorGetterMono() {
        ColorGetter[] items = new ColorGetter[] {
            ColorMono.WHITE,
            ColorMono.BLACK,
            ColorMono.RED,
            ColorMono.GREEN,
            ColorMono.BLUE,
            ColorMono.CYAN,
            ColorMono.MAGENTA,
            ColorMono.YELLOW,
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
        return rand(0.007);
    }

    public static void setRandAngleAndHeading(TPActor actor) {
        setRandHeading(actor);
        setRandAngle(actor);
    }

    public static Pt centerPos(TPProgram prog) {
        return new Pt(prog.getGeometry().getXCenter(),
                      prog.getGeometry().getYCenter());
    }

    public static Pt p1StartPos(TPProgram prog) {
        return new Pt(prog.getGeometry().getXOneThird(),
                      prog.getGeometry().getYCenter());
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
        int chosenIndex = (int) (Math.random() * anchorActor.getForm().numNodes());
        Node anchorNode = anchorActor.getForm().getNode(chosenIndex);
        PointAnchor anchor = new PointAnchor();
        anchor.setAnchor(anchorNode);
        actor.addBehaviour(anchor);
        // add as child, so that they don't destroy one-another
        anchorActor.addChild(actor);
    }

    /*--------------------------- properties ---------------------------*/

    public static void setStrongWithRandomKeyLink(TPForm form) {
        // choose one link at random
        int chosen = randInt(form.numLinks());
        for (int i = 0; i < form.numLinks(); i++) {
            if (i == chosen) {
                form.getLink(i).setStrength(TPLink.STRENGTH_LIGHT);
                form.getLink(i).addBehaviour(new PartSuicidePact());
            } else {
                form.getLink(i).setStrength(TPLink.STRENGTH_HEAVY);
            }
        }
    }

    /**
     * <p>Adds powerup behaviour to first link of form.</p>
     */
    public static void setPowerupForm(TPForm form, PowerupBehaviour powerup) {
        if (form.numLinks() > 0) {
            form.getLink(0).setStrength(TPLink.STRENGTH_LIGHT);
            form.getLink(0).addBehaviour(new PartSuicidePact());
            form.getLink(0).addBehaviour(powerup);
            for (int i = 1; i < form.numLinks(); i++) {
                form.getLink(i).setStrength(TPLink.STRENGTH_HEAVY);
            }
        }
    }

    public static void setSuicidePact(TPForm form) {
        for (int i = 0; i < form.numLinks(); i++)
            form.getLink(i).addBehaviour(new PartSuicidePact());
    }

    /*------------------------- helper methods -------------------------*/

    public static double rand(double max) {
        return Math.random() * max;
    }

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

    private static Node makeZeroNode() {
        return new Node(NEARLY_ZERO, NEARLY_ZERO);
    }

    private static Node[] pointsToNodes(Pt[] points) {
        Node[] nodes = new Node[points.length];
        for (int i = 0; i < points.length; i++)
            nodes[i] = new Node(points[i].x, points[i].y);
        return nodes;
    }

}
