package info.bschambers.toothpick;

public class TPPlatform {

    private String title = "";
    private TPProgram prog;
    private boolean progChanged = true;

    public TPPlatform(String title) {
        this.title = title;
        setProgram(TPProgram.NULL);
    }

    public TPPlatform(String title, TPProgram prog) {
        this.title = title;
        setProgram(prog);
    }

    public String getTitle() {
        return title;
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
