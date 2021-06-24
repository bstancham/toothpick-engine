package info.bstancham.toothpick;

import java.util.function.Consumer;

/**
 * <p>A miscellaneous {@link ProgramBehaviour} - pass the update-function into the
 * constructor.</p>
 *
 * <p>WARNING: {@link copy} returns {@code this}, therefore need to be careful to keep the
 * update-function strictly free of side effects.</p>
 */
public class ProgMisc implements ProgramBehaviour {

    private Consumer<TPProgram> updateFunc;

    public ProgMisc(Consumer<TPProgram> updateFunc) {
        this.updateFunc = updateFunc;
    }

    @Override
    public ProgMisc copy() { return this; }

    @Override
    public void update(TPProgram prog) {
        updateFunc.accept(prog);
    }

}
