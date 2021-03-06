package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.util.concurrent.Callable;
import mygame.appstates.CollisionDetector;
import mygame.appstates.InputAppstate;
import mygame.appstates.MainGame;
import mygame.appstates.StartScreenAppstate;
import tonegod.gui.core.Screen;

/**
 * A walking physical character followed by a 3rd person camera. (No animation.)
 *
 * @author normenhansen, zathras
 */
public class Main extends SimpleApplication{
    private BulletAppState bulletAppState;
    private InputAppstate inputAppState;
   
    
   
    private MainGame mainGameAppstate;
    private StartScreenAppstate startScreen;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // activate physics
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        
        //bulletAppState.setBroadphaseType(PhysicsSpace.BroadphaseType.SIMPLE);
        
        stateManager.attach(bulletAppState);
        
        CollisionDetector collisionAppState = new CollisionDetector();
        stateManager.attach(collisionAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(collisionAppState);
        // init a physics test scene
        
        mainGameAppstate=new MainGame();
        
        startScreen=new StartScreenAppstate();
        this.setScreenState(startScreen);
        
        inputAppState= new InputAppstate(this);
        stateManager.attach(inputAppState);

        // Create a node for the character model
        

        // Set forward camera node that follows the character, only used when
        // view is "locked"
        
        // Disable by default, can be enabled via keyboard shortcut
        //camNode.setEnabled(false);
        flyCam.setMoveSpeed(20);
        flyCam.setEnabled(false);
        //bulletAppState.setDebugEnabled(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    
    
    public void addModelAsset(final String fileLoc, final String name, final Vector3f loc){
        enqueue(new Callable() {

            public Object call() throws Exception {
                try{
                    Node node = (Node) getAssetManager().loadModel(fileLoc);
                    node.setName(name);
                    rootNode.attachChild(node);
                    
                }catch (NullPointerException e){
                    System.out.println("Specified object does not exist");
                }
                return null;
            }


        });
    }
    /*public void addRigidBodyModelAsset(final String fileLoc, final String name, final Vector3f loc, final float mass){
        enqueue(new Callable() {

            public Object call() throws Exception {
                try{
                    Node node = (Node) getAssetManager().loadModel(fileLoc);
                    node.setName(name);
                    node.addControl(new RigidBodyControl(mass));
                    rootNode.attachChild(node);
                    getPhysicsSpace().add(node);
                }catch (NullPointerException e){
                    System.out.println("Specified object does not exist");
                }
                return null;
            }


        });
    }*/


    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
    public BulletAppState getBulletAppState(){
        return bulletAppState;
    }

    

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public MainGame getMainGameAppstate() {
        return mainGameAppstate;
    }
    
    
    
    
    //sets the appstate
    //used by external appstates at the destruction of itself
    public int gameScreen=0;
    public void setScreenState(AbstractAppState state){
        if(state instanceof MainGame){
            if(gameScreen==0){
                stateManager.detach(stateManager.getState(StartScreenAppstate.class));
            }
            gameScreen = 1;
        }/*else if(state instanceof InGame){
            if(gameScreen==0){
                stateManager.detach(uslog);
            }
            gameScreen=1;
        }*/
        stateManager.attach(state);
    }

    public int getGameScreen() {
        return gameScreen;
    }
    public void setFlycam(boolean enable){
        flyCam.setEnabled(enable);
    }
    
    public void addGuiControl(Screen screen){
        guiNode.addControl(screen);
    }
    
    public void startGame(Screen screen){
        guiNode.removeControl(screen);
        this.setScreenState(mainGameAppstate);
    }
    public void stopGame(){
        this.stop();
    }
    public AppSettings getSettings(){
        return settings;
    }
    
}
