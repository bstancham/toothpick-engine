package info.bschambers.toothpick.actor;

public class EightWayController extends PlayerController {

    @Override
    public void update() {
        int amt = 3;
        if (bindUp.value())    pos = pos.addY(-amt);
        if (bindDown.value())  pos = pos.addY(amt);
        if (bindLeft.value())  pos = pos.addX(-amt);
        if (bindRight.value()) pos = pos.addX(amt);
    }

}
