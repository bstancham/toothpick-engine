package info.bschambers.toothpick;

public class TPGeometry implements TPEncodingHelper {

    private int width = 100;
    private int height = 100;
    private int xCenter = 50;
    private int yCenter = 50;
    public double xOffset = 0;
    public double yOffset = 0;
    public double scale = 1;

    public void setupAndCenter(int width, int height) {
        setWidth(width);
        setHeight(height);
        xOffset = -xCenter;
        yOffset = -yCenter;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int val) {
        width = val;
        xCenter = width / 2;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int val) {
        height = val;
        yCenter = height / 2;
    }

    public int getXCenter() {
        return xCenter;
    }

    public int getYCenter() {
        return yCenter;
    }

    public double getX(double x) {
        return xCenter + (x + xOffset) * scale;
    }

    public double getY(double y) {
        return yCenter + (y + yOffset) * scale;
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
