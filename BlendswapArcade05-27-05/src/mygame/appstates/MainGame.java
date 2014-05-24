/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.Random;
import mygame.Main;
import mygame.controls.BabyDropperControl;
import mygame.controls.BetterCharacterControl;
import mygame.controls.CharacterAnimControl;

/**
 *
 * @author User
 */
public class MainGame extends AbstractAppState{
    private Main app;
    private Node rootNode;
    private BulletAppState bulletAppState;
    
    
    private BetterCharacterControl physicsCharacter;
    private CharacterAnimControl characterAnimControl;
    private Node windowsNode, dirtyWindowsNode,objectDropNode;
    private Node characterNode;
    private CameraNode camNode;
    boolean rotate = false;
    private float speed=5;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    private Vector3f startPoint = new Vector3f(-10,0,18);
    
     boolean leftStrafe = false, rightStrafe = false, forward = false, backward = false,
            leftRotate = false, rightRotate = false;
    
    private int lives=3; 
    
    public MainGame(){
        
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //this.app.enableFlyCam(true);
        
          
          this.app=(Main) app;
          this.rootNode=this.app.getRootNode();
          this.bulletAppState=this.app.getStateManager().getState(BulletAppState.class);
         
          setUpNodes();
          setUpCharacter();
          setUpCam();  
       
    }
    
      @Override
      public void stateAttached(AppStateManager stateManager) {
          
      }
      private void setUpNodes(){
          windowsNode=new Node();
          windowsNode.setName("Windows Node");
          rootNode.attachChild(windowsNode);
          dirtyWindowsNode=new Node();
          rootNode.attachChild(dirtyWindowsNode);
          objectDropNode=new Node();
          rootNode.attachChild(objectDropNode);
      }
      private void setUpCharacter(){
          characterNode = new Node("character node");
        //characterNode.setLocalTranslation(new Vector3f(4, 5, 2));

        // Add a character control to the node so we can add other things and
        // control the model rotation
        
        physicsCharacter = new BetterCharacterControl(0.7f, 2f, 8f, characterNode);
        
        characterNode.addControl(physicsCharacter);
        

        
        // Load model, attach to character node
        Node model = (Node) app.getAssetManager().loadModel("Models/Sinbad/Sinbad.mesh.xml");
        model.setLocalScale(0.2f);
        model.setLocalTranslation(new Vector3f(0,1f,0));
        characterNode.attachChild(model);
        characterAnimControl = new CharacterAnimControl(model, physicsCharacter);
        characterNode.addControl(characterAnimControl);
        
        // Add character node to the rootNode
        rootNode.attachChild(characterNode);
        getPhysicsSpace().add(physicsCharacter);
        physicsCharacter.warp(startPoint);
      }
      private void setUpCam(){
            camNode = new CameraNode("CamNode", app.getCamera());
            camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
            camNode.setLocalTranslation(new Vector3f(0, 2, -6));
            Quaternion quat = new Quaternion();
            // These coordinates are local, the camNode is attached to the character node!
            quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
            camNode.setLocalRotation(quat);
            characterNode.attachChild(camNode);
      }
      
      @Override
    public void update(float tpf) {
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
       // fpsText.setText("Touch da ground = " + physicsCharacter.isOnGround());
       /* if (!lockView) {
            cam.lookAt(characterNode.getWorldTranslation().add(new Vector3f(0, 2, 0)), Vector3f.UNIT_Y);
        }*/
        
        if(lives<=0){
            physicsCharacter.warp(startPoint);
            lives=3;
        }
    }

    
      
      
      
      
      @Override
      public void stateDetached(AppStateManager stateManager) {
          app.getRootNode().detachChild(rootNode);
          app.getRootNode().detachChild(windowsNode);
          app.getRootNode().detachChild(dirtyWindowsNode);
          app.getRootNode().detachChild(objectDropNode);
          /*app.getInputManager().removeListener(actionListener);
          app.getInputManager().deleteMapping("left");
          app.enableFlyCam(false);*/
      }
    
