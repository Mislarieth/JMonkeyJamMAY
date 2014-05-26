/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
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
    private Node windowsNode, dirtyWindowsNode,objectDropNode, doorNode, levelNode;
    private Node characterNode;
    private CameraNode camNode;
    boolean rotate = false;
    private float speed=5;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    private Vector3f regenPoint = new Vector3f(-10,0,18);
    private Vector3f doorDirection = new Vector3f(0, 0, 1);
    private Vector3f gameViewDirection = new Vector3f(0, 0, 1).negate();
    private Vector3f maxCamHeight=new Vector3f(0,0,0);
    private Vector3f camLocation=new Vector3f(0,0,0);
    private Quaternion camRotation= new Quaternion();
    float camSpeed=2f;
    
     boolean leftStrafe = false, rightStrafe = false, forward = false, backward = false,
            leftRotate = false, rightRotate = false;
     boolean doorCollision=false, inGame=false, updateCamView=false;
    
    private int lives=3; 
    
    private int level=0;
    /*0==not in one
     * 1=1, 2=2,3=3,4=4,5=5,6=6,7=7
     * once you hit bottom ghost control, set level to level number
     * once you hit top ghost control&&level!=0 you completed it
     */
    
    public MainGame(){
        
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //this.app.enableFlyCam(true);
        
          
          this.app=(Main) app;
          this.rootNode=this.app.getRootNode();
          this.bulletAppState=this.app.getStateManager().getState(BulletAppState.class);
         //bulletAppState.setDebugEnabled(true);
          
          setUpNodes();
          setUpCharacter();
          setUpCam();
          setUpDoors();
          setUpLevels();
       
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
          doorNode=new Node();
          doorNode.setName("Door Node");
          rootNode.attachChild(doorNode);
          levelNode=new Node();
          levelNode.setName("Level Node");
          rootNode.attachChild(levelNode);
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
        physicsCharacter.warp(regenPoint);
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
            //camNode.setEnabled(false);
            //app.setFlycam(true);
            
      }
      private void setUpDoors(){
          GhostControl ghost = new GhostControl(new BoxCollisionShape(new Vector3f(4,1.5f,0.2f)));
          Node node = new Node("LeftDoorBottom");
          node.setLocalTranslation(new Vector3f(-5,1,0));
          node.addControl(ghost); 
          doorNode.attachChild(node);
          getPhysicsSpace().add(ghost);
          
          GhostControl ghost1 = new GhostControl(new BoxCollisionShape(new Vector3f(0.2f,1.5f,4)));
          Node node1 = new Node("RightDoorBottom");
          node1.setLocalTranslation(new Vector3f(0,1,5));
          node1.addControl(ghost1); 
          doorNode.attachChild(node1);
          getPhysicsSpace().add(ghost1);
          
          GhostControl ghost2 = new GhostControl(new BoxCollisionShape(new Vector3f(1,1.5f,0.2f)));
          Node node2 = new Node("LeftDoorTop");
          node2.setLocalTranslation(new Vector3f(-5,8,0));
          node2.addControl(ghost2); 
          doorNode.attachChild(node2);
          getPhysicsSpace().add(ghost2);
          
          GhostControl ghost3 = new GhostControl(new BoxCollisionShape(new Vector3f(0.2f,1.5f,1)));
          Node node3 = new Node("RightDoorTop");
          node3.setLocalTranslation(new Vector3f(0,8,5));
          node3.addControl(ghost3); 
          doorNode.attachChild(node3);
          getPhysicsSpace().add(ghost3);
          
          GhostControl ghost4 = new GhostControl(new BoxCollisionShape(new Vector3f(1,1.5f,0.2f)));
          Node node4 = new Node("Side1Door");
          node4.setLocalTranslation(new Vector3f(5,22,0));
          node4.addControl(ghost4); 
          doorNode.attachChild(node4);
          getPhysicsSpace().add(ghost4);
          
          GhostControl ghost5 = new GhostControl(new BoxCollisionShape(new Vector3f(0.2f,1.5f,1)));
          Node node5 = new Node("Side2Door");
          node5.setLocalTranslation(new Vector3f(0,32.5f,-5));
          node5.addControl(ghost5); 
          doorNode.attachChild(node5);
          getPhysicsSpace().add(ghost5);
          
          GhostControl ghost6 = new GhostControl(new BoxCollisionShape(new Vector3f(0.2f,1.5f,1)));
          Node node6 = new Node("Side3Door");
          node6.setLocalTranslation(new Vector3f(-3.5f,46.5f,-5));
          node6.addControl(ghost6); 
          doorNode.attachChild(node6);
          getPhysicsSpace().add(ghost6);
      }
      public void setUpLevels(){
          //startPoint.set(2,4.5f,10);
          GhostControl ghost = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,0.75f)));
          Node node = new Node("Level1Bottom");
          node.setLocalTranslation(new Vector3f(5,1,11));
          node.addControl(ghost); 
          doorNode.attachChild(node);
          getPhysicsSpace().add(ghost);
          
          GhostControl ghost1 = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,1f)));
          Node node1 = new Node("Level1Top");
          node1.setLocalTranslation(new Vector3f(5,24,10));
          node1.addControl(ghost1); 
          doorNode.attachChild(node1);
          getPhysicsSpace().add(ghost1);
          
          //startPoint.set(10,4.5f,8);
          GhostControl ghost2 = new GhostControl(new BoxCollisionShape(new Vector3f(0.75f,3,5)));
          Node node2 = new Node("Level2Bottom");
          node2.setLocalTranslation(new Vector3f(11,1,5));
          node2.addControl(ghost2); 
          doorNode.attachChild(node2);
          getPhysicsSpace().add(ghost2);
          
          GhostControl ghost3 = new GhostControl(new BoxCollisionShape(new Vector3f(1f,3,5)));
          Node node3 = new Node("Level2Top");
          node3.setLocalTranslation(new Vector3f(10,24,5));
          node3.addControl(ghost3); 
          doorNode.attachChild(node3);
          getPhysicsSpace().add(ghost3);
          
          GhostControl ghost4 = new GhostControl(new BoxCollisionShape(new Vector3f(0.75f,3,5)));
          Node node4 = new Node("Level3Bottom");
          node4.setLocalTranslation(new Vector3f(11,1,-5f));
          node4.addControl(ghost4); 
          doorNode.attachChild(node4);
          getPhysicsSpace().add(ghost4);
          
          GhostControl ghost5 = new GhostControl(new BoxCollisionShape(new Vector3f(1f,3,5)));
          Node node5 = new Node("Level3Top");
          node5.setLocalTranslation(new Vector3f(10,34.5f,-5f));
          node5.addControl(ghost5); 
          doorNode.attachChild(node5);
          getPhysicsSpace().add(ghost5);
          
          //[8.0, 1.0, -10.0] ---x
          GhostControl ghost6 = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,0.75f)));
          Node node6 = new Node("Level4Bottom");
          node6.setLocalTranslation(new Vector3f(5,1,-11));
          node6.addControl(ghost6); 
          doorNode.attachChild(node6);
          getPhysicsSpace().add(ghost6);
          
          GhostControl ghost7 = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,1f)));
          Node node7 = new Node("Level4Top");
          node7.setLocalTranslation(new Vector3f(5,34.5f,-10));
          node7.addControl(ghost7); 
          doorNode.attachChild(node7);
          getPhysicsSpace().add(ghost7);
          
          
          GhostControl ghost10 = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,0.75f)));
          Node node10 = new Node("Level5Bottom");
          node10.setLocalTranslation(new Vector3f(-5,1,-11));
          node10.addControl(ghost10); 
          doorNode.attachChild(node10);
          getPhysicsSpace().add(ghost10);
          
          GhostControl ghost11 = new GhostControl(new BoxCollisionShape(new Vector3f(5,3,1f)));
          Node node11 = new Node("Level5Top");
          node11.setLocalTranslation(new Vector3f(-5,48.5f,-10));
          node11.addControl(ghost11); 
          doorNode.attachChild(node11);
          getPhysicsSpace().add(ghost11);
          
          GhostControl ghost12 = new GhostControl(new BoxCollisionShape(new Vector3f(1f,3,5)));
          Node node12 = new Node("Level6Bottom");
          node12.setLocalTranslation(new Vector3f(-11,1,-5));
          node12.addControl(ghost12); 
          doorNode.attachChild(node12);
          getPhysicsSpace().add(ghost12);
          
          GhostControl ghost13 = new GhostControl(new BoxCollisionShape(new Vector3f(1f,3,5)));
          Node node13 = new Node("Level6Top");
          node13.setLocalTranslation(new Vector3f(-10,48.5f,-5));
          node13.addControl(ghost13); 
          doorNode.attachChild(node13);
          getPhysicsSpace().add(ghost13);
      }
      
      
      Vector3f camChange=new Vector3f();
      @Override
    public void update(float tpf) {
          // Get current forward and left vectors of model by using its rotation
        // to rotate the unit vectors
          if (leftRotate) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (rightRotate) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }
        if(doorCollision){
            viewDirection.set(doorDirection);
            doorCollision=false;
            
        }
        if(inGame){
            viewDirection.set(gameViewDirection);
        }
        physicsCharacter.setViewDirection(viewDirection);
          
          
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
        if (forward&&!inGame) {
            walkDirection.addLocal(modelForwardDir.mult(speed));
        } else if (backward&&!inGame) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(speed));
        }
        physicsCharacter.setWalkDirection(walkDirection);

        
        if(updateCamView){
            characterNode.detachChild(camNode);
            rootNode.attachChild(camNode);
            camNode.setLocalTranslation(camLocation);
            camNode.setLocalRotation(camRotation);
            updateCamView=false;
        }
        if(inGame){
            camChange.set(camNode.getLocalTranslation());
            if(camChange.getY()<=maxCamHeight.getY()){
                camChange.addLocal(new Vector3f(0,camSpeed*tpf,0));
            }
            camNode.setLocalTranslation(camChange);
            if(physicsCharacter.getPhysicsLocation().distance(camChange)>=12){
                lives=0;
            }
        }
        
        
        
        if(lives<=0){
            physicsCharacter.warp(regenPoint);
            lives=3;
            camNode.setLocalTranslation(camLocation);
        }
    }

    
      
      
      
      
      @Override
      public void stateDetached(AppStateManager stateManager) {
          app.getRootNode().detachChild(rootNode);
          app.getRootNode().detachChild(windowsNode);
          app.getRootNode().detachChild(dirtyWindowsNode);
          app.getRootNode().detachChild(objectDropNode);
          app.getRootNode().detachChild(doorNode);
          /*app.getInputManager().removeListener(actionListener);
          app.getInputManager().deleteMapping("left");
          app.enableFlyCam(false);*/
      }
    
    @Override
    public void cleanup() {
        super.cleanup();
 
    }
    
    
    
    
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
        int rows=1,numPerRows=3;
        Material mat1 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //mat1.setBoolean("UseMaterialColors", true);
        mat1.setColor("Color", new ColorRGBA(0.5764706f,0.42352942f,0.41960785f,1));
        RigidBodyControl rbc = new RigidBodyControl(0);
        /* A colored lit cube. Needs light source! */ 
        
         //windowsNode.removeControl(RigidBodyControl.class);
         windowsNode.detachAllChildren();
         
        
        
        if(numSide==1){
            //2,1,10   +++x
            rows=5;
            numPerRows=3;
            xVar=1;
            zVar=0;
            startPoint.set(2,4.5f,10);
            gameViewDirection.set(Vector3f.UNIT_X.negate());
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==2){
            //10,1,8   ---z
            rows=5;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10,4.5f,8);
            gameViewDirection.set(Vector3f.UNIT_Z);
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
        }else if(numSide==3){
            //[10.0, 1.0, -2.0] ---z
            rows=8;
            numPerRows=3;
            xVar=0;
            zVar=-1;
            startPoint.set(10,4.5f,-2);
            gameViewDirection.set(Vector3f.UNIT_Z);
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
        }else if(numSide==4){
            //[8.0, 1.0, -10.0] ---x
            rows=8;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(8,4.5f,-10);
            gameViewDirection.set(Vector3f.UNIT_X);
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==5){
            //[-2.0, 1.0, -10.0] ---x
            rows=12;
            numPerRows=3;
            xVar=-1;
            zVar=0;
            startPoint.set(-2,4.5f,-10);
            gameViewDirection.set(Vector3f.UNIT_X);
            box.updateGeometry(Vector3f.ZERO, 1, 0.25f, 2);
        }else if(numSide==6){
            //[-10.0, 1.0, -8.0] +++z
            rows=12;
            numPerRows=3;
            xVar=0;
            zVar=1;
            startPoint.set(-10,4.5f,-8);
            gameViewDirection.set(Vector3f.UNIT_Z.negate());
            box.updateGeometry(Vector3f.ZERO, 2, 0.25f, 1);
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
            Vector3f dropPt= objectDropPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
           // while(used.contains(coord)){
           //     coord=new Vector2f(rand.nextInt(numPerRows),rand.nextInt(rows));
           // }
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(dropPt, Vector3f.UNIT_Y.mult(-1));
            // 3. Collect intersections between Ray and Shootables in results list.
            windowsNode.collideWith(ray, results);
            if(results.size()>1){
               if(results.getClosestCollision().getDistance()>=1){
                   Vector3f pt= animPoint.add(new Vector3f(xVar*coord.getX()*spaceX,coord.getY()*spaceY,zVar*coord.getX()*spaceX));
                   //Spatial dirtyWindow =  assetManager.loadModel("Models/dirtyWindow/dirtyWindow.j3o");

                   int dropTime=rand.nextInt(6)+1;

                   Geometry objectDropper = new Geometry("Window_"+numSide+"_"+coord.getY()+"_"+coord.getX(), box);
                   objectDropper.setLocalTranslation(pt);
                   objectDropper.setMaterial(mat1);

                   objectDropNode.attachChild(objectDropper);
                   BabyDropperControl bdc = new BabyDropperControl(app.getAssetManager(),rootNode,app.getPhysicsSpace(),dropPt,dropTime);
                   objectDropper.addControl(bdc);
               }else{
                    System.out.println("Bollocks.");
               }
            }
            
        }
      
    }
    public void generateUserStuffs(int numSide){
        
        if(numSide==1){
            //2,1,10   +++x
            regenPoint.set(5f,0.5f,11.5f);
            gameViewDirection.set(Vector3f.UNIT_Z.negate());
            camRotation.lookAt(Vector3f.UNIT_Z.negate(), Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(0,0,7));
            maxCamHeight.set(camLocation.add(0, 24, 0));
            
            
        }else if(numSide==2){
            //10,1,8   ---z
            regenPoint.set(11.5f,1f,5);
            gameViewDirection.set(Vector3f.UNIT_X.negate());
            camRotation.lookAt(Vector3f.UNIT_X.negate(), Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(7,0,0));
            maxCamHeight.set(camLocation.add(0, 24, 0));
        }else if(numSide==3){
            //[10.0, 1.0, -2.0] ---z
            regenPoint.set(11.5f,1f,-5);
            gameViewDirection.set(Vector3f.UNIT_X.negate());
            camRotation.lookAt(Vector3f.UNIT_X.negate(), Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(7,0,0));
            maxCamHeight.set(camLocation.add(0, 34.5f, 0));
        }else if(numSide==4){
            //[8.0, 1.0, -10.0] ---x
            regenPoint.set(5,1f,-11.5f);
            gameViewDirection.set(Vector3f.UNIT_Z);
            camRotation.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(0,0,-7));
            maxCamHeight.set(camLocation.add(0, 34.5f, 0));
        }else if(numSide==5){
            //[-2.0, 1.0, -10.0] ---x
           regenPoint.set(-5,1f,-11.5f);
            gameViewDirection.set(Vector3f.UNIT_Z);
            camRotation.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(0,0,-7));
            maxCamHeight.set(camLocation.add(0, 48.5f, 0));
        }else if(numSide==6){
            //[-10.0, 1.0, -8.0] +++z
            regenPoint.set(-11.5f,1f,-5);
            gameViewDirection.set(Vector3f.UNIT_X);
            camRotation.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
            camLocation.set(regenPoint.add(-7,0,0));
            maxCamHeight.set(camLocation.add(0, 48.5f, 0));
        }
        updateCamView=true;
        
    }
    
    public void emptyNodes(){
        if(windowsNode.getControl(RigidBodyControl.class)!=null){
            
            app.getPhysicsSpace().remove(windowsNode);
            windowsNode.removeControl(RigidBodyControl.class);
        }
        windowsNode.detachAllChildren();
        dirtyWindowsNode.detachAllChildren();
        objectDropNode.detachAllChildren();
    }
    
    public void generateSide(int sideNumber, int numLedge, int numDirtyWindows, int babyDropper){
        generateWindows(sideNumber, numLedge);
        generateDirtyWindows(sideNumber, numDirtyWindows);
        generateObjectDropper(sideNumber, babyDropper);
        generateUserStuffs(sideNumber);
    }
    public void cleanupLevel(){
        setLevel(0);
        emptyNodes();
        setInGame(false);
        camNode.setLocalTranslation(new Vector3f(0, 2, -6));
        Quaternion quat = new Quaternion();
        // These coordinates are local, the camNode is attached to the character node!
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        camNode.setLocalRotation(quat);
        rootNode.detachChild(camNode);
        characterNode.attachChild(camNode);
    }
    public void setViewDirection(Vector3f dir){
        viewDirection=dir;
        physicsCharacter.setViewDirection(dir);
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
    public void setRegenPoint(Vector3f startPoint) {
        this.regenPoint = startPoint;
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
    public boolean isDoorCollision() {
        return doorCollision;
    }
    public void setDoorCollision(boolean doorCollision) {
        this.doorCollision = doorCollision;
    }
    public boolean isInGame() {
        return inGame;
    }
    public void setInGame(boolean doorCollision) {
        this.inGame = doorCollision;
    }
    public void setGameDirection(Vector3f dir) {
        this.gameViewDirection = dir;
    }
    public void setDoorDirection(Vector3f dir) {
        this.doorDirection = dir;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
    public void regen(){
        physicsCharacter.warp(regenPoint);
    }
    
}




