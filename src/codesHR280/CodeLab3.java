package codesHR280;


import codesHR280.CommonsHR;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import javax.swing.*;
import javax.xml.crypto.dsig.Transform;
import java.awt.*;

public class CodeLab3 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static BranchGroup shape(int n) {

        BranchGroup branchGroup = new BranchGroup();
        TransformGroup transformGroup = new TransformGroup();
        if (n < 3)// if n<3 then just return a ColorCube
        {
            transformGroup.addChild(new ColorCube(0.35));
            branchGroup.addChild(transformGroup);
            branchGroup.addChild(CommonsHR.rotateBehavior(10000, transformGroup));
            return branchGroup;
        }
        Transform3D rotate = new Transform3D();
        rotate.rotX(3 * Math.PI / 2);
        transformGroup.setTransform(rotate);
        branchGroup.addChild(transformGroup);

        Color3f[] colors = CommonsHR.Clrs;// array of colors
        int color_size = colors.length;   // length of the color array
        Shape3D shape = new Shape3D();  // new Shape3D object to add multiple triangles to
        float r = 0.6f, x, y;           // vertex at 0.06 away from origin
        Point3f[] coor = new Point3f[n];//Point Array to assign the points to the TriangleArray later
        Point3f[] coor2 = new Point3f[n];// another array for the triangles of the base
        Vector3f[] vec = new Vector3f[n];// a vector array to position the pillars
        for (int i = 0; i <= n - 1; i++) {
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r;//changing the previous value 72 to 360/n as instructed in lectures to create proper angle
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;//changing the previous value 72 to 360/n as instructed in lectures to create proper angle
            coor[i] = new Point3f(x, y, 0.5f);// adding the coordinates to the array
            coor2[i] = new Point3f(x, y, 0.5f);
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * 0.5f;//changing from r to 0.5 to make it fit inside the polygon
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * 0.5f;
            vec[i] = new Vector3f(x, y, 0);//add the same coordinate with different z for the pillars
        }
        int count = 0, colorC = 1, count1 = 0, count2 = 0;//count to keep track of coordinates and colorC to keep track of color array index
        for (int i = 0; i < n - 1; i++) {
            TransformGroup tg1 = new TransformGroup();//tg for the pillars
            transformGroup.addChild(tg1);//adding the tg
            Transform3D transform = new Transform3D();
            Vector3f vector = vec[i];//setting the position of pillars
            transform.setTranslation(vector);//repositioning
            Transform3D rotator = new Transform3D();
            rotator.rotX(Math.PI / 2);//rotating
            Transform3D trfm = new Transform3D();
            trfm.mul(transform);//repositioning the pillars
            trfm.mul(rotator);//adding rotation to the pillars
            tg1.setTransform(trfm);//transforming the pillars
            TriangleArray back = new TriangleArray(3, TriangleArray.COORDINATES);
            back.setCoordinate(0, coor2[count1++]);//setting the first coordinate as the first vertex then updating index
            back.setCoordinate(2, coor2[count1]); // setting the secont vertex but not updating since the next triangle shares the vertex
            back.setCoordinate(1, new Point3f(0, 0, 0.5f));//the third vertex is always 0,0,0
            TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);//creating new TriangleArray
            triangle.setCoordinate(0, coor[count++]);//setting the first coordinate as the first vertex then updating index
            triangle.setCoordinate(1, coor[count]); // setting the secont vertex but not updating since the next triangle shares the vertex
            triangle.setCoordinate(2, new Point3f(0, 0, 0.65f));//the third vertex is always 0,0,0
            triangle.setColor(0, colors[colorC++]);//seting vertex color as the colors[colorC] then updating colorC
            if (colorC == color_size)//if index is more than length then reset index
                colorC = 1;
            triangle.setColor(1, colors[colorC]);//setting the second vertex color
            Appearance app = new Appearance();//setting the color of pillars
            app.setMaterial(new Material(colors[colorC - 1], colors[colorC - 1], colors[colorC - 1], colors[colorC - 1], 1));
            Cylinder cylinder = new Cylinder(0.04f, 1f, app);//creating new pillars
            tg1.addChild(cylinder);//adding the pillars
            triangle.setColor(2, CommonsHR.Blue);//the center has to be blue so third vertex is always blue
            shape.addGeometry(triangle);//add the triangle to the shape
            shape.addGeometry(back);//adding the base of the polygon
        }

        TransformGroup tg1 = new TransformGroup();//tg of pillar
        transformGroup.addChild(tg1);
        Transform3D transform = new Transform3D();
        Vector3f vector = vec[n - 1];
        transform.setTranslation(vector);//changing the position
        Transform3D rotator = new Transform3D();
        rotator.rotX(Math.PI / 2);//adding rotation
        Transform3D trfm = new Transform3D();
        trfm.mul(transform);//changing the position of the pillar
        trfm.mul(rotator);//rotating pillar to be under the polygon
        tg1.setTransform(trfm);//transforming the pillar
        TriangleArray back = new TriangleArray(3, TriangleArray.COORDINATES);
        back.setCoordinate(0, coor2[n - 1]);//setting the first coordinate as the first vertex then updating index
        back.setCoordinate(2, coor2[0]); // setting the secont vertex but not updating since the next triangle shares the vertex
        back.setCoordinate(1, new Point3f(0, 0, 0.5f));//the third vertex is always 0,0,0
        TriangleArray triangle = new TriangleArray(3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);// this is the same code as the loop but for the last case where we have to
        // set the second vertex as the 0th coordinate and also set the color to be the same as that of first triangle
        triangle.setCoordinate(0, coor[n - 1]);//set vertex 1
        triangle.setCoordinate(1, coor[0]);//set vertex 2
        triangle.setCoordinate(2, new Point3f(0, 0, 0.65f));//third vertex always 0
        triangle.setColor(0, colors[colorC++]);//setting colors
        Appearance app = new Appearance();//creating new appearance according to the color
        app.setMaterial(new Material(colors[colorC - 1], colors[colorC - 1], colors[colorC - 1], colors[colorC - 1], 1));
        Cylinder cylinder = new Cylinder(0.04f, 1f, app);//create a new ctlinder
        tg1.addChild(cylinder);//adding the pillar to the transformgroup
        triangle.setColor(1, colors[1]);
        triangle.setColor(2, CommonsHR.Blue);
        shape.addGeometry(back);
        shape.addGeometry(triangle);
        transformGroup.addChild(shape);

        return branchGroup;//return the branch group
    }

    public static BranchGroup createScene() {

        BranchGroup sceneBG = shape(6);// Create branch group and add the 3D shapes using the function shape
        sceneBG.compile();// optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(1.35, 1.35, 2.0));
                new CommonsHR.MyGUI(createScene(), "HR's Lab3");
            }
        });
    }
}
