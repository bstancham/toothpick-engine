package info.bschambers.toothpick;

public interface ProgramBehaviour {

    default String[] getInfoLines() {
        return new String[0];
    }

    void update(TPProgram prog);

}
