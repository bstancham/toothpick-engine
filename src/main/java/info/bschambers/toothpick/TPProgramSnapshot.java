package info.bschambers.toothpick;

import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPPlayer;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class TPProgramSnapshot {

    // ENVIRONMENT
    public String title = "";
    public TPGeometry geom = new TPGeometry();
    public Color bgColor = Color.BLACK;
    public Image bgImage = null;
    public boolean smearMode = false;
    public boolean rescueChildActors = true;
    public boolean showProgramInfo = true;
    
    // ACTORS
    public List<TPActor> actors = new ArrayList<>();
    public List<TPPlayer> players = new ArrayList<>();

    // BEHAVIOURS
    public List<ProgramBehaviour> behaviours = new ArrayList<>();
    public List<ProgramBehaviour> resetBehaviours = new ArrayList<>();
    
    // DEBUGGING
    public boolean showDiagnosticInfo = true;
    protected boolean keepIntersectionPoints = false;
    protected boolean showBoundingBoxes = false;

}
