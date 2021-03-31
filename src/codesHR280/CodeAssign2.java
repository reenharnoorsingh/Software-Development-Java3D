package codesHR280;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

import javax.swing.*;

public class CodeAssign2 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D axis(Color3f yColor, float length) { // function which creates x,y and z axis
        LineArray axisLines = new LineArray(6, LineArray.COLOR_3 | LineArray.COORDINATES);
        axisLines.setCoordinate(0, new Point3f(length, 0, 0));//setting up the x-axis
        axisLines.setColor(0, CommonsHR.Green);//setting the color of the x-axis line as green
        axisLines.setCoordinate(1, new Point3f(0, 0, 0));
        axisLines.setColor(1, CommonsHR.Green);
        axisLines.setCoordinate(2, new Point3f(0, length, 0));//setting up the y-axis
        axisLines.setColor(2, yColor);//setting the color of y-axis as yColor
        axisLines.setCoordinate(3, new Point3f(0, 0, 0));
        axisLines.setColor(3, yColor);
        axisLines.setCoordinate(4, new Point3f(0, 0, length));//setting up the z-axis
        axisLines.setColor(4, CommonsHR.Red);//setting the color of z-axis as red
        axisLines.setCoordinate(5, new Point3f(0, 0, 0));
        axisLines.setColor(5, CommonsHR.Red);

