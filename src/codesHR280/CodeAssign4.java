package codesHR280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class CodeAssign4 extends JPanel implements MouseListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static PickTool pickTool;
    //RotationInterpolator variables
    private static RotationInterpolator rotate1;
    private static RotationInterpolator rotate2;
    private static RotationInterpolator rotate3;
    private final Canvas3D canvas;


    /* a constructor to set up and run the application */
    public CodeAssign4(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        canvas.addMouseListener(this);                        // NOTE: enable mouse clicking
        canvas.addKeyListener(this);

        SimpleUniverse su = new SimpleUniverse(canvas);       // create a SimpleUniverse
        CommonsHR.setEye(new Point3d(2, 2, 6.0));
        CommonsHR.defineViewer(su);                           // set the viewer's location

        sceneBG.compile();
        su.addBranchGraph(sceneBG);                           // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(600, 600);                              // set the size of the JFrame
        frame.setVisible(true);
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();
        sceneBG.addChild(addBackground(CommonsHR.Grey, new BoundingSphere()));    ///add Background
        pickTool = new PickTool(sceneBG);                   // allow picking of objs in 'sceneBG'
        pickTool.setMode(PickTool.BOUNDS);

        addLights(sceneBG, CommonsHR.White);
        sphereBehaviour(sceneBG);

        return sceneBG;
    }

    /* a function to add ambient light and a point light to 'sceneBG' */
    public static void addLights(BranchGroup sceneBG, Color3f clr) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        AmbientLight amLgt = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        amLgt.setInfluencingBounds(bounds);
        sceneBG.addChild(amLgt);
        Point3f pt = new Point3f(2.0f, 2.0f, 2.0f);
        Point3f atn = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ptLight);
    }

    /* a function to create and return material definition */
    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    //funtion that add the background in the frame
    private static Background addBackground(Color3f clr, BoundingSphere sphere) {
        Background bg = new Background();//create a background
        bg.setImage(new TextureLoader("images/bga2.jpg", null).getImage());//load image
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);//set scaling
        bg.setApplicationBounds(sphere);//setting bounds
        bg.setColor(clr);//setting color
        return bg;
    }

    //textured app for modifying appearance
    public static Texture texturedApp(String name) {
        String filename = name;
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage();
        if (image == null)
            System.out.println("File not found");
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        return texture;
    }

    //new setMaterial
    private static Material setMaterial() {
        Material mat = new Material();
        int SH = 128;
        mat.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        mat.setEmissiveColor(0.0f, 0.0f, 0.0f);
        mat.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        mat.setSpecularColor(1.0f, 1.0f, 1.0f);
        mat.setShininess(SH);
        mat.setLightingEnable(true);
        return mat;
    }

    //function to create the globes/spheres
    private static Sphere sphere(float rad, String name) {
        Appearance sphereAppearance = new Appearance();//appearance for sphere
        Color3f black = new Color3f(0, 0, 0);//black color
        ColoringAttributes coloringAttributes = new ColoringAttributes(black, ColoringAttributes.NICEST);
        PolygonAttributes polygonAttributes = new PolygonAttributes();
        sphereAppearance.setPolygonAttributes(polygonAttributes);
        sphereAppearance.setTexture(texturedApp("images/MarbleTexture.jpg"));//default image as MarbleTexture
        sphereAppearance.setColoringAttributes(coloringAttributes);
        sphereAppearance.setMaterial(setMaterial());
        sphereAppearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        Sphere sp = new Sphere(1, Sphere.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS | Primitive.ENABLE_APPEARANCE_MODIFY, 80, sphereAppearance);//creating the sphere
        sp.setUserData(0);
        sp.setName(name);
        return sp;
    }

    // function for single transform alpha
    public static RotationInterpolator rotationInterpolator(int rotationnumber, TransformGroup tg, char option, Point3d pos) {
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D axis = new Transform3D();
        switch (option) {
            case 'x':
                axis.rotX(Math.PI / 2);
                break;
            case 'z':
                axis.rotZ(Math.PI / 2);
                break;
            default:
                ///case Y
                axis.rotY(Math.PI / 2);
                break;
        }

        Alpha alpha = new Alpha(-1, rotationnumber);
        RotationInterpolator rotate = new RotationInterpolator(alpha, tg, axis, 0.0f, (float) Math.PI * 2);
        rotate.setSchedulingBounds(new BoundingSphere(pos, 100));
        return rotate;
    }

    // Animated Spheres
    private static void sphereBehaviour(BranchGroup bg) {
        ///Transformations
        Transform3D sc1 = new Transform3D();
        sc1.setScale(1.1f);

        Transform3D sc2 = new Transform3D();
        Vector3f trans1 = new Vector3f(1.05f, 0, 1.05f);
        sc2.setTranslation(trans1);
        sc2.setScale(0.5f);

        Transform3D sc3 = new Transform3D();
        Vector3f trans2 = new Vector3f(0, 1.05f, 1.05f);
        sc3.setTranslation(trans2);
        sc3.setScale(0.35f);

        ///first sphere and its rotation behaviour
        TransformGroup tg1 = new TransformGroup(sc1);
        tg1.addChild(sphere(1, "Sun"));//sun sphere
        bg.addChild(tg1);
        rotate1 = rotationInterpolator(10000, tg1, 'y', new Point3d(0, 0, 0));
        tg1.addChild(rotate1);

        ///second sphere
        ///reference frames
        TransformGroup tg2 = new TransformGroup(sc2);
        TransformGroup tg2ROT = new TransformGroup();
        tg2ROT.addChild(sphere(1, "Earth"));//earth sphere
        tg2.addChild(tg2ROT);
        tg1.addChild(tg2);
        rotate2 = rotationInterpolator(5000, tg2ROT, 'x', new Point3d(trans1));
        tg2.addChild(rotate2);

        ///third sphere
        TransformGroup tg3 = new TransformGroup(sc2);
        TransformGroup tg3ROT = new TransformGroup();
        tg3ROT.addChild(sphere(1, "Moon"));//moon sphere
        tg3.addChild(tg3ROT);
        tg2ROT.addChild(tg3);
        rotate3 = rotationInterpolator(2500, tg3ROT, 'z', new Point3d(trans2));
        tg3.addChild(rotate3);

    }

    public static void main(String[] args) {
        frame = new JFrame("HR's Assignment 4");
        frame.getContentPane().add(new CodeAssign4(createScene()));

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    // Function to determine what happens when z,x,c are pressed
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_Z)) {//key z
            long r = rotate1.getAlpha().getIncreasingAlphaDuration();
            if (r == 10000)
                rotate1.setAlpha(new Alpha(-1, 0));
            else
                rotate1.setAlpha(new Alpha(-1, 10000));
        }
        if ((e.getKeyCode() == KeyEvent.VK_X)) {//key x
            long r = rotate2.getAlpha().getIncreasingAlphaDuration();
            if (r == 5000)
                rotate2.setAlpha(new Alpha(-1, 0));
            else
                rotate2.setAlpha(new Alpha(-1, 5000));
        }
        if ((e.getKeyCode() == KeyEvent.VK_C)) {//key c
            long r = rotate3.getAlpha().getIncreasingAlphaDuration();
            if (r == 2500)
                rotate3.setAlpha(new Alpha(-1, 0));
            else
                rotate3.setAlpha(new Alpha(-1, 2500));
        }
    }

    @Override
    //function to determine when mouse is clicked
    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();           // mouse coordinates
        Point3d point3d = new Point3d(), center = new Point3d();
        canvas.getPixelLocationInImagePlate(x, y, point3d);   // obtain AWT pixel in ImagePlate coordinates
        canvas.getCenterEyeInImagePlate(center);              // obtain eye's position in IP coordinates

        Transform3D transform3D = new Transform3D();          // matrix to relate ImagePlate coordinates~
        canvas.getImagePlateToVworld(transform3D);            // to Virtual World coordinates
        transform3D.transform(point3d);                       // transform 'point3d' with 'transform3D'
        transform3D.transform(center);                        // transform 'center' with 'transform3D'

        Vector3d mouseVec = new Vector3d();
        mouseVec.sub(point3d, center);
        mouseVec.normalize();
        pickTool.setShapeRay(point3d, mouseVec);              // send a PickRay for intersection

        if (pickTool.pickClosest() != null) {
            PickResult pickResult = pickTool.pickClosest();   // obtain the closest hit
            Sphere box = (Sphere) pickResult.getNode(PickResult.PRIMITIVE);
            Appearance app = box.getAppearance();                // originally a PRIMITIVE as a box
            if ((int) box.getUserData() == 0) {               // retrieve 'UserData'
                String s = box.getName();
                if (s == "Sun")
                    app.setTexture(texturedApp("images/sun.jpg"));//sun image
                else if (s == "Earth")
                    app.setTexture(texturedApp("images/earth.jpg"));//earth image
                else
                    app.setTexture(texturedApp("images/moon.jpg"));//moon image
                box.setUserData(1);                           // set 'UserData' to a new value
            } else {
                app.setTexture(texturedApp("images/MarbleTexture.jpg"));
                box.setUserData(0);                           // reset 'UserData'
            }
            box.setAppearance(app);                           // change box's appearance
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
