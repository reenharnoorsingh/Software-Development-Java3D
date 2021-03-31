package codesHR280;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;


public class CodeAssign5b extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    private Canvas3D canvas;
    private static SoundUtilityJOAL utilityJOAL;               // needed for adding sound
    private static PickTool pickTool;
    private static RotationInterpolator rotate1;
    private static RotationInterpolator rotate2;
    private static RotationInterpolator rotate3;

    //function that adds the background
    private static Background addBackground(Color3f color, BoundingSphere sphere){
        Background background=new Background();//create a background
        background.setImage(new TextureLoader("images/bga2.jpg",null).getImage());//load image
        background.setImageScaleMode(Background.SCALE_FIT_MAX);//set scaling
        background.setApplicationBounds(sphere);//setting bounds
        background.setColor(color);//setting color
        return background;
    }
    //function that maps the texture maps the texture
    public static Texture texturedApp(String name) {
        String filename =name;
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage();
        if(image == null)
            System.out.println("File not found");
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        return texture;
    }
    public static RotationInterpolator rotationInterpolator(int rotationNumber, TransformGroup tg, char option, Point3d pos) {
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

        Alpha a = new Alpha(-1, rotationNumber);//declaring alpha
        RotationInterpolator rot = new RotationInterpolator(a, tg, axis, 0.0f, (float) Math.PI*2);
        rot.setSchedulingBounds(new BoundingSphere(pos, 100));//setting the bounds
        return rot;//returning the rotation factor
    }
    //function that defines sphere behaviour
    private static void makeCluster(BranchGroup bg) {
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


    private static Material setMaterial() {//sets the material
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

    private static Sphere sphere(float rad,String name) {
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




    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();
        sceneBG.addChild(addBackground(CommonsHR.Grey, new BoundingSphere()));	///add Background

//		oneBox(sceneBG);
        pickTool = new PickTool( sceneBG );                   // allow picking of objs in 'sceneBG'
        pickTool.setMode(PickTool.BOUNDS);

        addLights(sceneBG, CommonsHR.White);
        makeCluster(sceneBG);//adding the spheres to the scene
        return sceneBG;
    }

    /* a constructor to set up and run the application */
    public CodeAssign5b(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        canvas.addMouseListener(this);                        // NOTE: enable mouse clicking

        SimpleUniverse su = new SimpleUniverse(canvas);       // create a SimpleUniverse
        CommonsXY.setEye(new Point3d(2, 2, 6.0));
        CommonsXY.defineViewer(su);                           // set the viewer's location

        sceneBG.compile();
        su.addBranchGraph(sceneBG);                           // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(600, 600);                              // set the size of the JFrame
        frame.setVisible(true);
    }

    public CodeAssign5b(Canvas3D canvas3D, int k) {
        canvas = canvas3D;
    }

    public static void main(String[] args) {
        frame = new JFrame("HR's Assignment 5b");
        frame.getContentPane().add(new CodeAssign5b(createScene()));
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        String s2=canvas.getName();
        int x = event.getX(); int y = event.getY();           // mouse coordinates
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
                String s=box.getName();                 // we idnetify the sphere from its name
                if(s=="Sun") {    //if the biggest sphere
                    app.setTexture(texturedApp("images\\sun.jpg"));
                    if(s2=="F-L")
                        CodeAssign5b.playSound(1);
                    else
                        CodeAssign5b.playSound(2);
                }
                else if(s=="Earth") { // if the medium sphere
                    app.setTexture(texturedApp("images\\earth.jpg"));
                    if(s2=="F-L")
                        CodeAssign5b.playSound(1);
                    else
                        CodeAssign5b.playSound(2);
                }
                else {    // if the smallest sphere
                    app.setTexture(texturedApp("images\\moon.jpg"));
                    if(s2=="F-L")
                        CodeAssign5b.playSound(1);
                    else
                        CodeAssign5b.playSound(2);
                }
                box.setUserData(1);                           // set 'UserData' to a new value
            }
            else { //if they have been changed with the image then reset
                app.setTexture(texturedApp("images\\MarbleTexture.jpg"));
                if(s2=="F-L")
                    CodeAssign5b.playSound(1);
                else
                    CodeAssign5b.playSound(2);
                box.setUserData(0);                           // reset 'UserData'
            }
            box.setAppearance(app);                           // change box's appearance
        }
    }
    public void mouseEntered(MouseEvent arg0) { }
    public void mouseExited(MouseEvent arg0) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

    /* for A5: a function to initialize for playing sound */
    public static void initialSound() {
        utilityJOAL = new SoundUtilityJOAL();
        if (!utilityJOAL.load("laser2", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "laser2");
        if (!utilityJOAL.load("magic_bells", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "magic_bells");
    }

    /* a function to play different sound according to key (user) */
    public static void playSound(int key) {
        String snd_pt = "laser2";
        if (key > 1)
            snd_pt = "magic_bells";
        utilityJOAL.play(snd_pt);
        try {
            Thread.sleep(500); // sleep for 0.5 secs
        } catch (InterruptedException ex) {}
        utilityJOAL.stop(snd_pt);
    }

    /* a function to add ambient light and a point light to 'sceneBG' */
    public static void addLights(BranchGroup sceneBG, Color3f clr) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        AmbientLight amLgt = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        amLgt.setInfluencingBounds(bounds);
        sceneBG.addChild(amLgt);
        Point3f pt  = new Point3f(2.0f, 2.0f, 2.0f);
        Point3f atn = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ptLight);
    }

    /* a function to create and return material definition */
    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f*clr.x, 0.6f*clr.y, 0.6f*clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(CommonsXY.None);
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }
}