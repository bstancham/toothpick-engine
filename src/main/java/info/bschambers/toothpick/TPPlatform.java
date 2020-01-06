package info.bschambers.toothpick;

public class TPPlatform {

    private TPProgram prog;
    private boolean progChanged = true;

    public TPPlatform() {
        setProgram(TPProgram.NULL);
    }

    public TPPlatform(TPProgram prog) {
        setProgram(prog);
    }

    public TPProgram getProgram() {
        return prog;
    }

    public boolean programChanged() {
        return progChanged;
    }

    public void setProgramChanged(boolean val) {
        progChanged = val;
    }

    protected void setProgram(TPProgram prog) {
        this.prog = prog;
        progChanged = true;
    }

    public void update() {
        prog.update();
    }

}
