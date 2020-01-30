package info.bschambers.toothpick.actor;

public interface PartBehaviour {

    void action(TPPart part);

    PartBehaviour copy();

}
