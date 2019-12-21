package info.bschambers.toothpick.actor;

public class ThrustInertiaController extends PlayerController {

    private double xInertia = 0;
    private double yInertia = 0;
    private double inertiaStep = 0.01;
    private double rotStep = 0.005;

    @Override
    public void update() {
        if (bindUp.value()) {
            xInertia += Math.sin(Math.PI * angle) * inertiaStep;
            yInertia -= Math.cos(Math.PI * angle) * inertiaStep;
        }
        if (bindDown.value()) {
            xInertia -= Math.sin(Math.PI * angle) * inertiaStep;
            yInertia += Math.cos(Math.PI * angle) * inertiaStep;
        }
        if (bindLeft.value())
            angle -= rotStep;
        if (bindRight.value())
            angle += rotStep;
        pos = pos.add(xInertia, yInertia);
    }

}