    @Override
    public void cleanup() {
        super.cleanup();
 
    }
    
    
    
    
    ArrayList windowSillList=new ArrayList();
    public void generateWindows(int numSide, int numWindows){
        Vector3f startPoint=new Vector3f();
        ArrayList used = new ArrayList();
        Vector2f coord;
        Random rand = new Random();
        float spaceY=3.5f;
        int spaceX=3;
        int xVar=1;
        int zVar=1;
        Box box=new Box();
        int rows=0,numPerRows=3;
        Material mat1 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Color", new ColorRGBA(0.5764706f,0.42352942f,0.41960785f,1));
        RigidBodyControl rbc = new RigidBodyControl(0);
        /* A colored lit cube. Needs light source! */ 
        
        windowSillList.clear();
         //windowsNode.removeControl(RigidBodyControl.class);
         windowsNode.detachAllChildren();
         
        
        
        if(numSide==1){
            //2,1,10   +++x
            rows=6;
            numPerRows=3;
            xVar=1;
            zVar=0;
            startPoint.set(2,1,10);
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==2){
            //10,1,8   ---z
            rows=6;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10,1,8);
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
        }else if(numSide==3){
            //[10.0, 1.0, -2.0] ---z
            rows=9;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10,1,-2);
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
        }else if(numSide==4){
            //[8.0, 1.0, -10.0] ---x
            rows=9;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(8,1,-10);
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==5){
            //[-2.0, 1.0, -10.0] ---x
            rows=13;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(-2,1,-10);
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==6){
            //[-10.0, 1.0, -8.0] +++z
            rows=13;
            numPerRows=3;
            xVar=0;
            zVar=1;
            startPoint.set(-10,1,-8);
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
        }
        
        
        
        for(int i=0;i<numWindows;i++){
            coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // while(used.contains(coord)){
           //     coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // }
            
            if(!windowSillList.contains(coord)){
                windowSillList.add(coord);
                used.add(coord);
                Vector3f pt= startPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
                Geometry windowSill = new Geometry("Window_"+numSide+"_"+coord.getY()+"_"+coord.getX(), box);
                windowSill.setLocalTranslation(pt);
                windowSill.setMaterial(mat1);
                windowsNode.attachChild(windowSill);
            }
            
            
            
        }
        rbc.setCollisionShape(CollisionShapeFactory.createMeshShape((Node) windowsNode ));
        if(windowsNode.getControl(RigidBodyControl.class)!=null){
            
            app.getPhysicsSpace().remove(windowsNode);
            windowsNode.removeControl(RigidBodyControl.class);
        }
        windowsNode.addControl(rbc);
        app.getPhysicsSpace().add(windowsNode);
      
    }
    public void generateDirtyWindows(int numSide, int numWindows){
        Vector3f startPoint=new Vector3f();
        Vector2f coord;
        Random rand = new Random();
        float spaceY=3.5f;
        int spaceX=3;
        int xVar=1;
        int zVar=1;
        int rows=0,numPerRows=3;
        Box box=new Box(1,1,1);
        Material mat1 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", new ColorRGBA(0.5764706f,0.42352942f,0.41960785f,1));
        mat1.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/dirtyWindow/textures/DirtyWindow.png"));
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        /* A colored lit cube. Needs light source! */ 
        
        
         //windowsNode.removeControl(RigidBodyControl.class);
         dirtyWindowsNode.detachAllChildren();
         
        
        
        if(numSide==1){
            //2,1,10   +++x
            rows=6;
            numPerRows=3;
            xVar=1;
            zVar=0;
            startPoint.set(2,1.7f,10.001f);
            box.updateGeometry(Vector3f.ZERO, 0.99f, 0.7f, 0.1f);
        }else if(numSide==2){
            //10,1,8   ---z
            rows=6;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10.001f,1.7f,8);
            box.updateGeometry(Vector3f.ZERO, 0.1f, 0.7f, 0.99f);
        }else if(numSide==3){
            //[10.0, 1.0, -2.0] ---z
            rows=9;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10.001f,1.7f,-2);
            box.updateGeometry(Vector3f.ZERO, 0.1f, 0.7f, 0.99f);
        }else if(numSide==4){
            //[8.0, 1.0, -10.0] ---x
            rows=9;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(8,1.7f,-9.999f);
            box.updateGeometry(Vector3f.ZERO, 0.99f, 0.7f, 0.1f);
        }else if(numSide==5){
            //[-2.0, 1.0, -10.0] ---x
            rows=13;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(-2,1.7f,-9.999f);
            box.updateGeometry(Vector3f.ZERO, 0.99f, 0.7f, 0.1f);
        }else if(numSide==6){
            //[-10.0, 1.0, -8.0] +++z
            rows=13;
            numPerRows=3;
            xVar=0;
            zVar=1;
            startPoint.set(-9.999f,1.7f,-8);
            box.updateGeometry(Vector3f.ZERO, 0.1f, 0.7f, 0.99f);
        }
        
        
        
        for(int i=0;i<numWindows;i++){
            coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // while(used.contains(coord)){
           //     coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // }
            Vector3f pt= startPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
            //Spatial dirtyWindow =  assetManager.loadModel("Models/dirtyWindow/dirtyWindow.j3o");
            
            
            
            Geometry dirtyWindow = new Geometry("Window_"+numSide+"_"+coord.getY()+"_"+coord.getX(), box);
            dirtyWindow.setLocalTranslation(pt);
            dirtyWindow.setQueueBucket(RenderQueue.Bucket.Transparent);
            dirtyWindow.setMaterial(mat1);
            
            dirtyWindowsNode.attachChild(dirtyWindow);
            
        }
      
    }
    public void generateObjectDropper(int numSide, int numWindows){
        Vector3f animPoint=new Vector3f();
        Vector3f objectDropPoint=new Vector3f();
        Vector2f coord;
        Random rand = new Random();
        float spaceY=3.5f;
        int spaceX=3;
        int xVar=1;
        int zVar=1;
        int rows=0,numPerRows=3;
        Box box=new Box(0.25f,0.25f,0.25f);
        Material mat1 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        
         //windowsNode.removeControl(RigidBodyControl.class);
         objectDropNode.detachAllChildren();
         
        
        
        if(numSide==1){
            //2,1,10   +++x
            rows=6;
            numPerRows=3;
            xVar=1;
            zVar=0;
            animPoint.set(2,1.7f,10.001f);
            objectDropPoint.set(2,1.7f,12);
        }else if(numSide==2){
            //10,1,8   ---z
            rows=6;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            animPoint.set(10.001f,1.7f,8);
            objectDropPoint.set(12,1.7f,8f);
        }else if(numSide==3){
            //[10.0, 1.0, -2.0] ---z
            rows=9;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            animPoint.set(10.001f,1.7f,-2);
            objectDropPoint.set(12,1.7f,-2);
        }else if(numSide==4){
            //[8.0, 1.0, -10.0] ---x
            rows=9;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            animPoint.set(8,1.7f,-9.999f);
            objectDropPoint.set(8,1.7f,-12);
        }else if(numSide==5){
            //[-2.0, 1.0, -10.0] ---x
            rows=13;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            animPoint.set(-2,1.7f,-9.999f);
            objectDropPoint.set(-2,1.7f,-12);
        }else if(numSide==6){
            //[-10.0, 1.0, -8.0] +++z
            rows=13;
            numPerRows=3;
            xVar=0;
            zVar=1;
            animPoint.set(-9.999f,1.7f,-8);
            objectDropPoint.set(-12,1.7f,-8);
        }
        
        
        
        for(int i=0;i<numWindows;i++){
            coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // while(used.contains(coord)){
           //     coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // }
            if(!windowSillList.contains(coord)){
                Vector3f pt= animPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
                //Spatial dirtyWindow =  assetManager.loadModel("Models/dirtyWindow/dirtyWindow.j3o");
                Vector3f dropPt= objectDropPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
                int dropTime=rand.nextInt(6)+1;

                Geometry objectDropper = new Geometry("Window_"+numSide+"_"+coord.getY()+"_"+coord.getX(), box);
                objectDropper.setLocalTranslation(pt);
                objectDropper.setMaterial(mat1);

                objectDropNode.attachChild(objectDropper);
                BabyDropperControl bdc = new BabyDropperControl(app.getAssetManager(),rootNode,app.getPhysicsSpace(),dropPt,dropTime);
                objectDropper.addControl(bdc);
                System.out.println(dropTime);
            }else{
                System.out.println("Bollocks.");
            }
            
            
        }
      
    }
    
    
    public void generateSide(int sideNumber, int numLedge, int numDirtyWindows, int babyDropper){
        generateWindows(sideNumber, numLedge);
        generateDirtyWindows(sideNumber, numDirtyWindows);
        generateObjectDropper(sideNumber, babyDropper);
    }

    
    
    
    public void setLives(int lives) {
        this.lives = lives;
    }
    public int getLives() {
        return lives;
    }
    public PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }
    public void setStartPoint(Vector3f startPoint) {
        this.startPoint = startPoint;
    }
    public BetterCharacterControl getPhysicsCharacter() {
        return physicsCharacter;
    }
    public boolean isRotate() {
        return rotate;
    }
    public boolean isLeftStrafe() {
        return leftStrafe;
    }
    public boolean isRightStrafe() {
        return rightStrafe;
    }
    public boolean isForward() {
        return forward;
    }
    public boolean isBackward() {
        return backward;
    }
    public boolean isLeftRotate() {
        return leftRotate;
    }
    public boolean isRightRotate() {
        return rightRotate;
    }
    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }
    public void setLeftStrafe(boolean leftStrafe) {
        this.leftStrafe = leftStrafe;
    }
    public void setRightStrafe(boolean rightStrafe) {
        this.rightStrafe = rightStrafe;
    }
    public void setForward(boolean forward) {
        this.forward = forward;
    }
    public void setBackward(boolean backward) {
        this.backward = backward;
    }
    public void setLeftRotate(boolean leftRotate) {
        this.leftRotate = leftRotate;
    }
    public void setRightRotate(boolean rightRotate) {
        this.rightRotate = rightRotate;
    }
    
    
}




