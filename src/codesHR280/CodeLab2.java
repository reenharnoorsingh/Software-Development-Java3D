package codesHR280;

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

import java.awt.*;

public class CodeLab2 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D lineShape(int n) { //n as index
        Color3f[] colors = CommonsHR.Clrs;//Adding the colors array from Commons
        int colorSize = colors.length;//colorSize set to the total length of color array
        Shape3D shape = new Shape3D();//new 3d shape
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
        int count = 0, colorC = 1;
        for (int i = 0; i < n - 1; i++) {
            TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);//creating new triangle array
            triangle.setCoordinate(0, coor[count++]);//first coordinate as first vertex
            triangle.setCoordinate(1, coor[count]);//second vertex
            triangle.setCoordinate(2, new Point3f(0, 0, 0));//setting third vertex as 0,0,0
            triangle.setColor(0, colors[colorC++]);//setting the color of first the vertex
            if (colorC == colorSize)//reset index if true
                colorC = 1;
            triangle.setColor(1, colors[colorC]);//second vertex color
            triangle.setColor(2, CommonsHR.Blue);//third vertex as blue
            shape.addGeometry(triangle);
        }
        TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);//new triangle array for missing triangle
        triangle.setCoordinate(0, coor[n - 1]);//vertex 1
        triangle.setCoordinate(1, coor[0]);//vertex 2`
        triangle.setCoordinate(2, new Point3f(0, 0, 0));//vertex 3 0,0,0
        triangle.setColor(0, colors[colorC++]);//setting colors
        triangle.setColor(1, colors[1]);
        triangle.setColor(2, CommonsHR.Blue);
        shape.addGeometry(triangle);
        return shape;
    }

    public static TransformGroup letters3D(int n, double s) { //referred from J2-Geometry Slides
        Font my2DFont = new Font("Arial", Font.PLAIN, 1);//creating font
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, "Polygon" + n);//add text to the centre
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        Transform3D scalar = new Transform3D();
        scalar.setScale(0.1);
        TransformGroup text = new TransformGroup(scalar);
        text.addChild(new Shape3D(text3D));
        return text;
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        int n = 60;//number of sides
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, sceneTG));
        sceneTG.addChild(letters3D(n, 5));                   //adds the Polygon message
        sceneTG.addChild(lineShape(n));
        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsHR.MyGUI(createScene(), "HR's Lab 2");
            }
        });
    }
}
