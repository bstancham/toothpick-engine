package info.bschambers.toothpick;

public interface ProgramBehaviour {

    /** Singleton-group ID for physics-models. */
    public static final String PHYSICS_MODEL_ID = "physics-model";

    /** Singleton-group ID for intro-transitions. */
    public static final String INTRO_TRANSITION_ID = "intro-transition";

    /** Singleton-group ID for outro-transitions. */
    public static final String OUTRO_TRANSITION_ID = "outro-transition";

    /**
     * Returns an exact copy of this ProgramBehaviour. In some cases may return the actual
     * same instance.
     */
    ProgramBehaviour copy();

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

    default void reset() {}

    void update(TPProgram prog);

    default String[] getInfoLines() {
        return new String[0];
    }

}
