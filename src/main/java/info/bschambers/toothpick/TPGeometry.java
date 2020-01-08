package info.bschambers.toothpick;

/**
 * <p>{@code xCenter}/{@code yCenter} (if set correctly) represent the center of the
 * screen.</p>
 *
 * <p>{@code width}/{@code height} represent the dimensions of the play area.</p>
 *
 * <p>{@code xOffset}/{@code yOffset} represent the position of the top-left corner of the
 * play-area relative to the center point. So for example, if the play area is perfectly
 * centered then xOffset will be equal to {@code -(width / 2)}.</p>
 */
public class TPGeometry implements TPEncodingHelper {

    private int width = 100;
    private int height = 100;
    private int xCenter = 50;
    private int yCenter = 50;
    public double xOffset = 0;
    public double yOffset = 0;
    public double scale = 1;

    @Deprecated
    public void setupAndCenter(int width, int height) {
        setWidth(width);
        setHeight(height);
        xOffset = -xCenter;
        yOffset = -yCenter;
    }

    public int getWidth() {
        return width;
    }

    @Deprecated
    public void setWidth(int val) {
        width = val;
        xCenter = width / 2;
    }

    public int getHeight() {
        return height;
    }

    @Deprecated
    public void setHeight(int val) {
        height = val;
        yCenter = height / 2;
    }

    public int getXCenter() {
        return xCenter;
    }

    public void setXCenter(int val) {
        xCenter = val;
    }

    public int getYCenter() {
        return yCenter;
    }

    public void setYCenter(int val) {
        yCenter = val;
    }

    public double xToScreen(double x) {
        return xCenter + ((x + xOffset) * scale);
    }

    public double yToScreen(double y) {
        return yCenter + ((y + yOffset) * scale);
    }

    public double xFromScreen(double x) {
        if (scale == 0)
            return xCenter;
        double temp = -((xCenter + xOffset) - x);
        return temp / scale;
    }

    public double yFromScreen(double y) {
        if (scale == 0)
            return yCenter;
        double temp = -((yCenter + yOffset) - y);
        return temp / scale;
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(Double.class, width, "setWidth");
        params.addMethod(Double.class, height, "setHeight");
        params.addField(Double.class, xOffset, "xOffset");
        params.addField(Double.class, yOffset, "yOffset");
        params.addField(Double.class, scale, "scale");
        return params;
    }

}
