package codesHR280;

import javax.swing.JPanel;
import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.AmbientLight;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ExponentialFog;
import org.jogamp.java3d.Fog;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.QuadArray;
import org.jogamp.java3d.RotationInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.TriangleArray;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.TexCoord2f;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class CodeAssign3 extends JPanel {
    private static final long serialVersionUID = 1L;

    ///Shapes and Structures
    private static BranchGroup pavilion(int n) {

        BranchGroup bg = new BranchGroup();

        if(n<3) {
            bg.addChild(new ColorCube(0.35));
            return bg;
        }
        else {

            float r = 0.6f, x,z;                             // vertex at 0.06 away from origin
            Point3f coor[] = new Point3f[n];
            Vector3f vect[] = new Vector3f[n];
            Cylinder cylinderArr[] = new Cylinder[n];

            Transform3D transform = new Transform3D();
            TransformGroup t = null;
            TriangleArray tri = new TriangleArray(n*3 , TriangleArray.COORDINATES | TriangleArray.COLOR_3 |GeometryArray.TEXTURE_COORDINATE_2);
            TriangleArray tri2 = new TriangleArray(n*3 , TriangleArray.COORDINATES | TriangleArray.COLOR_3 | GeometryArray.TEXTURE_COORDINATE_2);

            for (int i = 0; i < n; i++) {
                x = (float) Math.cos(Math.PI / 180 * (90 + (360)/n *i)) * r;		///360 divided by the number of sides
                z = (float) Math.sin(Math.PI / 180 * (90 + (360)/n  *i)) * r;
                coor[i] = new Point3f(x, 0.5f, z);
            }
            for (int i = 0; i < n; i++) {
                x = (float) Math.cos(Math.PI / 180 * (90 + (360)/n *i)) * (0.5f);		///360 divided by the number of sides
                z = (float) Math.sin(Math.PI / 180 * (90 + (360)/n  *i)) * (0.5f);
                vect[i] = new Vector3f(x, 0.0f, z);
            }

            TexCoord2f q = new TexCoord2f(0.0f, 0.0f);
            for (int i = 0; i < n; i++) {
                q.set(0,0); tri.setTextureCoordinate(0,i*3,q);
                q.set(0,1); tri.setTextureCoordinate(0,i*3 +1,q);
                q.set(1,1); tri.setTextureCoordinate(0,i*3 + 2,q);
                tri.setCoordinate(i*3 , new Point3f(0, 0.65f, 0.0f) );
                tri.setCoordinate(i*3 + 1 , coor[(i)]);
                tri.setCoordinate(i*3 + 2 , coor[(i + 1) % n]);

                ///new colour for every cylinder
                Appearance app = new Appearance();
                app.setTexture(texturedApp("MarbleTexture"));
                cylinderArr[i] = new Cylinder(0.04f, 1f, Primitive.GENERATE_TEXTURE_COORDS, app);
                transform.setTranslation(vect[i]);
                t = new TransformGroup(transform);
                bg.addChild(t);
                t.addChild(cylinderArr[i]);
            }
            for (int i = 0; i < n; i++) {
                q.set(0,0); tri2.setTextureCoordinate(0,i*3,q);
                q.set(0,1); tri2.setTextureCoordinate(0,i*3 +1,q);
                q.set(1,1); tri2.setTextureCoordinate(0,i*3 + 2,q);
                tri2.setCoordinate(i*3 , new Point3f(0.0f, 0.5f, 0.0f) );
                tri2.setCoordinate(i*3 + 1 , coor[(i)]);
                tri2.setCoordinate(i*3 + 2 , coor[(i + 1) % n]);

            }

            ///see from more directions
            Appearance app2 = new Appearance();
            app2.setTexture(texturedApp("MarbleTexture"));
            PolygonAttributes pa = new PolygonAttributes();
            pa.setCullFace(PolygonAttributes.CULL_NONE);
            app2.setPolygonAttributes(pa);


            Shape3D pyramid = new Shape3D(tri, app2);
            pyramid.addGeometry(tri2);
            bg.addChild(pyramid);

            return bg;
        }
    }
    private static TransformGroup scalableBase(float scale) {
        BranchGroup bg = new BranchGroup();

        QuadArray quadArr = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        QuadArray quadArr2 = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        ///top rectangle being drawn
        quadArr.setCoordinate(0, new Point3f(-1, 0, -1));
        quadArr.setCoordinate(1, new Point3f(-1,0,1));
        quadArr.setCoordinate(3, new Point3f(1,0,-1));
        quadArr.setCoordinate(2, new Point3f(1,0,1));
        for(int i=0; i<4;i++) {
            quadArr.setColor(i, CommonsHR.Blue);
        }
        ///bottom rectangle being drawn
        quadArr2.setCoordinate(0, new Point3f(-1, -0.08f, -1));
        quadArr2.setCoordinate(1, new Point3f(-1,-0.08f,1));
        quadArr2.setCoordinate(3, new Point3f(1,-0.08f,-1));
        quadArr2.setCoordinate(2, new Point3f(1,-0.08f,1));
        for(int i=0; i<4;i++) {
            quadArr2.setColor(i, CommonsHR.Magenta);
        }
        ///sides of the rectangle
        ///Front
        QuadArray q3 = rectangle(CommonsHR.Red, new Point3f(1,0.04f,1f), new Vector2f(1f,1f));
        ///Right
        QuadArray q6 = rectangle(CommonsHR.Green, new Point3f(1,0.04f,1f), new Vector2f(1f,1f));
        ///Back
        QuadArray q4 = rectangle(CommonsHR.Yellow, new Point3f(1,0.04f,-1f), new Vector2f(1f,1f));
        ///Left
        QuadArray q5 = rectangle(CommonsHR.Cyan, new Point3f(1,0.04f,1f), new Vector2f(1f,1f));

        ///visible from all sides
        Appearance app2 = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app2.setPolygonAttributes(pa);

        ///transform group
        Transform3D transform = new Transform3D();
        Transform3D rotate = new Transform3D();
        rotate.rotY(Math.PI/2);
        TransformGroup r = new TransformGroup(rotate);
        r.addChild(new Shape3D(q6));
        bg.addChild(r);


        rotate.rotY(-Math.PI/2);
        TransformGroup r2 = new TransformGroup(rotate);
        r2.addChild(new Shape3D(q5));
        bg.addChild(r2);
        transform.setScale(scale);
        transform.setTranslation(new Vector3d(0, (-0.04f*scale)-0.5f, 0));

        TransformGroup t = new TransformGroup(transform);
        Shape3D shape3D = new Shape3D(quadArr, app2);
        shape3D.addGeometry(quadArr2);
        shape3D.addGeometry(q3);
        shape3D.addGeometry(q4);

        t.addChild(shape3D);
        t.addChild(bg);
        return t;

    }
    private static QuadArray rectangle(Color3f color, Point3f size, Vector2f scale) {
        QuadArray quadArr = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES);
        ///corners of the rectangle
        quadArr.setCoordinate(0, new Point3f(-size.x*scale.x, size.y*scale.y-.04f, size.z));
        quadArr.setCoordinate(1, new Point3f(-size.x*scale.x , -size.y*scale.y-.04f, size.z));
        quadArr.setCoordinate(2, new Point3f(size.x*scale.x , -size.y*scale.y-.04f, size.z));
        quadArr.setCoordinate(3, new Point3f(size.x*scale.x , size.y*scale.y-.04f, size.z));
        for(int i=0; i<4;i++) {
            quadArr.setColor(i, color);
        }
        return quadArr;
    }
    private static Sphere sphere(float rad) {
        Appearance app = new Appearance();
        app.setMaterial(setMaterial());
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(pa);
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.7f);
        app.setTransparencyAttributes(ta);


        return  new Sphere(rad, Sphere.GENERATE_NORMALS, 80, app);
    }
    public static void object(SharedGroup sg) {
        int n = 6;
        sg.addChild(sphere(1));
        sg.addChild(scalableBase(0.6f));
        sg.addChild(pavilion(n));
        sg.compile();

    }
    ////Animation
    public static RotationInterpolator ri(int rotationnumber, TransformGroup tg, char option, Point3d pos) {
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D axis = new Transform3D();
        switch(option) {
            case 'x':
                axis.rotX(Math.PI/2);
                break;
            case 'z':
                axis.rotZ(Math.PI/2);
                break;
            default:
                ///case Y
                axis.rotY(Math.PI/2);
                break;
        }

        Alpha a = new Alpha(-1, rotationnumber);
        RotationInterpolator rot = new RotationInterpolator(a, tg, axis, 0.0f, (float) Math.PI*2);
        rot.setSchedulingBounds(new BoundingSphere(pos, 100));

        return rot;
    }
    private static void animateSpheres(BranchGroup bg) {
        SharedGroup sharedgroup = new SharedGroup();
        object(sharedgroup);

        ///Transformations
        Transform3D scaler1 = new Transform3D();
        scaler1.setScale(1.1f);

        Transform3D scaler2 = new Transform3D();
        Vector3f trans1 = new Vector3f(1.05f, 0, 1.05f);
        scaler2.setTranslation(trans1);
        scaler2.setScale(0.5f);

        Transform3D scaler3 = new Transform3D();
        Vector3f trans2 = new Vector3f(0, 1.05f, 1.05f);
        scaler3.setTranslation(trans2);
        scaler3.setScale(0.35f);

        ///first sphere and its rotation behaviour
        Link first = new Link(sharedgroup);
        TransformGroup tg1 = new TransformGroup(scaler1);
        tg1.addChild(first);
        bg.addChild(tg1);
        bg.addChild(ri(10000, tg1, 'y', new Point3d(0,0,0)));

        ///second sphere
        Link second = new Link(sharedgroup);
        ///reference frames
        TransformGroup tg2 = new TransformGroup(scaler2);
        TransformGroup tg2ROT = new TransformGroup();
        tg2ROT.addChild(second);
        tg2.addChild(tg2ROT);
        tg1.addChild(tg2);
        tg2.addChild(ri(5000, tg2ROT, 'x', new Point3d(trans1)));

        ///third sphere
        Link third = new Link(sharedgroup);
        TransformGroup tg3 = new TransformGroup(scaler2);
        TransformGroup tg3ROT = new TransformGroup();
        tg3ROT.addChild(third);
        tg3.addChild(tg3ROT);
        tg2ROT.addChild(tg3);
        tg3.addChild(ri(2500, tg3ROT, 'z', new Point3d(trans2)));


    }

    ///Appearances
    public static Texture texturedApp(String name) {
        String filename = "images/" +name+ ".jpg";
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage();
        if(image == null)
            System.out.println("File not found");
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        return texture;
    }
    private static Material setMaterial() {
        Material mat= new Material();
        int SH = 128;
        mat.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        mat.setEmissiveColor(0.0f, 0.0f, 0.0f);
        mat.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        mat.setSpecularColor(1.0f, 1.0f, 1.0f);
        mat.setShininess(SH);
        mat.setLightingEnable(true);
        return mat;
    }
    public static void addLights(TransformGroup b) {
        ///ambient light of colour 0.2f, 0.2f, 0.2f
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        AmbientLight light = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        light.setInfluencingBounds(bounds);
        b.addChild(light);

        ////point light(white) at position (2, 2, 2)
        Point3f pt = new Point3f(2, 2, 2);
        Point3f atn = new Point3f(1, 0, 0);
        Color3f clr = CommonsHR.White;
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        b.addChild(ptLight);
    }
    private static Fog createFog(Color3f clr, BoundingSphere bounds) {
        ExponentialFog e_fog = new ExponentialFog(clr, 0.1f);
        e_fog.setInfluencingBounds(bounds);

        return e_fog;
    }
    ///Background
    private static Background createBackground(Color3f clr, BoundingSphere bounds) {
        Background bg = new Background();
        String filename = "images/bga2.jpg";
        bg.setImage(new TextureLoader(filename, null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(bounds);
        bg.setColor(clr);
        return bg;
    }
    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);

        sceneBG.addChild(sceneTG);	                         // add TG to the scene BranchGroup
        ///sceneBG.addChild(CommonsAP.rotateBehavior(10000, sceneTG)); // make sceneTG continuously rotating
        sceneBG.addChild(createBackground(CommonsHR.Grey, bounds));	///add Background
        sceneBG.addChild(createFog(CommonsHR.Grey, bounds));
        ///createContent(sceneTG);
        animateSpheres(sceneBG);

        addLights(sceneTG);
        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* a function to allow key navigation with the ViewingPlateform */

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsHR.setEye(new Point3d(2, 2,6.0));
                new CommonsHR.MyGUI(createScene(), "HR's Assign 3");
            }
        });
    }
}
