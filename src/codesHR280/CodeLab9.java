package codesHR280;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;


public class CodeLab9 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static SoundUtilityJOAL soundJOAL;
    private static String snd_pt = "cow";

    public class BehaviorArrowKey extends Behavior {
        private TransformGroup navigatorTG;
        private WakeupOnAWTEvent wEnter;

        public BehaviorArrowKey(ViewingPlatform targetVP, TransformGroup chasedTG) {
            navigatorTG = chasedTG;
            targetVP.getViewPlatformTransform();
        }

        public void initialize() {
            wEnter = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
            wakeupOn(wEnter);                              // decide when behavior becomes live
        }

        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            Transform3D navigatorTF = new Transform3D();   // get Transform3D from 'navigatorTG'
            navigatorTG.getTransform(navigatorTF);
            Vector3d vct = new Vector3d();
            navigatorTF.get(vct);                          // get position of 'navigatorTG'
            soundJOAL.setPos("cow",(float)vct.x,(float)vct.y,(float)vct.z);//set position sound added

            wakeupOn(wEnter);                              // decide when behavior becomes live
        }
    }

    public static Material setMaterial(Color3f clr){//function added from the slides as advised
        int SH=128;
        Material ma = new Material();//creating material
        ma.setAmbientColor(new Color3f(0,0,0));
        ma.setEmissiveColor(new Color3f(0,0,0));
        ma.setDiffuseColor(clr);
        ma.setSpecularColor(new Color3f(1.0f,1.0f,1.0f));
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    private static Appearance setApp(Color3f clr) {
        Appearance app = new Appearance();
        app.setMaterial(setMaterial(clr));
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(clr);
        app.setColoringAttributes(colorAtt);
        return app;
    }

    //function for loading the cow shape
    private TransformGroup addCowShape() {
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
        Scene scene = null;
        try {
            scene = f.load("images/CowTray.obj");//load the shape
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        BranchGroup branchGroup = scene.getSceneGroup();
        Shape3D cows = (Shape3D) branchGroup.getChild(0);
        cows.setAppearance(setApp(CommonsXY.Orange));
        TransformGroup loadedShape = new TransformGroup();
        loadedShape.addChild(branchGroup);
        Transform3D rot = new Transform3D();
        Transform3D scale = new Transform3D();
        scale.setScale(0.4);
        rot.rotX(3 * Math.PI / 2);
        rot.mul(scale);
        loadedShape.setTransform(rot);
        return loadedShape;

    }
    //function for adding the lights
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

    public BranchGroup soundObject() {
        BranchGroup objectBG = new BranchGroup();
        TransformGroup objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objectTG.addChild(addCowShape());
        soundJOAL = new SoundUtilityJOAL();
        if (!soundJOAL.load(snd_pt, 0f, 0f, 10f, true))     // fix 'snd_pt' at cow location
            System.out.println("Could not load " + snd_pt);
        else
            soundJOAL.play(snd_pt);                         // start 'snd_pt'

        ViewingPlatform ourView = CommonsXY.getSimpleU().getViewingPlatform();
        KeyNavigatorBehavior  myRotationbehavior = new KeyNavigatorBehavior(objectTG);
        BehaviorArrowKey myViewRotationbehavior = new BehaviorArrowKey(ourView, objectTG);
        myRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myRotationbehavior);
        myViewRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myViewRotationbehavior);

        objectBG.addChild(objectTG);

        return objectBG;
    }

    /* a function to create and return the scene BranchGroup */
    public BranchGroup createScene() {
        CommonsXY.createUniverse();

        BranchGroup scene = new BranchGroup();            // create 'scene' as content branch
        TransformGroup scene_TG = new TransformGroup();   // create 'scene_TG' TransformGroup

        scene_TG.addChild(soundObject());
        scene.addChild(scene_TG); // add scene_TG to scene BG
        scene.addChild(threeAxes(CommonsXY.Yellow, 0.5f));    // add axes
        addLights(scene, CommonsXY.White);                    // point+amb

        return scene;
    }

    /* the main entrance of the application with specified window dimension */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsXY.setEye(new Point3d(1.35, 0.35, 2.0));
                CodeLab9 codeLab9 = new CodeLab9();
                new CommonsXY.MyGUI(codeLab9.createScene(), "HR's Lab 9");
            }
        });
    }

    /* a function to create and return a Shape3D */
    public static Shape3D threeAxes(Color3f yColor, float ln) {
        Point3f pts[] = { new Point3f(0, 0, 0),              // use 4 points for axices
                new Point3f(0, 0, ln),
                new Point3f(ln, 0, 0),   new Point3f(0, ln, 0) };
        int[] indices = {0, 1, 0, 2, 0, 3};                  // the Z-, X-, Y-axis

        IndexedLineArray lines = new IndexedLineArray(4,
                LineArray.COORDINATES | LineArray.COLOR_3, 6);

        lines.setCoordinates(0, pts);
        lines.setCoordinateIndices(0, indices);

        Color3f[] line_clr = {CommonsXY.Red, CommonsXY.Green, yColor};
        int[] c_indices = {0, 0, 1, 1, 2, 2};
        lines.setColors(0,  line_clr);                       // set color for each axis
        lines.setColorIndices(0, c_indices);

        return new Shape3D(lines);                           // return the Shape3D
    }

}