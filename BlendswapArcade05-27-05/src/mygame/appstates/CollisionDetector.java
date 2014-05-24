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
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;

/**
 *
 * @author User
 */
public class CollisionDetector extends AbstractAppState implements PhysicsCollisionListener{
    
    private Main app;
    protected PhysicsSpace physicsSpace;
    private Node rootNode;
    
    
    
    public CollisionDetector(){
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //this.app.enableFlyCam(true);
        
          
          this.app=(Main) app;
          this.rootNode=this.app.getRootNode();
          this.physicsSpace=this.app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
          
       
    }
    
    public void collision(PhysicsCollisionEvent event) {
        /*System.out.println(event.getNodeA().getName());
        System.out.println(event.getNodeB().getName());*/
        if (event.getNodeA().getName() != null || event.getNodeB().getName() != null){
            if ( event.getNodeA().getName().equals("BABY")) {
                Spatial node =  event.getNodeA();
                if ( event.getNodeB().getName().equals("character node")){
                    int lives=app.getStateManager().getState(MainGame.class).getLives();
                    app.getStateManager().getState(MainGame.class).setLives(lives-1);
                    System.out.println(app.getStateManager().getState(MainGame.class).getLives());
                }
                if(node!=null){
                    physicsSpace.remove(node);
                    rootNode.detachChild(node);
                }


            } else if ( event.getNodeB().getName().equals("BABY")) {
                Spatial node = (Node) event.getNodeB();
                if ( event.getNodeA().getName().equals("character node")){
                    int lives=app.getStateManager().getState(MainGame.class).getLives();
                    app.getStateManager().getState(MainGame.class).setLives(lives-1);
                    System.out.println(app.getStateManager().getState(MainGame.class).getLives());
                }
                if(node!=null){
                    physicsSpace.remove(node);
                    rootNode.detachChild(node);
                }

            }
        }
    }

}
