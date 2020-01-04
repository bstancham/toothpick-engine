package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncodingHelper;
import java.awt.Color;

public interface ColorGetter extends TPEncodingHelper {

    ColorGetter copy();

    Color get();

    /**
     * <p>Get the next color, in the context of background color {@code bgColor}. The
     * default implementation ensures that the new color is not too close to the
     * background color using {@link enforceMinProximity}.</p>
     */
    default Color getWithBG(Color bgColor) {
        return enforceMinProximity(200, get(), bgColor);
    }

    /*--------------------- static utility methods ---------------------*/

    public static Color randColor() {
        return new Color(rand255(), rand255(), rand255());
    }

    public static int rand255() {
        return (int) (Math.random() * 256);
    }

    public static Color setBrightness(Color c, double brightness) {
        int r = (int) (c.getRed() * brightness);
        int g = (int) (c.getGreen() * brightness);
        int b = (int) (c.getBlue() * brightness);
	if (r > 255) r = 255;
	if (g > 255) g = 255;
	if (b > 255) b = 255;
        return new Color(r, g, b);
    }

    /**
     * <p>Calculates the proximity of one color to another. The proximity of two colors
     * here means the sum of the proximity of each, the red, green and blue bands of the
     * two colors, and the proximity of each of these is measured as the numerical
     * difference between the two values.</p>
     *
     * <p>EXAMPLE:</p>
     *
     * <pre>
     * {@code
     *         | Color A | Color B | difference |
     * |-------+---------+---------+------------|
     * | red   |     255 |     200 |         55 |
     * | green |     100 |     100 |          0 |
     * | blue  |     110 |     115 |          5 |
     * |-------+---------+---------+------------|
     *                             | TOTAL = 60 |
     * }
     * </pre>
     *
     * @return The integer proximity between Color a and Color b.
     */
    public static int proximity(Color a, Color b) {
        int rdiff = Math.abs(a.getRed() - b.getRed());
        int gdiff = Math.abs(a.getGreen() - b.getGreen());
        int bdiff = Math.abs(a.getBlue() - b.getBlue());
        return rdiff + gdiff + bdiff;
    }

    /**
     * <p>Ensure that Color {@code a} has no less than {@code min} proximity to Color
     * {@code b}.</p>
     *
     * @return Color {@code a}, possibly modified to maintain minimum distance from Color
     * {@code b}.
     */
    public static Color enforceMinProximity(int min, Color a, Color b) {
        int prox = proximity(a, b);

        if (prox >= min)
            return a;

        int portion = prox / 3;
        int rportion = portion;
        int gportion = portion;
        int bportion = portion;

        int rem = prox % 3;
        if (rem > 0) rportion += 1;
        if (rem > 1) gportion += 1;

        int ra = a.getRed();
        int rb = b.getRed();
        int ga = a.getGreen();
        int gb = b.getGreen();
        int ba = a.getBlue();
        int bb = b.getBlue();

        int rdir = (ra < rb ? -1 : 1);
        int gdir = (ga < gb ? -1 : 1);
        int bdir = (ba < bb ? -1 : 1);

        ColorShifter shifter = new ColorShifter(ra, ga, ba);
        shifter.rDir = rdir;
        shifter.gDir = gdir;
        shifter.bDir = bdir;
        shifter.rShift = rportion;
        shifter.gShift = gportion;
        shifter.bShift = bportion;

        int count = 0;
        while (!shifter.isFinished() && count < 3) {
            shifter.shift();
            count++;
            if (count >= 3) {
                System.out.println("ColorGetter.enforceMinProximity()"
                                   + " --> stop shifter after three repetitions");
                shifter.makeSafe();
            }
        }

        Color col = new Color(shifter.r, shifter.g, shifter.b);
        return col;
    }

    /**
     * <p>Used by {@link enforceMinProximity} for attempting to iteratively shift rgb
     * values to mutually acceptable positions.</p>
     */
    public static class ColorShifter {

        int r;
        int g;
        int b;
        int rDir = 1;
        int gDir = 1;
        int bDir = 1;
        int rShift = 0;
        int gShift = 0;
        int bShift = 0;

        public ColorShifter(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public boolean isFinished() {
            return rShift == 0 && gShift == 0 && bShift == 0;
        }

        /**
         * <p>Ensures that r/g/b values are all within the range of 0-255. Use this to
         * cleanup when repeated calls to {@link shift} do not succeed in finishing the
         * job.</p>
         */
        public void makeSafe() {
            r = fitBounds(r);
            g = fitBounds(g);
            b = fitBounds(b);
        }

        private int fitBounds(int val) {
            if (val > 255) return 255;
            if (val < 0) return 0;
            return val;
        }

        public void shift() {

            // shift values
            r += (rShift * rDir);
            g += (gShift * gDir);
            b += (bShift * bDir);
            rShift = 0;
            gShift = 0;
            bShift = 0;

            // boundary checking
            if (r > 255) {
                int over = r - 255;
                r = 255;
                int half = over / 2;
                gShift = half;
                bShift = half;
                if (over % 2 == 1)
                    gShift += 1;
            } else if (g > 255) {
                int over = g - 255;
                g = 255;
                int half = over / 2;
                bShift = half;
                rShift = half;
                if (over % 2 == 1)
                    bShift += 1;
            } else if (b > 255) {
                int over = b - 255;
                b = 255;
                int half = over / 2;
                rShift = half;
                gShift = half;
                if (over % 2 == 1)
                    rShift += 1;
            } else if (r < 0) {
                int over = -r;
                r = 0;
                int half = over / 2;
                gShift = half;
                bShift = half;
                if (over % 2 == 1)
                    gShift += 1;
            } else if (g < 0) {
                int over = -g;
                g = 0;
                int half = over / 2;
                bShift = half;
                rShift = half;
                if (over % 2 == 1)
                    bShift += 1;
            } else if (b < 0) {
                int over = -b;
                b = 0;
                int half = over / 2;
                rShift = half;
                gShift = half;
                if (over % 2 == 1)
                    rShift += 1;
            }
        }

    }

}
