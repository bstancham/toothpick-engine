package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;

/**
 * Wraps a TPActor and a KeyInputHandler.
 */
public class TPPlayer implements TPEncodingHelper {

    public static final TPPlayer NULL = new TPPlayer();

    private TPActor archetype;
    private TPActor actor;
    private KeyInputHandler input = KeyInputHandler.NULL;

    public TPPlayer() {
        this(TPActor.NULL);
    }

    public TPPlayer(TPActor a) {
        setArchetype(a);
    }

    public TPActor getArchetype() {
        return archetype;
    }

    public void setArchetype(TPActor a) {
        archetype = a.copy();
        reset();
    }

    public TPActor getActor() {
        return actor;
    }

    public void setActor(TPActor a) {
        actor = a;
    }

    public KeyInputHandler getInputHandler() {
        return input;
    }

    public void setInputHandler(KeyInputHandler input) {
        this.input = input;
        archetype.setInputHandler(input);
        actor.setInputHandler(input);
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean retainStats) {
        TPActor old = actor;
        actor = archetype.copy();
        if (retainStats)
            actor.copyStats(old);
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        params.addMethod(TPActor.class, getArchetype(), "setArchetype");
        params.addMethod(TPActor.class, getActor(), "setActor");
        params.addMethod(KeyInputHandler.class, getInputHandler(), "setInputHandler");
        return params;
    }

}
