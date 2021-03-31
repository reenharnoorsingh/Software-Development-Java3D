package codesHR280;


import java.util.Iterator;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class MakeColorCubeTransparent extends Behavior {
    private final Shape3D shape;
    private final TransparencyAttributes transparencyAttributes;
    private final Appearance shapeAppearance;
    private boolean inCollision;
    private ColoringAttributes shapeColoring;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public MakeColorCubeTransparent(Shape3D s) {
        shape = s; // save the original color of 'shape"
        shapeAppearance = shape.getAppearance();
        transparencyAttributes = shapeAppearance.getTransparencyAttributes();
        inCollision = false;
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.5f);
        TransparencyAttributes newTransparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0);
        inCollision = !inCollision; // collision has taken place
        if (inCollision) { // change color to highlight 'shape'
            shapeAppearance.setTransparencyAttributes(transparencyAttributes);
            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            shapeAppearance.setTransparencyAttributes(newTransparencyAttributes);
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}