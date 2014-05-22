package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.Random;
import mygame.controls.BetterCharacterControl;
import mygame.controls.CharacterAnimControl;

/**
 * A walking physical character followed by a 3rd person camera. (No animation.)
 *
 * @author normenhansen, zathras
 */
public class Main extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private BetterCharacterControl physicsCharacter;
    private CharacterAnimControl characterAnimControl;
    private Node windowsNode;
    private Node characterNode;
    private CameraNode camNode;
    boolean rotate = false;
    private float speed=5;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    boolean leftStrafe = false, rightStrafe = false, forward = false, backward = false,
            leftRotate = false, rightRotate = false;
    
    private Vector3f startPoint;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //setup keyboard mapping
        setupKeys();

        
        
        windowsNode=new Node();
        rootNode.attachChild(windowsNode);
        
        
        // activate physics
        bulletAppState = new BulletAppState();
        
        stateManager.attach(bulletAppState);

        // init a physics test scene
        
        setupPlanet();

        // Create a node for the character model
        characterNode = new Node("character node");
        //characterNode.setLocalTranslation(new Vector3f(4, 5, 2));

        // Add a character control to the node so we can add other things and
        // control the model rotation
        
        physicsCharacter = new BetterCharacterControl(0.7f, 2f, 8f, characterNode);
        
        characterNode.addControl(physicsCharacter);
        getPhysicsSpace().add(physicsCharacter);
        physicsCharacter.warp(startPoint);

        
        // Load model, attach to character node
        Node model = (Node) assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        model.setLocalScale(0.2f);
        model.setLocalTranslation(new Vector3f(0,1f,0));
        characterNode.attachChild(model);
        characterAnimControl = new CharacterAnimControl(model, physicsCharacter);
        characterNode.addControl(characterAnimControl);
        
        // Add character node to the rootNode
        rootNode.attachChild(characterNode);

        // Set forward camera node that follows the character, only used when
        // view is "locked"
        camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 2, -6));
        Quaternion quat = new Quaternion();
        // These coordinates are local, the camNode is attached to the character node!
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        camNode.setLocalRotation(quat);
        characterNode.attachChild(camNode);
        // Disable by default, can be enabled via keyboard shortcut
        camNode.setEnabled(false);
        //bulletAppState.setDebugEnabled(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        

        // Get current forward and left vectors of model by using its rotation
        // to rotate the unit vectors
        Vector3f modelForwardDir = characterNode.getWorldRotation().mult(Vector3f.UNIT_Z);
        Vector3f modelLeftDir = characterNode.getWorldRotation().mult(Vector3f.UNIT_X);

        // WalkDirection is global!
        // You *can* make your character fly with this.
        walkDirection.set(0, 0, 0);
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(speed));
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(speed));
        }
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(speed));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(speed));
        }
        physicsCharacter.setWalkDirection(walkDirection);

        // ViewDirection is local to characters physics system!
        // The final world rotation depends on the gravity and on the state of
        // setApplyPhysicsLocal()
        if (leftRotate) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (rightRotate) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }
        physicsCharacter.setViewDirection(viewDirection);
        fpsText.setText("Touch da ground = " + physicsCharacter.isOnGround());
        if (!lockView) {
            cam.lookAt(characterNode.getWorldTranslation().add(new Vector3f(0, 2, 0)), Vector3f.UNIT_Y);
        }
    }

    private void setupPlanet() {
        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(Vector3f.UNIT_XYZ.negate());
        rootNode.addLight(dl);
        Node scene = (Node) assetManager.loadModel("Scenes/MainLevel/MainScene.j3o");
        startPoint= scene.getChild("StartPoint").getLocalTranslation();
        scene.addControl(new RigidBodyControl(0));
        scene.setName("Main Scene");
        rootNode.attachChild(scene);
        getPhysicsSpace().add(scene);
        
        
        
    }


    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Strafe Left")) {
            if (value) {
                leftStrafe=true;
            } else {
                leftStrafe=false;
            }
        } else if (binding.equals("Strafe Right")) {
            if (value) {
                rightStrafe=true;
            } else {
                rightStrafe=false;
            }
        } else if (binding.equals("Rotate Left")) {
            if (value) {
                leftRotate=true;
            } else {
                leftRotate=false;
            }
        } else if (binding.equals("Rotate Right")) {
            if (value) {
                
                rightRotate=true;
            } else {
                rightRotate=false;
                
            }
        } else if (binding.equals("Walk Forward")) {
            if (value) {
                forward=true;
            } else {
                forward=false;
            }
        } else if (binding.equals("Walk Backward")) {
            if (value) {
                backward=true;
            } else {
                backward=false;
            }
        } else if (binding.equals("Jump")) {
            physicsCharacter.jump();
        } else if (binding.equals("Duck")) {
            if (value) {
                physicsCharacter.setDucked(true);
            } else {
                physicsCharacter.setDucked(false);
            }
        } else if (binding.equals("Lock View")) {
            if (value && lockView) {
                lockView = false;
            } else if (value && !lockView) {
                lockView = true;
            }
            flyCam.setEnabled(!lockView);
            camNode.setEnabled(lockView);
        }else if (binding.equals("GenTest")) {
            if (value) {
                generateWindows(1,6,3,10);
            } else {
               
            }
        }
    }
    private boolean lockView = false;

    private void setupKeys() {
        inputManager.addMapping("Strafe Left",
                new KeyTrigger(KeyInput.KEY_U),
                new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Strafe Right",
                new KeyTrigger(KeyInput.KEY_O),
                new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("Rotate Left",
                new KeyTrigger(KeyInput.KEY_J),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Rotate Right",
                new KeyTrigger(KeyInput.KEY_L),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Walk Forward",
                new KeyTrigger(KeyInput.KEY_I),
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Walk Backward",
                new KeyTrigger(KeyInput.KEY_K),
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_F),
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Duck",
                new KeyTrigger(KeyInput.KEY_G),
                new KeyTrigger(KeyInput.KEY_LSHIFT),
                new KeyTrigger(KeyInput.KEY_RSHIFT));
        inputManager.addMapping("Lock View",
                new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("GenTest",
                new KeyTrigger(KeyInput.KEY_C));
        inputManager.addListener(this, "Strafe Left", "Strafe Right");
        inputManager.addListener(this, "Rotate Left", "Rotate Right");
        inputManager.addListener(this, "Walk Forward", "Walk Backward");
        inputManager.addListener(this, "Jump", "Duck", "Lock View");
        inputManager.addListener(this, "GenTest");
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    
    public void generateWindows(int numSide, int rows, int numPerRows, int numWindows){
        Vector3f startPoint=new Vector3f();
        ArrayList used = new ArrayList();
        Vector2f coord;
        Random rand = new Random();
        float spaceY=3.5f;
        int spaceX=3;
        int xVar=1;
        int zVar=1;
        Box box = new Box(1,0.25f,1);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        
        
        windowsNode.removeControl(RigidBodyControl.class);
        windowsNode.detachAllChildren();
        
        if(numSide==1){
            xVar=1;
            zVar=0;
            startPoint.set(2,1,10);
        }else if(numSide==2){
            xVar=0;
            zVar=-1;
            startPoint.set(10,1,8);
        }else if(numSide==3){
            xVar=0;
            zVar=-1;
            startPoint.set(10,1,-2);
        }else if(numSide==4){
            xVar=-1;
            zVar=0;
            startPoint.set(8,1,-10);
        }else if(numSide==5){
            xVar=-1;
            zVar=0;
            startPoint.set(-2,1,-10);
        }else if(numSide==6){
            xVar=0;
            zVar=1;
            startPoint.set(-10,1,-8);
        }
        
        
        
        for(int i=0;i<numWindows;i++){
            coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // while(used.contains(coord)){
           //     coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // }
            used.add(coord);
            Vector3f pt= startPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
            Geometry windowSill = new Geometry("Window_"+numSide+"_"+coord.getY()+"_"+coord.getX(), box);
            windowSill.setLocalTranslation(pt);
            windowSill.setMaterial(mat1);
            windowsNode.attachChild(windowSill);
            
            
        }
        windowsNode.addControl(new RigidBodyControl(0));
        getPhysicsSpace().add(windowsNode);
        
        /*2,1,10   +++x
         * 10,1,8   ---z
         * [10.0, 1.0, -2.0] ---z
         * [8.0, 1.0, -10.0] ---x
         * [-2.0, 1.0, -10.0] ---x
         * [-10.0, 1.0, -8.0] +++z
         */
        
        
        
        
        
    }
}
