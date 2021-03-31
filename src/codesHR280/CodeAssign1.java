package codesHR280;

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

import java.awt.*;

public class CodeAssign1 extends JPanel {
    private static final long serialVersionUID = 1L;


    private static Shape3D f1(Color3f yColor, float length) {
        LineArray axes = new LineArray(500, LineArray.COLOR_3 | LineArray.COORDINATES);
        axes.setCoordinate(0, new Point3f(length, 0, 0));
        axes.setColor(0, CommonsHR.Green);//setting x-axis color as green
        axes.setCoordinate(1, new Point3f(0, 0, 0));
        axes.setColor(1, CommonsHR.Green);
        axes.setCoordinate(2, new Point3f(0, length, 0));
        axes.setColor(2, yColor);
        axes.setCoordinate(3, new Point3f(0, 0, 0));
        axes.setColor(3, yColor);
        axes.setCoordinate(4, new Point3f(0, 0, length));
        axes.setColor(4, CommonsHR.Red);
        axes.setCoordinate(5, new Point3f(0, 0, 0));
        axes.setColor(5, CommonsHR.Red);

        return new Shape3D(axes);
    }

    private static Shape3D f2(Color3f color, Point3f size, Vector2f scale) {
        QuadArray rect = new QuadArray(500, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        rect.setCoordinate(0, new Point3f(-2 * size.x * scale.x, -2 * size.y * scale.y, size.z));
        rect.setColor(0, color);
        rect.setCoordinate(1, new Point3f(2 * size.x * scale.x, -2 * size.y * scale.y, size.z));
        rect.setColor(1, color);
        rect.setCoordinate(2, new Point3f(2 * size.x * scale.x, 2 * size.y * scale.y, size.z));
        rect.setColor(2, color);
        rect.setCoordinate(3, new Point3f(-2 * size.x * scale.x, 2 * size.y * scale.y, size.z));
        rect.setColor(3, color);

        return new Shape3D(rect);
    }

    private static Shape3D f3(int n) {
        if (n < 3)// if n<3 then just return a ColorCube
            return new ColorCube(0.35);

        Color3f[] colors = CommonsHR.Clrs;// array of colors
        int color_size = colors.length;   // length of the color array
        Shape3D shape = new Shape3D();  // new Shape3D object to add multiple triangles to
        float r = 0.6f, x, y;                             // vertex at 0.06 away from origin

        Point3f[] coor = new Point3f[n];//Point Array to assign the points to the TriangleArray later
        Point3f[] coor2 = new Point3f[n];// Point array to assign the points to the base of the shape
        for (int i = 0; i <= n - 1; i++) {
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r;//changing the previous value 72 to 360/n as instructed in lectures to create proper angle
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;//changing the previous value 72 to 360/n as instructed in lectures to create proper angle
            coor[i] = new Point3f(x, y, 0.25f);// adding the coordinates to the array
            coor2[i] = new Point3f(x, y, 0.25f);//same point but for the base of the shape
        }
        int count = 0, colorC = 1, count1 = 0, count2 = 0;//count to keep track of coordinates and colorC to keep track of color array index
        for (int i = 0; i < n - 1; i++) {
            TriangleArray base = new TriangleArray(3, TriangleArray.COORDINATES);
            base.setCoordinate(0, coor2[count1++]);//setting the first coordinate as the first vertex then updating index
            base.setCoordinate(2, coor2[count1]); //changing the vertex order to make the shape visible at base
            base.setCoordinate(1, new Point3f(0, 0, 0.25f));//the third vertex is always 0,0,0.25
            TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);//creating new TriangleArray
            triangle.setCoordinate(0, coor[count++]);//setting the first coordinate as the first vertex then updating index
            triangle.setCoordinate(1, coor[count]); // setting the secont vertex but not updating since the next triangle shares the vertex
            triangle.setCoordinate(2, new Point3f(0, 0, 0.5f));//the third vertex is always 0,0,0.5
            triangle.setColor(0, colors[colorC++]);//seting vertex color as the colors[colorC] then updating colorC
            if (colorC == color_size)//if index is more than length then reset index
                colorC = 1;
            triangle.setColor(1, colors[colorC]);
            triangle.setColor(2, CommonsHR.Blue);
            shape.addGeometry(triangle);//add the triangle to the shape
            shape.addGeometry(base);
        }

        TriangleArray base = new TriangleArray(3, TriangleArray.COORDINATES);//Triangle array for the base
        base.setCoordinate(0, coor2[n - 1]);//setting the last vertex
        base.setCoordinate(2, coor2[0]); // changed the vertex to make it appear even when at the base
        base.setCoordinate(1, new Point3f(0, 0, 0.25f));//the third vertex is always 0,0,0.25
        TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);
        triangle.setCoordinate(0, coor[n - 1]);// vertex 1
        triangle.setCoordinate(1, coor[0]);// vertex 2
        triangle.setCoordinate(2, new Point3f(0, 0, 0.5f));//vertex 3 0,0,0
        triangle.setColor(0, colors[colorC++]);//setting colors
        triangle.setColor(1, colors[1]);
        triangle.setColor(2, CommonsHR.Blue);
        shape.addGeometry(base);//adding the base triangle to the shape
        shape.addGeometry(triangle);
        return shape;
    }

    private static TransformGroup f4(String txt, double scl, Point3f pnt, Color3f clr) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1);//creating font
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, txt);//add text to the centre
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        text3D.setPosition(pnt);
        Transform3D scalar = new Transform3D();
        scalar.setScale(scl);
        Appearance look = new Appearance();
        look.setMaterial(new Material(clr, clr, clr, clr, 1));
        TransformGroup text = new TransformGroup(scalar);
        text.addChild(new Shape3D(text3D, look));
        return text;
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        String str = "Java3D";
        sceneTG.addChild(f1(CommonsHR.Blue, 0.5f));
        sceneTG.addChild(f2(CommonsHR.Grey, new Point3f(1f, 0.8f, -0.25f), new Vector2f(0.6f, 0.6f)));
        sceneTG.addChild(f3(5));
        sceneTG.addChild(f4(str, 0.2d, new Point3f(-str.length() / 4f, 0, 0), CommonsHR.White));
        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsHR.MyGUI(createScene(), "HR's Assignment 1");
            }
        });
    }
}