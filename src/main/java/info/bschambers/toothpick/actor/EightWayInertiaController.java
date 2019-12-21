package info.bschambers.toothpick.actor;

public class EightWayInertiaController extends PlayerController {

    private double xInertia = 0;
    private double yInertia = 0;
    private double step = 0.5;

    @Override
    public void update() {
        if (bindUp.value())
            yInertia -= step;
        if (bindDown.value())
            yInertia += step;
        if (bindLeft.value())
            xInertia -= step;
        if (bindRight.value())
            xInertia += step;
        pos = pos.add(xInertia, yInertia);
    }

}
