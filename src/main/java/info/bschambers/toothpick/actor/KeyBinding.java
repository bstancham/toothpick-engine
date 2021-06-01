package info.bschambers.toothpick.actor;

public class KeyBinding {

    /** An identifying label for this key-binding. */
    public final String label;

    private int keyCode = 0;
    private boolean value = false;

    public KeyBinding(String label, int keyCode) {
        this.label = label;
        this.keyCode = keyCode;
    }

    public KeyBinding copy() {
        KeyBinding b = new KeyBinding(label, keyCode);
        b.value = value;
        return b;
    }

    /** Gets the key-code. */
    public int code() { return keyCode; }

    /** Sets the key-code. */
    public void setCode(int val) { keyCode = val; }

    public boolean value() { return value; }

    public void setValue(boolean val) { value = val; }
}