        return new Shape3D(axisLines);//returning the axisLines in Shape3D
    }

    private static Texture imageLoader(String s) { //function for loading the image for the shape
        String filename = "images/"+s;//change this for the image
        TextureLoader loader = new TextureLoader(filename, null);//loading the texture
        ImageComponent2D image = loader.getImage();//image component
        if (image == null) {
            System.out.println("Error Loading the Image");//error message
        }
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());//creating the textures for the shapes
        texture.setImage(0, image);
        return texture;//return the texture
    }

    private static Shape3D settingBackground() { //function for creating the background
        Appearance app = new Appearance();
        app.setTexture(imageLoader("bga2.jpg"));//loading the background image

        QuadArray quadArray = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3 | QuadArray.TEXTURE_COORDINATE_2);
        Point3f[] imgCoordinate = new Point3f[4];//array to store the coordinates of the 4 sides
        imgCoordinate[0] = new Point3f(-15, -15, -17.0f);//first coordinate
        imgCoordinate[1] = new Point3f(15, -15, -17.0f);//second coordinate
        imgCoordinate[2] = new Point3f(15f, 15f, -17.0f);//third coordinate
        imgCoordinate[3] = new Point3f(-15f, 15f, -17.0f);//fourth coordinate

        quadArray.setCoordinates(0, imgCoordinate);//setting the coordinates= of the rectangle

        float[] tex1 = {-5, 0};//array for texture of coordinate 1
        float[] tex2 = {0, -5};//array for texture of coordinate 2
        float[] tex3 = {5, 0};//array for texture of coordinate 3
        float[] tex4 = {0, -5};//array for texture of coordinate 4
        quadArray.setTextureCoordinate(0, 0, tex1);//setting textured coordinate
        quadArray.setTextureCoordinate(0, 1, tex2);//setting textured coordinate
        quadArray.setTextureCoordinate(0, 2, tex3);//setting textured coordinate
        quadArray.setTextureCoordinate(0, 3, tex4);//setting textured coordinate

        return new Shape3D(quadArray, app);//returns the background to the shape
    }

    private static Shape3D setupBase(Color3f color, Point3f[] pointArray) {//function to setup the rectangles for the base structure
        QuadArray quadArray = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3);
        quadArray.setCoordinates(0, pointArray);//setting the coordinate of the rectangle
        for (int i = 0; i < 4; i++)
            quadArray.setColor(i, color);//setting the color of the vertexes

        return new Shape3D(quadArray);//return the new shape
    }

    private static BranchGroup pavilion(int n) {//creates the pavilion or the color cube

        BranchGroup branchGroup = new BranchGroup();//creating a new branch group
        TransformGroup transformGroup = new TransformGroup();// creating a new transform group

        if (n < 3)// if n<3 return ColorCube
        {
            transformGroup.addChild(new ColorCube(0.35));
            branchGroup.addChild(transformGroup);
            branchGroup.addChild(CommonsHR.rotateBehavior(10000, transformGroup));//adding rotation

            return branchGroup;
        }

        Transform3D rotate = new Transform3D();
        rotate.rotX(3 * Math.PI / 2);//making the transform group vertical
        branchGroup.addChild(transformGroup);//adding transformation to transform group
        transformGroup.setTransform(rotate);//setting it to rotation

        Appearance appearance = new Appearance();//setting the color of pillars
        appearance.setTexture(imageLoader("MarbleTexture.jpg"));//adding the image

        float r = 0.6f, x, y;           // vertex at 0.06 away from origin
        Point3f[] tArrayCoordinate = new Point3f[n];//assigns the points to the TriangleArray later
        Point3f[] tBaseCoordinate = new Point3f[n];// assigns the points to the the triangles of the base
        Vector3f[] pillarArray = new Vector3f[n];// a vector array to position the pillars

        for (int i = 0; i <= n - 1; i++) {
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r;// 360/n because 360 is the sum of all angles in a polygon
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;// ^^
            tArrayCoordinate[i] = new Point3f(x, y, 0.5f);// adding the triangle coordinates to the array
            tBaseCoordinate[i] = new Point3f(x, y, 0.5f); // adding the triangle base coordinates to the array

            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * 0.5f;//changing from r to 0.5 to make it fit inside the polygon
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * 0.5f;
            pillarArray[i] = new Vector3f(x, y, 0f);//adding the coordinates with different z-points for the pillars
        }

        int count = 0, counter = 0;//initializing some variables

        for (int i = 0; i < n - 1; i++) {

            TransformGroup tgPillar = new TransformGroup();//transform group for the pillars
            transformGroup.addChild(tgPillar);//adding the transform group

            Transform3D transform = new Transform3D();
            Vector3f vector = pillarArray[i];//setting the position of pillars
            transform.setTranslation(vector);//repositioning
            Transform3D rotator = new Transform3D();
            rotator.rotX(Math.PI / 2);//rotation
            Transform3D transform3D = new Transform3D();
            transform3D.mul(transform);//repositioning the pillars
            transform3D.mul(rotator);//adding rotation to the pillars
            tgPillar.setTransform(transform3D);//transforming the pillars

            TriangleArray basePyramid = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.TEXTURE_COORDINATE_2);// the base for pyramid
            basePyramid.setCoordinate(0, tBaseCoordinate[counter++]);//first coordinate as the first vertex and then incrementing it
            basePyramid.setCoordinate(2, tBaseCoordinate[counter]); //second vertex
            basePyramid.setCoordinate(1, new Point3f(0, 0, 0.5f));//third vertex is always 0,0,0
            float[] tester = {tArrayCoordinate[0].x, tArrayCoordinate[0].z, tArrayCoordinate[1].x, tArrayCoordinate[1].z, 0.0f, 0.0f};
            basePyramid.setTextureCoordinates(0, 0, tester);

            TriangleArray topPyramid = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.TEXTURE_COORDINATE_2);//creating new TriangleArray
            topPyramid.setCoordinate(0, tArrayCoordinate[count++]);
            topPyramid.setCoordinate(1, tArrayCoordinate[count]);
            topPyramid.setCoordinate(2, new Point3f(0, 0, 0.65f));
            topPyramid.setTextureCoordinates(0, 0, tester);

            Cylinder cylinder = new Cylinder(0.04f, 1f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, appearance);//creating pillars
            tgPillar.addChild(cylinder);//adding the pillars
            Shape3D pyramidTop = new Shape3D(topPyramid, appearance);//creating the pyramid shape
            transformGroup.addChild(pyramidTop);// adding the pyramid to the tg
            Shape3D pyramidBase = new Shape3D(basePyramid, appearance);
            transformGroup.addChild(pyramidBase);
        }
        TransformGroup tgPillar = new TransformGroup();//tg of pillar
        transformGroup.addChild(tgPillar);

        Transform3D transform = new Transform3D();
        Vector3f vector = pillarArray[n - 1];
        transform.setTranslation(vector);//changing the position
        Transform3D rotator = new Transform3D();
        rotator.rotX(Math.PI / 2);//adding rotation
        Transform3D transform3D = new Transform3D();
        transform3D.mul(transform);//changing the position of the pillar
        transform3D.mul(rotator);//rotating pillar to be under the polygon
        tgPillar.setTransform(transform3D);//transforming the pillar

        TriangleArray basePyramid = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.TEXTURE_COORDINATE_2);
        basePyramid.setCoordinate(0, tBaseCoordinate[n - 1]);
        basePyramid.setCoordinate(2, tBaseCoordinate[0]);
        basePyramid.setCoordinate(1, new Point3f(0, 0, 0.5f));

        TriangleArray topPyramid = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.TEXTURE_COORDINATE_2);// this is the same code as the loop but for the last case where we have to
        topPyramid.setCoordinate(0, tArrayCoordinate[n - 1]);//set vertex 1
        topPyramid.setCoordinate(1, tArrayCoordinate[0]);//set vertex 2
        topPyramid.setCoordinate(2, new Point3f(0, 0, 0.65f));

        float[] test = {tArrayCoordinate[0].x, tArrayCoordinate[0].z, tArrayCoordinate[1].x, tArrayCoordinate[1].z, 0.0f, 0.0f};
        basePyramid.setTextureCoordinates(0, 0, test);//set the texture coordinates for the base
        topPyramid.setTextureCoordinates(0, 0, test);//set the texture coordinates for the top part

        Cylinder cylinder = new Cylinder(0.04f, 1f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, appearance);//create a new ctlinder
        tgPillar.addChild(cylinder);//adding the pillar to the transform group
        Shape3D pyramidTop = new Shape3D(topPyramid, appearance);//creating the pyramid
        transformGroup.addChild(pyramidTop);//adding the pyramid to the tg
        Shape3D pyramidBase = new Shape3D(basePyramid, appearance);//creating the shape for base
        transformGroup.addChild(pyramidBase);//adding the base to TransformGroup
        return branchGroup;//return the branch group
    }

    private static TransformGroup baseRectangleShape() { //function that sets colors for the rectangular base
        TransformGroup transformGroup = new TransformGroup();

        Point3f[] front = new Point3f[4];//array to store the coordinates of the 4 sides front
        front[0] = new Point3f(-1, -0.04f, 1);//first coordinate
        front[1] = new Point3f(1, -0.04f, 1);//second coordinate
        front[2] = new Point3f(1, 0.04f, 1);//third coordinate
        front[3] = new Point3f(-1, 0.04f, 1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Red, front));

        Point3f[] back = new Point3f[4];//array to store the coordinates of the 4 sides back
        back[0] = new Point3f(-1, -0.04f, -1);//first coordinate
        back[3] = new Point3f(1, -0.04f, -1);//second coordinate
        back[2] = new Point3f(1, 0.04f, -1);//third coordinate
        back[1] = new Point3f(-1, 0.04f, -1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Cyan, back));

        Point3f[] top = new Point3f[4];//array to store the coordinates of the 4 sides of top
        top[0] = new Point3f(-1, 0.04f, -1);//first coordinate
        top[2] = new Point3f(1, 0.04f, 1);//second coordinate
        top[3] = new Point3f(1, 0.04f, -1);//third coordinate
        top[1] = new Point3f(-1, 0.04f, 1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Blue, top));

        Point3f[] bottom = new Point3f[4];//array to store the coordinates of the 4 sides bottom
        bottom[0] = new Point3f(-1, -0.04f, -1);// first coordinate
        bottom[2] = new Point3f(1, -0.04f, 1);//second coordinate
        bottom[1] = new Point3f(1, -0.04f, -1);//third coordinate
        bottom[3] = new Point3f(-1, -0.04f, 1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Magenta, bottom));

        Point3f[] left = new Point3f[4];//array to store the coordinates of the 4 sides left
        left[0] = new Point3f(-1, -0.04f, -1);//first coordinate
        left[1] = new Point3f(-1, -0.04f, 1);//second coordinate
        left[2] = new Point3f(-1, 0.04f, 1);//third coordinate
        left[3] = new Point3f(-1, 0.04f, -1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Yellow, left));

        Point3f[] right = new Point3f[4];//array to store the coordinates of the 4 sides right
        right[0] = new Point3f(1, -0.04f, -1);//first coordinate
        right[3] = new Point3f(1, -0.04f, 1);//second coordinate
        right[2] = new Point3f(1, 0.04f, 1);//third coordinate
        right[1] = new Point3f(1, 0.04f, -1);//fourth coordinate
        transformGroup.addChild(setupBase(CommonsHR.Green, right));

        return transformGroup;
    }

    public static BranchGroup createScene() {

        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        TransformGroup sceneTG3 = new TransformGroup();       // create a TransformGroup (TG3)
        TransformGroup sceneTG2 = baseRectangleShape();

        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        sceneBG.addChild(sceneTG3);                             // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, sceneTG));//adding rotation
        sceneTG.addChild(pavilion(10));//adding pavilion
        sceneTG3.addChild(axis(CommonsHR.Blue, 0.5f));// This adds the axis
        sceneTG3.addChild(settingBackground());// This adds the grey background

        Transform3D transform3D = new Transform3D(); //transformation for translation
        Transform3D transform3D2 = new Transform3D();//transformation for scaling
        Transform3D transform3D3 = new Transform3D();
        transform3D2.setScale(0.6);//scaling the base
        transform3D.setTranslation(new Vector3f(0, -0.50f - (0.04f * 0.6f), 0));//translating the base
        transform3D3.mul(transform3D);
        transform3D3.mul(transform3D2);
        sceneTG2.setTransform(transform3D3);
        sceneTG.addChild(sceneTG2);

        return sceneBG;
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(0.05, 0.35, 2.5));
                new CommonsHR.MyGUI(createScene(), "Harnoor Reen's Assignment 2");
            }
        });
    }
}