package codesHR280;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

import javax.swing.*;
import java.awt.*;

public class CodeLab6 extends JPanel {
    private static final long serialVersionUID = 1L;

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

    public static Material setMaterial() {
        int i = 256;
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        material.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        material.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        material.setShininess(i);
        material.setLightingEnable(true);
        return material;
    }

    public static void addLighting(TransformGroup scene_TG) {
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0));
        scene_TG.addChild(ambientLight);
        Point3f pt = new Point3f(2.0f, 2.0f, 2.0f);
        Point3f at = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight ptLight = new PointLight(CommonsHR.White, pt, at);
        ptLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0));
        scene_TG.addChild(ptLight);
    }

    public static TransformGroup addText(String text, double scale, Point3f point, Color3f color, Double rotation, Boolean ind) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1);//creating the 2d font
        Color3f clr;
        if (color == CommonsHR.Cyan)
            clr = CommonsHR.Yellow;
        else clr = CommonsHR.Blue;
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);// converting 2d font to 3d font
        Text3D text3D = new Text3D(font3D, text);//making a text with that 3d font
        text3D.setPosition(point);//position the text according to the parameter
        Appearance app = new Appearance();
        app.setMaterial(new Material(clr, clr, clr, clr, 1));//set the appearance according to parameter
        Transform3D scalar = new Transform3D();//scaling the text to fit to the other shape
        Transform3D rotate = new Transform3D();
        scalar.setScale(scale);//scale according to the parameter
        if (!ind)
            rotate.rotY(rotation);
        else rotate.rotX(rotation);
        Transform3D t = new Transform3D();
        t.mul(scalar);
        t.mul(rotate);
        TransformGroup textTransform = new TransformGroup(t);//making a transform group
        textTransform.addChild(new Shape3D(text3D, app));//adding the 3d text to the transform group
        return textTransform; // return the Transform Group
    }

    private static TransformGroup drawRectangles(Color3f color, Point3f[] pArray, Vector3f[] nArray, String s, Double rot, Boolean ind) {
        TransformGroup tg = new TransformGroup();
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(new Color3f(0.2f, 0.5f, 0.9f), ColoringAttributes.NICEST);
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setColoringAttributes(ca);
        app.setPolygonAttributes(pa);
        app.setMaterial(setMaterial());
        QuadArray quadArray = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3 | QuadArray.NORMALS);
        quadArray.setCoordinates(0, pArray);//setting the coordinate of the rectangle
        for (int i = 0; i < 4; i++)
            quadArray.setColor(i, color);//setting the color of the vertexes
        quadArray.setNormals(0, nArray);
        tg.addChild(new Shape3D(quadArray, app));
        tg.addChild(addText(s, 0.1d, new Point3f(-1.2f, -0.2f, 4), color, rot, ind));
        return tg;
    }

    private static TransformGroup drawCube() {
        TransformGroup transformGroup = new TransformGroup();
        Point3f[] top = new Point3f[4];//array to store the coordinates of the 4 sides

        Vector3f[] sideNormal1 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal1[i] = new Vector3f();
        sideNormal1[0].y = 1.0f;
        sideNormal1[1].y = 1.0f;
        sideNormal1[2].y = 1.0f;
        sideNormal1[3].y = 1.0f;
        top[0] = new Point3f(-0.4f, 0.4f, -0.4f);//setting the first coordinate
        top[2] = new Point3f(0.4f, 0.4f, 0.4f);//setting the second coordinate
        top[3] = new Point3f(0.4f, 0.4f, -0.4f);//setting the third coordinate
        top[1] = new Point3f(-0.4f, 0.4f, 0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Blue, top, sideNormal1, "Top", Math.PI / 2, true));

        Vector3f[] sideNormal2 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal2[i] = new Vector3f();
        sideNormal2[0].y = -1.0f;
        sideNormal2[1].y = -1.0f;
        sideNormal2[2].y = -1.0f;
        sideNormal2[3].y = -1.0f;
        Point3f[] bottom = new Point3f[4];//array to store the coordinates of the 4 sides
        bottom[0] = new Point3f(-0.4f, -0.4f, -0.4f);//setting the first coordinate
        bottom[2] = new Point3f(0.4f, -0.4f, 0.4f);//setting the second coordinate
        bottom[1] = new Point3f(0.4f, -0.4f, -0.4f);//setting the third coordinate
        bottom[3] = new Point3f(-0.4f, -0.4f, 0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Magenta, bottom, sideNormal2, "Bottom", Math.PI / 2, true));

        Vector3f[] sideNormal3 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal3[i] = new Vector3f();
        sideNormal3[0].z = 1.0f;
        sideNormal3[1].z = 1.0f;
        sideNormal3[2].z = 1.0f;
        sideNormal3[3].z = 1.0f;
        Point3f[] front = new Point3f[4];//array to store the coordinates of the 4 sides
        front[0] = new Point3f(-0.4f, -0.4f, 0.4f);//setting the first coordinate
        front[1] = new Point3f(0.4f, -0.4f, 0.4f);//setting the second coordinate
        front[2] = new Point3f(0.4f, 0.4f, 0.4f);//setting the third coordinate
        front[3] = new Point3f(-0.4f, 0.4f, 0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Red, front, sideNormal3, "Front", Math.PI * 2, false));


        Vector3f[] sideNormal4 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal4[i] = new Vector3f();
        sideNormal4[0].z = -1.0f;
        sideNormal4[1].z = -1.0f;
        sideNormal4[2].z = -1.0f;
        sideNormal4[3].z = -1.0f;
        Point3f[] back = new Point3f[4];//array to store the coordinates of the 4 sides
        back[0] = new Point3f(-0.4f, -0.4f, -0.4f);//setting the first coordinate
        back[3] = new Point3f(0.4f, -0.4f, -0.4f);//setting the second coordinate
        back[2] = new Point3f(0.4f, 0.4f, -0.4f);//setting the third coordinate
        back[1] = new Point3f(-0.4f, 0.4f, -0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Cyan, back, sideNormal4, "Back", Math.PI, false));

        Vector3f[] sideNormal5 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal5[i] = new Vector3f();
        sideNormal5[0].x = -1.0f;
        sideNormal5[1].x = -1.0f;
        sideNormal5[2].x = -1.0f;
        sideNormal5[3].x = -1.0f;
        Point3f[] left = new Point3f[4];//array to store the coordinates of the 4 sides
        left[0] = new Point3f(-0.4f, -0.4f, -0.4f);//setting the first coordinate
        left[1] = new Point3f(-0.4f, -0.4f, 0.4f);//setting the second coordinate
        left[2] = new Point3f(-0.4f, 0.4f, 0.4f);//setting the third coordinate
        left[3] = new Point3f(-0.4f, 0.4f, -0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Yellow, left, sideNormal5, "Left", (Math.PI / 2) * 3, false));

        Vector3f[] sideNormal6 = new Vector3f[4];//array to store the normals of the 4 sides
        for (int i = 0; i < 4; i++) sideNormal6[i] = new Vector3f();
        sideNormal6[0].x = 1.0f;
        sideNormal6[1].x = 1.0f;
        sideNormal6[2].x = 1.0f;
        sideNormal6[3].x = 1.0f;
        Point3f[] right = new Point3f[4];//array to store the coordinates of the 4 sides
        right[0] = new Point3f(0.4f, -0.4f, -0.4f);//setting the first coordinate
        right[3] = new Point3f(0.4f, -0.4f, 0.4f);//setting the second coordinate
        right[2] = new Point3f(0.4f, 0.4f, 0.4f);//setting the third coordinate
        right[1] = new Point3f(0.4f, 0.4f, -0.4f);//setting the fourth coordinate
        transformGroup.addChild(drawRectangles(CommonsHR.Green, right, sideNormal6, "Right", Math.PI / 2, false));
        return transformGroup;
    }

    public static BranchGroup createScene() {

        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        TransformGroup sceneTG3 = new TransformGroup();       // create a TransformGroup (TG)
        TransformGroup sceneTG2 = drawCube();
        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        sceneBG.addChild(sceneTG3);                             // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, sceneTG));
        sceneTG3.addChild(drawAxis(CommonsHR.Blue, 0.5f));// This adds the axis
        sceneTG.addChild(sceneTG2);
        addLighting(sceneTG3);
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(0.05, 0.35, 3));
                new CommonsHR.MyGUI(createScene(), "HR's Lab6");
            }
        });
    }
}
