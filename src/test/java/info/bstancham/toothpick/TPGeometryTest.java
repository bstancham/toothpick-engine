package info.bstancham.toothpick;

import org.junit.Test;
import static org.junit.Assert.*;

public class TPGeometryTest {

    private double DELTA = 0.00001;

    @Test
    public void testSetupAndCenter() {
        TPGeometry geom = new TPGeometry();
        geom.setupAndCenter(300, 800);
        assertEquals(300, geom.getWidth());
        assertEquals(800, geom.getHeight());
        assertEquals(150, geom.getXCenter());
        assertEquals(400, geom.getYCenter());
        assertEquals(-150, geom.xOffset, DELTA);
        assertEquals(-400, geom.yOffset, DELTA);
    }

    @Test
    public void testToScreen() {
        TPGeometry geom = new TPGeometry();
        geom.setupAndCenter(400, 600);
        // with default scaling and offset co-ords should be unchanged
        assertEquals(10, geom.xToScreen(10), DELTA);
        assertEquals(35, geom.yToScreen(35), DELTA);

        geom.xOffset = 420;
        geom.yOffset = -64;
        // 200 + 430
        assertEquals(630, geom.xToScreen(10), DELTA);
        // 300 - 29
        assertEquals(271, geom.yToScreen(35), DELTA);

        geom.scale = 0.5;
        // 200 + 215
        assertEquals(415, geom.xToScreen(10), DELTA);
        // 300 - 14.5
        assertEquals(285.5, geom.yToScreen(35), DELTA);
    }

    @Test
    public void testFromScreen() {
        TPGeometry geom = new TPGeometry();
        geom.setWidth(400);
        geom.setHeight(600);

        // -(200 - 10)
        assertEquals(-190, geom.xFromScreen(10), DELTA);
        // -(300 - 35)
        assertEquals(-265, geom.yFromScreen(35), DELTA);

        geom.xOffset = -420;
        geom.yOffset = 70;
        // -((200 - 420) - 10)
        // -(-220 - 10)
        // -(-230)
        // 230
        assertEquals(230, geom.xFromScreen(10), DELTA);
        // -((300 + 70) - 35)
        // -(370 - 35)
        // -335
        assertEquals(-335, geom.yFromScreen(35), DELTA);

        geom.scale = 0.5;
        assertEquals(460, geom.xFromScreen(10), DELTA);
        assertEquals(-670, geom.yFromScreen(35), DELTA);
    }

    /**
     * Should be able convert to screen co-ords then back to TPG co-ordinates and get the
     * same number we started with.
     */
    @Test
    public void testConvertFromAndToScreen() {
        TPGeometry geom = new TPGeometry();
        geom.setupAndCenter(400, 600);

        double x = 32;
        double y = -106;

        double xScreen = geom.xToScreen(x);
        double yScreen = geom.yToScreen(y);
        double xGeom = geom.xFromScreen(xScreen);
        double yGeom = geom.yFromScreen(yScreen);
        assertEquals(x, xGeom, DELTA);
        assertEquals(y, yGeom, DELTA);

        geom.xOffset = -420;
        geom.yOffset = 70;

        xScreen = geom.xToScreen(x);
        yScreen = geom.yToScreen(y);
        xGeom = geom.xFromScreen(xScreen);
        yGeom = geom.yFromScreen(yScreen);
        assertEquals(x, xGeom, DELTA);
        assertEquals(y, yGeom, DELTA);

        geom.scale = 0.5;

        System.out.println("x=" + x + " y=" + y);
        xScreen = geom.xToScreen(x);
        yScreen = geom.yToScreen(y);
        System.out.println("xScreen=" + xScreen + " yScreen=" + yScreen);
        xGeom = geom.xFromScreen(xScreen);
        yGeom = geom.yFromScreen(yScreen);
        System.out.println("xGeom=" + xGeom + " yGeom=" + yGeom);

        // 200 + ((32 - 420) * 0.5)
        // 200 + (-388 * 0.5)
        // 200 - 194
        // 6
        // assertEquals(126, xScreen, DELTA);

        // assertEquals(x, xGeom, DELTA);
        // assertEquals(0, yGeom, DELTA);
    }

    @Test
    public void testXDistWrapped() {
        TPGeometry geom = new TPGeometry();
        geom.setupAndCenter(350, 200);
        // not wrapped
        assertEquals(60, geom.xDistWrapped(100, 160), DELTA);
        assertEquals(-95, geom.xDistWrapped(100, 5), DELTA);
        // wrapped around
        assertEquals(120, geom.xDistWrapped(250, 20), DELTA);
        assertEquals(-150, geom.xDistWrapped(100, 300), DELTA);
        // origin and target the same
        assertEquals(0, geom.xDistWrapped(224, 224), DELTA);
    }

    @Test
    public void testYDistWrapped() {
        TPGeometry geom = new TPGeometry();
        geom.setupAndCenter(350, 200);
        // not wrapped
        assertEquals(40, geom.yDistWrapped(150, 190), DELTA);
        assertEquals(-30, geom.yDistWrapped(150, 120), DELTA);
        // wrapped around
        assertEquals(65, geom.yDistWrapped(150, 15), DELTA);
        assertEquals(-70, geom.yDistWrapped(40, 170), DELTA);
        // origin and target the same
        assertEquals(0, geom.yDistWrapped(21, 21), DELTA);
    }

}
