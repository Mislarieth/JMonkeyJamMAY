/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author User
 */
public class BabyDropperControl implements Control {
    
    
    private int updateTime=5;
    private Spatial spatial;
    private boolean enabled=true;
    private Box box;
    private Material mat1;
    
    private Node rootNode;
    protected PhysicsSpace physicsSpace;
    private Vector3f pos;
    private Random random;
    private Spatial ducky,crate,pot;
    
    private float timing=0;
    
    public BabyDropperControl(){
    }
    public BabyDropperControl(Spatial spatial){
       this.setSpatial(spatial);
    }
    public BabyDropperControl(AssetManager assetManager, Node rootNode, PhysicsSpace physicsSpace, Vector3f pos){
        random=new Random();
        box = new Box(0.5f, 0.5f, 0.5f);
        mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setTexture("DiffuseMap",assetManager.loadTexture("Models/Crates/Crate1.png"));
       
        ducky =  assetManager.loadModel("Models/Rubber Duck/Rubber Duck.j3o");
        ducky.scale(0.5f);
        ducky.setName("BABY");
        pot =  assetManager.loadModel("Models/Flower pot/Flower pot.j3o");
        pot.scale(0.75f);
        pot.setName("BABY");
        crate = new Geometry("BABY",box);
        crate.setMaterial(mat1);
        
        this.rootNode=rootNode;
        this.physicsSpace=physicsSpace;
        this.pos=pos;
        this.updateTime=random.nextInt(5)+5;
    }
    
    public void makeCannonBall(Node rootNode, PhysicsSpace physicsSpace, Vector3f pos) {
        Node ballNode = new Node();
        ballNode.setName("BABY");
        Spatial geometry = new Node();
        geometry.setName("BABY");
        int i = random.nextInt(3)+1;
        if(i==1){
            geometry=ducky;
        }else if(i==2){
            geometry=pot;
        }else if(i==3){
            geometry=crate;
        }
        ballNode.attachChild(geometry);
       RigidBodyControl ball_phy = new RigidBodyControl(1f);
       
        rootNode.attachChild(ballNode);
        ballNode.addControl(ball_phy);
        
        physicsSpace.add(ballNode);
        ball_phy.setPhysicsLocation(pos);
        ball_phy.clearForces();
        ball_phy.setLinearVelocity(Vector3f.UNIT_Y.mult(-1));
      }
    
    
    public void update(float tpf) {
        if(timing>=updateTime){
            makeCannonBall(rootNode, physicsSpace, pos);
            timing=0;
        }
        timing+=tpf;
    }
    
    
    public void setSpatial(Spatial spatial) {
        this.spatial=spatial;
    }
    
    
    
    
    
    
    public Control cloneForSpatial(Spatial spatial) {
        BabyDropperControl bdc = new BabyDropperControl(spatial);
        
        return bdc;
    }    
    public void render(RenderManager rm, ViewPort vp) {
    }
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
