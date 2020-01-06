package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPProgram;

public interface TPBehaviour {

    /** ID string used by key-input-behaviour singleton group. */
    public static final String KEY_INPUT_BEHAVIOUR_FLAG = "key-input-behaviour";

    /**
     * <p>Gets the name of the singleton-group which this behaviour belongs to, or an
     * empty string if it belongs to none. A program may only have one member instance of
     * any singleton-group. If no group name is given then a program may have multiple
     * instances of this behaviour.</p>
     *
     * @return The name of the singleton-group which this behaviour belongs to, or an
     * empty string if it belongs to none.
     */
    default String getSingletonGroup() {
        return "";
    }

    void update(TPProgram prog, TPActor a);

}
