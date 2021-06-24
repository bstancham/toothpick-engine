package info.bstancham.toothpick.actor;

import info.bstancham.toothpick.TPEncoding;
import info.bstancham.toothpick.TPEncodingHelper;

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

    public TPPlayer copy() {
        TPPlayer p = new TPPlayer(archetype.copy());
        p.actor = actor.copy();
        p.input = input.copy();
        return p;
    }

    public String infoString() {
        return this + "\nKeyInputHandler = " + input;
    }

    public TPActor getArchetype() {
        return archetype;
    }

    public void setArchetype(TPActor a) {
        archetype = a.copy();
        archetype.setIsPlayer(true);
        reset();
    }

    public TPActor getActor() {
        return actor;
    }

    /** Sets the actor, without setting the archetype. */
    public void setActor(TPActor a) {
        actor = a;
        a.setIsPlayer(true);
        a.setInputHandler(input);
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
