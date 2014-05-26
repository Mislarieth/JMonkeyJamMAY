/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import mygame.Main;

/**
 *
 * @author User
 */
public class InputAppstate extends AbstractAppState implements ActionListener {
    Main app;
    
    public InputAppstate(Main app){
        this.app=app;
        setupKeys();
    }
    
    private void setupKeys() {
        app.getInputManager().addMapping("Strafe Left",
                new KeyTrigger(KeyInput.KEY_R));
        app.getInputManager().addMapping("Strafe Right",
                new KeyTrigger(KeyInput.KEY_F));
        app.getInputManager().addMapping("Rotate Left",
                new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addMapping("Rotate Right",
                new KeyTrigger(KeyInput.KEY_D));
        app.getInputManager().addMapping("Walk Forward",
                new KeyTrigger(KeyInput.KEY_W));
        app.getInputManager().addMapping("Walk Backward",
                new KeyTrigger(KeyInput.KEY_S));
        app.getInputManager().addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addMapping("Duck",
                new KeyTrigger(KeyInput.KEY_RSHIFT));
        app.getInputManager().addMapping("Lock View",
                new KeyTrigger(KeyInput.KEY_LSHIFT));
        app.getInputManager().addMapping("GenTest",
                new KeyTrigger(KeyInput.KEY_C));
        app.getInputManager().addListener(this, "Strafe Left", "Strafe Right");
        app.getInputManager().addListener(this, "Rotate Left", "Rotate Right");
        app.getInputManager().addListener(this, "Walk Forward", "Walk Backward");
        app.getInputManager().addListener(this, "Jump", "Duck", "Lock View");
        app.getInputManager().addListener(this, "GenTest");
        app.getInputManager().addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(this, "Shoot");
    }
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Strafe Left")) {
            if(app.getGameScreen()==1){
                    if (value) {
                        if(!app.getMainGameAppstate().isUseSammich()&&app.getMainGameAppstate().getNumSammiches()>0){
                            app.getMainGameAppstate().setUseSammich(true);
                            app.getMainGameAppstate().setNumSammiches(app.getMainGameAppstate().getNumSammiches()-1);
                            System.out.println(app.getMainGameAppstate().getNumSammiches());
                        }
                    } else {
                        //app.getMainGameAppstate().setLeftStrafe(false);
                    }
            }
            
        } else if (binding.equals("Strafe Right")) {
            if(app.getGameScreen()==1){
                if (value) {
                    if(!app.getMainGameAppstate().isUsePot()&&app.getMainGameAppstate().getNumPots()>0){
                        app.getMainGameAppstate().setUsePot(true);
                        app.getMainGameAppstate().setNumPots(app.getMainGameAppstate().getNumPots()-1);
                        System.out.println(app.getMainGameAppstate().getNumPots());
                    }
                } else {
                    //app.getMainGameAppstate().setRightStrafe(false);
                }
            }
        } else if (binding.equals("Rotate Left")) {
            if(app.getGameScreen()==1){
                if(app.getStateManager().getState(MainGame.class).getLevel()==0){
                    app.getMainGameAppstate().setLeftStrafe(false);
                    if (value) {
                        app.getMainGameAppstate().setLeftRotate(true);
                    } else {
                        app.getMainGameAppstate().setLeftRotate(false);
                    }
                }else{
                    if (value) {
                        app.getMainGameAppstate().setLeftStrafe(true);
                    } else {
                        app.getMainGameAppstate().setLeftStrafe(false);
                    }
                }
            }
        } else if (binding.equals("Rotate Right")) {
            if(app.getGameScreen()==1){
                if(app.getStateManager().getState(MainGame.class).getLevel()==0){
                    app.getMainGameAppstate().setRightStrafe(false);
                    if (value) {
                        app.getMainGameAppstate().setRightRotate(true);
                    } else {
                        app.getMainGameAppstate().setRightRotate(false);
                    }
                }else{
                    if (value) {
                        app.getMainGameAppstate().setRightStrafe(true);
                    } else {
                        app.getMainGameAppstate().setRightStrafe(false);
                    }
                }
            }
        } else if (binding.equals("Walk Forward")) {
            if(app.getGameScreen()==1){
                if(app.getStateManager().getState(MainGame.class).getLevel()==0){
                    if (value) {
                        app.getMainGameAppstate().setForward(true);
                        if(app.getStateManager().getState(MainGame.class).getLevel()!=0){
                            app.getMainGameAppstate().setForward(false);
                        }
                    } else {
                        app.getMainGameAppstate().setForward(false);
                    }
                }else{
                    app.getMainGameAppstate().setForward(false);
                }
            }
        } else if (binding.equals("Walk Backward")) {
            if(app.getGameScreen()==1){
                if(app.getStateManager().getState(MainGame.class).getLevel()==0){
                    if (value) {
                        app.getMainGameAppstate().setBackward(true);
                        if(app.getStateManager().getState(MainGame.class).getLevel()!=0){
                            app.getMainGameAppstate().setBackward(false);
                        }
                    } else {
                        app.getMainGameAppstate().setBackward(false);
                    }
                }else{
                    app.getMainGameAppstate().setBackward(false);
                }
            }
        } else if (binding.equals("Jump")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().getPhysicsCharacter().jump();
                } else {
                    //app.getMainGameAppstate().setBackward(false);
                }
               
            }else if(app.getGameScreen()==0){
                //app.setScreenState(app.getMainGameAppstate());
            }
        } else if (binding.equals("Duck")) {
            if(app.getGameScreen()==1){
                
                if (value) {
                   // app.getMainGameAppstate().getPhysicsCharacter().setDucked(true);
                } else {
                    //app.getMainGameAppstate().getPhysicsCharacter().setDucked(false);
                }
            }
        }else if (binding.equals("GenTest")) {
            if(app.getGameScreen()==1){
                if (value) {
                    if(app.getMainGameAppstate().getLevel()!=0){
                        app.getMainGameAppstate().generateSide(app.getMainGameAppstate().getLevel(),10,10,4);
                    }
                    
                } else {

                }
            }
            
        }else if (binding.equals("Lock View")) {
            if(app.getGameScreen()==1){
                app.getStateManager().getState(MainGame.class).setLives(0);
                  
            }
            
        } 
        if(binding.equals("Shoot")){
            if (value) {
                if(app.getGameScreen()==1){
                    app.getMainGameAppstate().cleanWindow();

                }
               
            } else {

            }
        }
    }
    
    @Override
      public void stateDetached(AppStateManager stateManager) {
            app.getInputManager().removeListener(this);
            app.getInputManager().deleteMapping("Strafe Left");
            app.getInputManager().deleteMapping("Strafe Right");
            app.getInputManager().deleteMapping("Rotate Left");
            app.getInputManager().deleteMapping("Rotate Right");
            app.getInputManager().deleteMapping("Walk Forward");
            app.getInputManager().deleteMapping("Walk Backward");
            app.getInputManager().deleteMapping("Jump");
            app.getInputManager().deleteMapping("Duck");
            app.getInputManager().deleteMapping("Lock View");
            app.getInputManager().deleteMapping("GenTest");
      }
    
}
