package info.bschambers.toothpick;

import java.util.function.Consumer;

/**
 * <p>A miscellaneous {@link ProgramBehaviour} - pass the update-function into the
 * constructor.</p>
 *
 * <p>WARNING: {@link copy} returns {@code this}, therefore need to be careful to keep the
 * update-function strictly free of side effects.</p>
 */
public class PBMisc implements ProgramBehaviour {

    private Consumer<TPProgram> updateFunc;

    public PBMisc(Consumer<TPProgram> updateFunc) {
        this.updateFunc = updateFunc;
    }

    @Override
    public PBMisc copy() { return this; }

    @Override
    public void update(TPProgram prog) {
        updateFunc.accept(prog);
    }

}
