package info.bschambers.toothpick.actor;

/** Wraps a TPActor and a KeyInputHandler. */
public class TPPlayer {

    public static final TPPlayer NULL = new TPPlayer(TPActor.NULL);

    private TPActor archetype;
    private TPActor actor;
    private KeyInputHandler input = KeyInputHandler.NULL;

    public TPPlayer(TPActor a) {
        setArchetype(a);
    }

    public void setArchetype(TPActor a) {
        archetype = a.copy();
        reset();
    }

    public void reset() {
        actor = archetype.copy();
    }

    public TPActor getArchetype() {
        return archetype;
    }

    public TPActor getActor() {
        return actor;
    }

    public KeyInputHandler getInputHandler() {
        return input;
    }

    public void setInputHandler(KeyInputHandler input) {
        this.input = input;
        archetype.setInputHandler(input);
        actor.setInputHandler(input);
    }

}
