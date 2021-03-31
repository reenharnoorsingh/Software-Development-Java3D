package codesHR280;


import javax.swing.JPanel;

import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

public class CodeLab8 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Point3d pt_zero = new Point3d(0d, 0d, 0d);

    private static TransformGroup createColumn(double scale, Vector3d pos) {
        Transform3D transM = new Transform3D();
        transM.set(scale, pos);  // Create base TG with 'scale' and 'position'
        TransformGroup baseTG = new TransformGroup(transM);

        Shape3D shape = new Box(0.5, 5.0, 1.0);
        baseTG.addChild(shape); // Create a column as a box and add to 'baseTG'

        Appearance app = shape.getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(0.6f, 0.3f, 0.0f); // set column's color and make changeable
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);

        CollisionDetectColumns cd = new CollisionDetectColumns(shape);
        cd.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // detect column's collision

        baseTG.addChild(cd); // add column with behavior of CollisionDector
        return baseTG;
    }

    private static TransformGroup createBox() {
        TransformGroup[] transfmTG = new TransformGroup[2];
        for (int i = 0; i < 2; i++) {         // two TGs: 0-self and 1-orbit
            transfmTG[i] = new TransformGroup();
            transfmTG[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }

        Transform3D t = new Transform3D(); // define scale and position
        t.set(0.12f, new Vector3d(0.0, -0.6, 0.0));
        TransformGroup transCube = new TransformGroup(t);
        ColorCube colorCube = new ColorCube();
        transCube.addChild(colorCube);
        //added this part
        colorCube.setAppearance(new Appearance());
        Appearance colorCubeAppearance = colorCube.getAppearance();
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes();//transparency attributes introduced
        transparencyAttributes.setTransparency(1);
        colorCubeAppearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        colorCubeAppearance.setTransparencyAttributes(transparencyAttributes);
        MakeColorCubeTransparent transparent = new MakeColorCubeTransparent(colorCube);//new transparent color added to color cube
        transparent.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, -0.6, 0.0), 100));
        transCube.addChild(transparent);
        //end of part
        transfmTG[0].addChild(transCube);            // add a unit cube to 3rd TG

        Transform3D yAxis1 = new Transform3D();
        yAxis1.rotX(Math.PI / 2.0);                // define animation along orbit
        Alpha alphaOrbit = new Alpha(-1, Alpha.INCREASING_ENABLE |
                Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200, 5000, 2500, 200);
        RotationInterpolator tickTock = new RotationInterpolator(alphaOrbit,
                transfmTG[1], yAxis1, -(float) Math.PI / 2.0f, (float) Math.PI / 2.0f);
        tickTock.setSchedulingBounds(new BoundingSphere(pt_zero, 10d));
        transfmTG[1].addChild(tickTock);     // add orbit animation to scene graph

        Transform3D yAxis2 = new Transform3D();
        Alpha alphaSelf = new Alpha(-1, Alpha.INCREASING_ENABLE,
                0, 0, 4000, 0, 0, 0, 0, 0);   // define self-rotating animation
        RotationInterpolator rotatorSelf = new RotationInterpolator(alphaSelf,
                transfmTG[0], yAxis2, 0.0f, (float) Math.PI * 2.0f);
        rotatorSelf.setSchedulingBounds(new BoundingSphere(pt_zero, 10d));
        transfmTG[0].addChild(rotatorSelf);
        transfmTG[1].addChild(transfmTG[0]);      // add self-rotation to orbit

        return transfmTG[1];
    }


    private static void createTickTock(TransformGroup sceneTG) {
        Vector3d[] pos = {new Vector3d(-0.52, 0.0, 0.0),
                new Vector3d(0.52, 0.0, 0.0)};
        for (int i = 0; i < 2; i++)
            sceneTG.addChild(createColumn(0.12, pos[i]));
        sceneTG.addChild(createBox());
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup scene = new BranchGroup(); // create 'scene' as content branch
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        scene.addChild(sceneTG);                             // add TG to the scene BranchGroup
        scene.addChild(CommonsHR.rotateBehavior(10000, sceneTG));

        createTickTock(sceneTG);
        scene.compile(); // optimize scene BG

        return scene;
    }

    /* the main entrance of the application */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(1.35, -0.35, 2.0));
                new CommonsHR.MyGUI(createScene(), "HR's Lab8");
            }
        });
    }
}