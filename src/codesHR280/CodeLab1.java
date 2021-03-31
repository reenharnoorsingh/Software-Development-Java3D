package codesHR280;
/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/
/* *********************************************************
 * Changes made by Harnoor Singh Reen, 110006294
 * Contact: reen@uwindsor.ca
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

public class CodeLab1 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D lineShape(int n) { //n as index
        float r = 0.6f, x, y;// vertex at 0.06 away from origin
        if (n < 3) {
            ColorCube cube = new ColorCube(0.35);//creating new cube
            return cube;
        }
        Point3f[] coor = new Point3f[n];
        LineArray lineArr = new LineArray(500, LineArray.COLOR_3 | LineArray.COORDINATES);//changed vertex count to 500
        for (int i = 0; i <= n - 1; i++) {
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r;//dividing 360 with the number of sides
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;
            coor[i] = new Point3f(x, y, 0.0f);
        }
        for (int i = 0; i <= n - 2; i++) {
            lineArr.setCoordinate(i * 2, coor[i]);
            lineArr.setCoordinate(i * 2 + 1, coor[i + 1]);
            lineArr.setColor(i * 2, CommonsHR.Red);
            lineArr.setColor(i * 2 + 1, CommonsHR.Green);
        }
        //adding the missing line from the above loop in the polygon
        lineArr.setCoordinate((n - 1) * 2, coor[n - 1]);
        lineArr.setCoordinate((n - 1) * 2 + 1, coor[0]);
        lineArr.setColor((n - 1) * 2 * 2, CommonsHR.Red);
        lineArr.setColor((n - 1) * 2 + 1, CommonsHR.Green);

        return new Shape3D(lineArr);
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        sceneTG.addChild(lineShape(5));

        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsHR.MyGUI(createScene(), "HR's Lab 1");
            }
        });
    }
}

