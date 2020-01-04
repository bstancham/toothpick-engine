package info.bschambers.toothpick.actor;

import java.awt.Color;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColorGetterTest {

    @Test
    public void testProximity() {
        Color a = new Color(255, 100, 110);
        Color b = new Color(200, 100, 115);
        assertEquals(60, ColorGetter.proximity(a, b));
        assertEquals(510, ColorGetter.proximity(Color.RED, Color.GREEN));
    }

    @Test
    public void testEnforceMinProximity() {

        Color a = new Color(255, 100, 110);
        Color b = new Color(200, 100, 115);
        Color result = ColorGetter.enforceMinProximity(50, a, b);
        assertEquals(result, a);

        result = ColorGetter.enforceMinProximity(100, a, b);
        assertNotEquals(result, a);
        // assertEquals(50, ColorGetter.proximity(result, b));

    }

}
