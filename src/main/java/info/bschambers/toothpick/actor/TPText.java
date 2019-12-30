package info.bschambers.toothpick.actor;

import info.bschambers.toothpick.TPEncoding;

public class TPText extends TPPart {

    @Override
    public void update(double x, double y, double angle) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public TPText copy() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /*---------------------------- Encoding ----------------------------*/

    @Override
    public TPEncoding getEncoding() {
        TPEncoding params = new TPEncoding();
        return params;
    }

}
