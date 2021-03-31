package codesHR280;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;


public class CodeLab7 extends JPanel {
    private static final long serialVersionUID = 1L;

    ///sets the material, clr, and allows lighting to be set on the material
    private static Material setMaterial(Color3f clr) {
        Material mat= new Material();
        int SH = 128;
        mat.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        mat.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        mat.setDiffuseColor(clr);
        mat.setSpecularColor(1.0f, 1.0f, 1.0f);
        mat.setShininess(SH);
        mat.setLightingEnable(true);
        return mat;
    }
    ///create sphere based on clr, radius and divisions
    private static Sphere sphere(Color3f clr, float rad, int div) {
        Appearance app = new Appearance();

        app.setMaterial(setMaterial(clr));
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(pa);


        return  new Sphere(rad, Sphere.GENERATE_NORMALS, div, app);
    }

    ///function to add a point and ambient light
    public static void addLights(BranchGroup b) {
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
    ///define the simpoe universe
    private void defineViewer(SimpleUniverse simple_U, Point3d eye) {

        TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }
    ///allow the keys to control the simple universe
    private KeyNavigatorBehavior keyNavigation(SimpleUniverse simple_U) {

        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 20.0);
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
    }
    ///adds content to a branch group, the content is the roation and the sphere
    private static BranchGroup createScene(BranchGroup sceneBG) {
        TransformGroup content_TG = new TransformGroup();    // create a TransformGroup (TG)
        sceneBG.addChild(content_TG);
        sceneBG.addChild(CommonsHR.rotateBehavior(10000, content_TG));
        spheres(content_TG);
        addLights(sceneBG);

        return sceneBG;
    }

    ///render spheres depending on the distances

    private static void spheres(TransformGroup TG) {
        float[] distances = {4, 7.5f, 12};
        Switch target = new Switch();
        target.setCapability(Switch.ALLOW_SWITCH_WRITE);
        target.addChild(sphere(CommonsHR.Green, 0.75f, 60 ));
        target.addChild(sphere(CommonsHR.Blue, 0.6f, 45));
        target.addChild(sphere(CommonsHR.Orange, 0.5f, 30));
        target.addChild(sphere(CommonsHR.Red, 0.35f, 15 ));

        DistanceLOD render = new DistanceLOD(distances);
        render.setSchedulingBounds(new BoundingSphere());
        render.addSwitch(target);

        TG.addChild(render);
        TG.addChild(target);

    }

    public CodeLab7() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas_3D = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas_3D);   // create a SimpleUniverse
        defineViewer(su, new Point3d(1.35, 0.35, 2.0));    // set the viewer's location

        BranchGroup scene = new BranchGroup();
        createScene(scene);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation

        scene.compile();		                             // optimize the BranchGroup
        su.addBranchGraph(scene);                            // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas_3D);
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HR Lab 7"); // NOTE: change XY to your initials
        frame.getContentPane().add(new CodeLab7());         // create an instance of the class
        frame.setSize(600, 600);                             // set the size of the JFrame
        frame.setVisible(true);
    }

}

