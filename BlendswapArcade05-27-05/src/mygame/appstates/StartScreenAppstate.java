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
import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;
import com.jme3.scene.Node;
import tonegod.gui.style.StyleManager;


/**
 *
 * @author User
 */
public class StartScreenAppstate extends AbstractAppState{
    private Main app;
    private Node rootNode;
    
    private MotionPath path;
    private MotionEvent cameraMotionControl;
    private CameraNode camNode;
    private Vector3f center= new Vector3f(0,0,0);
    private Vector3f bottom= new Vector3f(0,0,0);
    int index=0;
    private Vector3f top= new Vector3f(0,50,0);
    private boolean goingUp=true;
    
    public int winCount = 0;
    private Screen screen;
    public Window win;
    
    public StartScreenAppstate(){
        /*this.app = app;
        this.screen = screen;*/
    }
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //this.app.enableFlyCam(true);
        
          
          this.app=(Main) app;
          this.rootNode=this.app.getRootNode();
          
          setupPlanet();
          setUpCam();
          setUpGui();
       
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
      
      public void setUpGui(){
        screen = new Screen(app);
        screen.initialize();
        screen.setUseCustomCursors(true);
        app.addGuiControl(screen);
        
        // Add window
        win = new Window(screen, "MainMenu", new Vector2f(screen.getWidth()/2, screen.getHeight()/2), new Vector2f(500, 160));
        
        // Window position is top-left based, move window to center
        win.moveTo(win.getX()-(win.getWidth()/2), win.getY()-(win.getHeight()/2));
        
        // set other misc attrs of window
        //win.setFont("/Path/To/Font");
        win.setIsMovable(false);
        win.setWindowIsMovable(false);
        win.setResizeN(false);
        win.setResizeS(false);
        win.setResizeE(false);
        win.setResizeW(false);
        win.setWindowTitle("Window Washer");
        
        // create button and add to window
        int buttonPadding = 40;
        ButtonAdapter btn1 = new ButtonAdapter( screen, "Btn1", new Vector2f((win.getWidth()/2)-((win.getWidth()-20)/2), 35),  new Vector2f(win.getWidth()-20, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                startGame();
            }
        };
        btn1.setText("Play");
        
        ButtonAdapter btn2 = new ButtonAdapter( screen, "Btn2", new Vector2f((win.getWidth()/2)-((win.getWidth()-20)/2), 35+(buttonPadding)),  new Vector2f(win.getWidth()-20, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                createNewWindow("YOU HAVE 110 SWEG MESSAGES");
            }
        };
        btn2.setText("Options");
        
        ButtonAdapter btn3 = new ButtonAdapter( screen, "Btn3", new Vector2f((win.getWidth()/2)-((win.getWidth()-20)/2), 35+(buttonPadding*2)),  new Vector2f(win.getWidth()-20, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                stopGame();
            }
        };
        btn3.setText("Exit");
        
        // Add it to out initial window
        win.addChild(btn1);
        win.addChild(btn2);
        win.addChild(btn3);

        // Add window to the screen
       screen.addElement(win);
    }
      
      public final void createNewWindow(String someWindowTitle) {
        AlertBox nWin = new AlertBox(screen,"Window" + winCount,new Vector2f( screen.getWidth()/2, screen.getHeight()/2)) {
            String uid="Window"+(winCount);
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(screen.getElementById(uid));
            }
        };
        nWin.setWindowTitle("YOLO SWEG");
        nWin.setMsg(someWindowTitle);
        nWin.setButtonOkText("Close");
        nWin.setResizeN(false);
        nWin.setResizeE(false);
        nWin.setResizeS(false);
        nWin.setResizeW(false);
        nWin.setWindowIsMovable(false);
        nWin.moveTo(nWin.getX()-(nWin.getWidth()/2), nWin.getY()-(nWin.getHeight()/2));
        
        
        screen.addElement(nWin);
        winCount++;
    }
      
    public void startGame(){
        if(screen.getElementById("MainMenu") != null){
            screen.removeElement(screen.getElementById("MainMenu"));
        }
        screen.setUseCustomCursors(false);
        app.startGame(screen);
    }
    
    public void stopGame(){
        app.stopGame();
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




