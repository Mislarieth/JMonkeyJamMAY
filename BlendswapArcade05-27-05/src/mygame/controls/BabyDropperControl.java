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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import java.io.IOException;

/**
 *
 * @author User
 */
public class BabyDropperControl implements Control {
    
    
    private int updateTime=5;
    private Spatial spatial;
    private boolean enabled=true;
    private Sphere sphere;
    private Material mat1;
    private RigidBodyControl ball_phy;
    
    private Node rootNode;
    protected PhysicsSpace physicsSpace;
    private Vector3f pos;
    
    private float timing=0;
    
    public BabyDropperControl(){
    }
    public BabyDropperControl(Spatial spatial){
       this.setSpatial(spatial);
    }
    public BabyDropperControl(AssetManager assetManager, Node rootNode, PhysicsSpace physicsSpace, Vector3f pos, int timing){
       
        sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
        mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        this.rootNode=rootNode;
        this.physicsSpace=physicsSpace;
        this.pos=pos;
        this.updateTime=timing;
    }
    
    public void makeCannonBall(Node rootNode, PhysicsSpace physicsSpace, Vector3f pos) {
        Node ballNode = new Node();
        ballNode.setName("BABY");
        Geometry ball_geo = new Geometry("BABY", sphere);
        ball_geo.setMaterial(mat1);
        ballNode.attachChild(ball_geo);
        ball_geo.setLocalTranslation(pos);
        ball_phy = new RigidBodyControl(1f);
        rootNode.attachChild(ballNode);
        ballNode.addControl(ball_phy);
        
        physicsSpace.add(ballNode);
      }
    
    
    public void update(float tpf) {
        if(timing>=updateTime){
            makeCannonBall(rootNode, physicsSpace, pos);
            timing=0;
            System.out.println("Walrus");
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
