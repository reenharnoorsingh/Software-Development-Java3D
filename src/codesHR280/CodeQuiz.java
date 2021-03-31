package codesHR280;
/* *********************************************************
 * For use by students to work on lab quiz.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/
// Harnoor Singh Reen - 110006294

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class CodeQuiz extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int n = 4;                                // set number of corner points
    private static final float pt = 0.6f;                          // set corner points
    private static final Point3f[] pts = {new Point3f(pt, 0f, 0f), new Point3f(0f, 0f, -pt),
            new Point3f(-pt, 0f, 0f), new Point3f(0f, 0f, pt)};

    /* a function to create an n-side polygonal shape in specific size, color, and line style */
    private static Shape3D lineShape(Color3f c, int w, int p, boolean g) {
        int[] strip = {n + 1};                               // add one point to close the polygon
        IndexedLineStripArray ilsa = new IndexedLineStripArray(n + 1,
                GeometryArray.COLOR_3 | GeometryArray.COORDINATES, n + 1, strip);

        int[] pntIndices = new int[n];                       // the strip has 'n' indices
        Color3f[] color = {c};                               // use only one color
        int[] clrIndices = new int[n];                       // one strip has 'n' indices
        for (int i = 0; i < n; i++) {
            pntIndices[i] = i;                               // set point index
            clrIndices[i] = 0;                               // fix color index
        }

        ilsa.setColors(0, color);                            // set color for points
        ilsa.setColorIndices(0, clrIndices);
        ilsa.setCoordinates(0, pts);                         // link points with indices
        ilsa.setCoordinateIndices(0, pntIndices);

        Appearance app = new Appearance();
        LineAttributes lineAtt = new LineAttributes(w, p, g);
        app.setLineAttributes(lineAtt);

        return new Shape3D(ilsa, app);                       // return the Shape3D
    }

    //function that adds the green square
    private static Shape3D addGreenSquare() {
        Appearance app = new Appearance();
        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(polygonAttributes);
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f);
        app.setTransparencyAttributes(transparencyAttributes);
        QuadArray planar = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3);
        planar.setCoordinates(0, pts);
        for (int i = 0; i < 4; i++) {
            planar.setColor(i, Commons.Green);//adds the green color
        }
        return new Shape3D(planar, app);
    }

    //function that makes the shape translating along the y axis / single transform alpha
    private static PositionInterpolator positionInterpolator(TransformGroup tg) {
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 0, 0, 5000, 0, 0);
        Transform3D axisPosition = new Transform3D();
        axisPosition.rotZ(Math.PI / 2.0);
        PositionInterpolator pi = new PositionInterpolator(alpha, tg, axisPosition, -0.5f, 0.5f);
        pi.setSchedulingBounds(new BoundingSphere());
        return pi;
    }

    //function that makes the positionInterpolator rotate/ single transform alpha
    private static RotationInterpolator rotationInterpolator(TransformGroup tg) {
        Alpha alpha = new Alpha(-1, 5000);
        Transform3D axisPosition = new Transform3D();
        axisPosition.rotX(-Math.PI / 2.0);
        RotationInterpolator ri = new RotationInterpolator(alpha, tg, axisPosition, 0, 3.14159f);
        ri.setSchedulingBounds(new BoundingSphere());
        return ri;
    }

    //function that adds the axis
    private static Shape3D drawAxis(Color3f yColor, float length) {
        LineArray axes = new LineArray(6, LineArray.COLOR_3 | LineArray.COORDINATES);
        axes.setCoordinate(0, new Point3f(length, 0, 0));//setting the first index to the point on the x axis to make axis of length "length"
        axes.setColor(0, CommonsHR.Green);//coloring x axis as green
        axes.setCoordinate(1, new Point3f(0, 0, 0));
        axes.setColor(1, CommonsHR.Green);
        axes.setCoordinate(2, new Point3f(0, length, 0));//setting the first index to the point on the y axis to make axis of length "length"
        axes.setColor(2, yColor);//setting the color of y axis as yColor
        axes.setCoordinate(3, new Point3f(0, 0, 0));
        axes.setColor(3, yColor);
        axes.setCoordinate(4, new Point3f(0, 0, length));//setting the first index to the point on the z axis to make axis of length "length"
        axes.setColor(4, CommonsHR.Red);//setting the color of z axis as red
        axes.setCoordinate(5, new Point3f(0, 0, 0));
        axes.setColor(5, CommonsHR.Red);
        return new Shape3D(axes);//returning the axes
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'scene' as content branch

        TransformGroup tg = new TransformGroup();
        TransformGroup tg2 = new TransformGroup();
        TransformGroup tg3 = new TransformGroup();
        tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        PositionInterpolator p = positionInterpolator(tg);
        RotationInterpolator r = rotationInterpolator(tg2);

        Shape3D sq = addGreenSquare();
        Shape3D shape3D = lineShape(CommonsXY.Red, 3, LineAttributes.PATTERN_SOLID, true);
        tg.addChild(shape3D);                           // attach the shape to 'sceneBG'
        tg2.addChild(sq);
        tg3.addChild(drawAxis(Commons.Yellow, 0.5f));
        tg.addChild(tg2);
        //tg.addChild(tg3);
        tg.addChild(r);

        sceneBG.addChild(tg);
        sceneBG.addChild(tg3);
        sceneBG.addChild(p);
        sceneBG.compile();                                   // optimize 'sceneBG'

        return sceneBG;
    }

    /* the main entrance of the application with specified window dimension */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsXY.setEye(new Point3d(1.35, 0.75, 2.0));
                new CommonsXY.MyGUI(createScene(), "HR's Lab Quiz");
            }
        });
    }
}

