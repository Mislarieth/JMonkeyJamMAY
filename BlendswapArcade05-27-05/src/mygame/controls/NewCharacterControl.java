/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.AbstractPhysicsControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author User
 */
public class NewCharacterControl extends AbstractPhysicsControl{
    protected PhysicsRigidBody rigidBody;
    protected float radius;
    protected float height;
    protected float mass;
    protected Spatial model;
    
    protected final Vector3f localUp = new Vector3f(0, 1, 0);
    protected final Vector3f localForward = new Vector3f(0, 0, 1);
    protected final Vector3f localLeft = new Vector3f(1, 0, 0);
    protected final Quaternion localForwardRotation = new Quaternion(Quaternion.DIRECTION_Z);
    protected final Vector3f viewDirection = new Vector3f(0, 0, 1);

    protected final Vector3f location = new Vector3f();
    
    
    
    
    
    @Override
    protected void createSpatialData(Spatial spat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void removeSpatialData(Spatial spat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void setPhysicsLocation(Vector3f vec) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void setPhysicsRotation(Quaternion quat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void addPhysics(PhysicsSpace space) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void removePhysics(PhysicsSpace space) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
