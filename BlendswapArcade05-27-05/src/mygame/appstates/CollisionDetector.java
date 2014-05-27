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
import com.jme3.math.Vector3f;
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
    private Vector3f charLoc=Vector3f.ZERO;
    
    
    
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
        charLoc=app.getMainGameAppstate().getPhysicsCharacter().getPhysicsLocation();
        if(event.getNodeA()!=null&&event.getNodeB()!=null){
            if (event.getNodeA().getName() != null && event.getNodeB().getName() != null){
                String nodeAName=event.getNodeA().getName();
                String nodeBName=event.getNodeB().getName();

                if ( nodeAName.equals("BABY")) {
                    Spatial node =  event.getNodeA();
                    if ( nodeBName.equals("character node")){
                        if(!app.getMainGameAppstate().isUseSammich()){
                            int lives=app.getStateManager().getState(MainGame.class).getLives();
                            app.getStateManager().getState(MainGame.class).setLives(lives-1);
                            System.out.println("BUGEREJG");
                        }
                        
                    }
                    if(node!=null){
                        app.getMainGameAppstate().objectHit(node.getWorldTranslation());
                        physicsSpace.remove(node);
                        rootNode.detachChild(node);
                    }
                } else if ( nodeBName.equals("BABY")) {
                    Spatial node = (Node) event.getNodeB();
                    if ( nodeAName.equals("character node")){
                        if(!app.getMainGameAppstate().isUseSammich()){
                            int lives=app.getStateManager().getState(MainGame.class).getLives();
                            app.getStateManager().getState(MainGame.class).setLives(lives-1);
                            System.out.println("BUGEREJG");
                        }
                    }
                    if(node!=null){
                        app.getMainGameAppstate().objectHit(node.getWorldTranslation());
                        physicsSpace.remove(node);
                        rootNode.detachChild(node);
                    }
                }

                if ( nodeAName.equals("LeftDoorBottom")) {
                    Spatial node =  event.getNodeA();
                    if ( nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(0,6f,1.5f)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_Z);
                        app.getMainGameAppstate().setInShop(true);
                    }
                } else if ( nodeBName.equals("LeftDoorBottom")) {
                    Spatial node = (Node) event.getNodeB();
                    if ( nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(0,6f,1.5f)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_Z);
                        app.getMainGameAppstate().setInShop(true);
                    }

                }
                if ( nodeAName.equals("LeftDoorTop")) {
                    Spatial node =  event.getNodeA();
                    if ( nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(0,-8,1.5f)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_Z);
                        app.getMainGameAppstate().setInShop(false);
                    }


                } else if (nodeBName.equals("LeftDoorTop")) {
                    Spatial node = (Node) event.getNodeB();
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(0,-8,1.5f)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_Z);
                        app.getMainGameAppstate().setInShop(false);
                    }
                }

                if ( nodeAName.equals("RightDoorBottom")) {
                    Spatial node =  event.getNodeA();
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(-1.5f,6f,0)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(true);
                    }
                } else if (nodeBName.equals("RightDoorBottom")) {
                    Spatial node = (Node) event.getNodeB();
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(new Vector3f(-1.5f,7,5));

                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(true);
                    }
                }
                if (nodeAName.equals("RightDoorTop")) {
                    Spatial node =  event.getNodeA();
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(-1.5f,-8f,0)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(false);
                    }
                } else if (nodeBName.equals("RightDoorTop")) {
                    Spatial node = (Node) event.getNodeB();
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(node.getLocalTranslation().add(new Vector3f(-1.5f,-8f,0)));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(false);
                    }
                }
                if (nodeAName.equals("Side1Door")||nodeAName.equals("Side2Door")||nodeAName.equals("Side3Door")) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(new Vector3f(-1.5f,7,5));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(true);
                    }
                } else if (nodeBName.equals("Side1Door")||nodeBName.equals("Side2Door")||nodeBName.equals("Side3Door")) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).getPhysicsCharacter().warp(new Vector3f(-1.5f,7,5));
                        app.getStateManager().getState(MainGame.class).setDoorCollision(true);
                        app.getStateManager().getState(MainGame.class).setDoorDirection(Vector3f.UNIT_X.mult(-1));
                        app.getMainGameAppstate().setInShop(true);

                    }
                }

                if (nodeAName.equals("Level1Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        
                        app.getStateManager().getState(MainGame.class).setLevel(1);
                        app.getMainGameAppstate().generateSide(1,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(11.5f));
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                } else if (nodeBName.equals("Level1Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(1);
                        app.getMainGameAppstate().generateSide(1,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(11.5f));
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                }
                if (nodeAName.equals("Level1Top")) {
                    if (nodeBName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(500);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 22, 8));
                         }
                    }
                } else if (nodeBName.equals("Level1Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(500);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 22, 8));
                         }
                        
                    }
                }


                if (nodeAName.equals("Level2Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(2);
                        app.getMainGameAppstate().generateSide(2,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(11.5f));
                        app.getMainGameAppstate().setCamSpeed(2);
                    }
                } else if (nodeBName.equals("Level2Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(2);
                        app.getMainGameAppstate().generateSide(2,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(11.5f));
                        app.getMainGameAppstate().setCamSpeed(2);
                    }
                }
                if (nodeAName.equals("Level2Top")) {
                    if (nodeBName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(750);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 22, 8));
                         }
                    }
                } else if (nodeBName.equals("Level2Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(750);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(8, 22, charLoc.getZ()));
                         }
                    }
                }


                if (nodeAName.equals("Level3Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(3);
                        app.getMainGameAppstate().generateSide(3,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(11.5f));
                    
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                } else if (nodeBName.equals("Level3Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(3);
                        app.getMainGameAppstate().generateSide(3,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(11.5f));
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                }
                if (nodeAName.equals("Level3Top")) {
                    if (nodeBName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1000);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(8, 32, charLoc.getZ()));
                         }
                    }
                } else if (nodeBName.equals("Level3Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1000);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(8, 32, charLoc.getZ()));
                         }
                    }
                }


                if (nodeAName.equals("Level4Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(4);
                        app.getMainGameAppstate().generateSide(4,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(2f);
                    }
                } else if (nodeBName.equals("Level4Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(4);
                        app.getMainGameAppstate().generateSide(4,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(2f);
                    }
                }
                if (nodeAName.equals("Level4Top")) {
                    if (nodeBName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1250);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 32, -8));
                         }
                    }
                } else if (nodeBName.equals("Level4Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1250);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 32, -8));
                         }
                    }
                }




                if (nodeAName.equals("Level5Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(5);
                        app.getMainGameAppstate().generateSide(5,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                } else if (nodeBName.equals("Level5Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(5);
                        app.getMainGameAppstate().generateSide(5,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setZ(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(1.5f);
                    }
                }
                if (nodeAName.equals("Level5Top")) {
                    if (nodeBName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1500);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 46, -8));
                         }
                    }
                } else if (nodeBName.equals("Level5Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(1500);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(charLoc.getX(), 46, -8));
                         }
                    }
                }


                if (nodeAName.equals("Level6Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeBName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(6);
                        app.getMainGameAppstate().generateSide(6,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(2f);
                    }
                } else if (nodeBName.equals("Level6Bottom")&&app.getStateManager().getState(MainGame.class).getLevel()==0) {
                    if (nodeAName.equals("character node")){
                        app.getStateManager().getState(MainGame.class).setLevel(6);
                        app.getMainGameAppstate().generateSide(6,10,10,4);
                        app.getMainGameAppstate().setInGame(true);
                        app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.setX(-11.5f));
                        app.getMainGameAppstate().setCamSpeed(2f);
                    }
                }
                if (nodeAName.equals("Level6Top")) {
                    if (nodeBName.equals("character node")){
                        if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(2000);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(-8, 46, charLoc.getZ()));
                        }
                    }
                } else if (nodeBName.equals("Level6Top")) {
                    if (nodeAName.equals("character node")){
                         if(app.getMainGameAppstate().isInGame()){
                            app.getMainGameAppstate().cleanupLevel();
                            app.getMainGameAppstate().addPoints(2000);
                            app.getMainGameAppstate().getPhysicsCharacter().warp(charLoc.set(-8, 46, charLoc.getZ()));
                         }
                    }
                }
            }
        }
    }

}
