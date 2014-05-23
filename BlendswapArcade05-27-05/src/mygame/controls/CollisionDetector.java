/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author User
 */
public class CollisionDetector extends AbstractAppState implements PhysicsCollisionListener{
    
    
    protected PhysicsSpace physicsSpace;
    private Node rootNode;
    
    public CollisionDetector(Node rootNode,PhysicsSpace physicsSpace){
        this.rootNode=rootNode;
        this.physicsSpace=physicsSpace;
    }
    
    public void collision(PhysicsCollisionEvent event) {
        /*System.out.println(event.getNodeA().getName());
        System.out.println(event.getNodeB().getName());*/
        if (event.getNodeA().getName() == null || event.getNodeB().getName() == null) return;
        if ( event.getNodeA().getName().equals("BABY") ) {
            Spatial node =  event.getNodeA();
            physicsSpace.remove(node);
            rootNode.detachChild(node);
            
        } else if ( event.getNodeB().getName().equals("BABY")) {
            Spatial node = (Node) event.getNodeB();
            physicsSpace.remove(node);
            rootNode.detachChild(node);
        }
    }

}
