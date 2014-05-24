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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.ChaseCamera;
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
public class StartScreenAppstate extends AbstractAppState{
    private Main app;
    private Node rootNode;
    
     private MotionPath path;
    private MotionEvent cameraMotionControl;
    private ChaseCamera chaser;
    private CameraNode camNode;
    private Vector3f center= new Vector3f(0,0,0);
    private Vector3f bottom= new Vector3f(0,0,0);
    int index=0;
    private Vector3f top= new Vector3f(0,50,0);
    private boolean goingUp=true;
    
    public StartScreenAppstate(){
        
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //this.app.enableFlyCam(true);
        
          
          this.app=(Main) app;
          this.rootNode=this.app.getRootNode();
          
          setupPlanet();
          setUpCam();  
       
    }
    
      @Override
      public void stateAttached(AppStateManager stateManager) {
          
      }
      public void setupPlanet() {
        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(Vector3f.UNIT_XYZ.negate());
        rootNode.addLight(dl);
        
        app.addRigidBodyModelAsset("Scenes/MainLevel/MainScene.j3o", "Main Scene", Vector3f.ZERO,0);
        
        
        
    }
     
      private void setUpCam(){
            camNode = new CameraNode("CamNode", app.getCamera());
            camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
            camNode.setLocalTranslation(new Vector3f(0, 2, -6));
            path = new MotionPath();
        path.setCycle(true);
        path.addWayPoint(new Vector3f(20, 6, 20));
        path.addWayPoint(new Vector3f(0, 9, 30));
        path.addWayPoint(new Vector3f(-40, 15, 0));
        path.addWayPoint(new Vector3f(0, 21, -50));
        path.addWayPoint(new Vector3f(60, 27, 0));
        path.addWayPoint(new Vector3f(0, 40, 70));
        path.addWayPoint(new Vector3f(-80, 50, 0));
        path.addWayPoint(new Vector3f(0, 60, -90));
        path.addWayPoint(new Vector3f(20, 50, 0));
        path.addWayPoint(new Vector3f(0, 40, 30));
        path.addWayPoint(new Vector3f(-40, 27, 0));
        path.addWayPoint(new Vector3f(0, 21, -50));
        path.addWayPoint(new Vector3f(45, 15, 0));
        path.addWayPoint(new Vector3f(0, 9, 40));
        path.addWayPoint(new Vector3f(-30, 6, 20));
        path.setCurveTension(0.83f);


        cameraMotionControl = new MotionEvent(camNode, path);
        cameraMotionControl.setSpeed(0.1f);
        cameraMotionControl.setLoopMode(LoopMode.Loop);
        cameraMotionControl.setLookAt(center, Vector3f.UNIT_Y);
        cameraMotionControl.setDirectionType(MotionEvent.Direction.LookAt);

        rootNode.attachChild(camNode);
        path.addListener(new MotionPathListener() {

            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                index=wayPointIndex;
            }
        });
        cameraMotionControl.play();

      }
      
      @Override
    public void update(float tpf) {
       
             
             center.interpolate(new Vector3f(center.x,path.getWayPoint(index).y,center.z), 0.1f*tpf);
        
    }

    
      
      
      
      
      @Override
      public void stateDetached(AppStateManager stateManager) {
          app.getRootNode().detachChild(camNode);
      }
    
    @Override
    public void cleanup() {
        super.cleanup();
 
    }
    
    
}




