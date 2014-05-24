/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.state.AbstractAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
                new KeyTrigger(KeyInput.KEY_U),
                new KeyTrigger(KeyInput.KEY_Z));
        app.getInputManager().addMapping("Strafe Right",
                new KeyTrigger(KeyInput.KEY_O),
                new KeyTrigger(KeyInput.KEY_X));
        app.getInputManager().addMapping("Rotate Left",
                new KeyTrigger(KeyInput.KEY_J),
                new KeyTrigger(KeyInput.KEY_LEFT));
        app.getInputManager().addMapping("Rotate Right",
                new KeyTrigger(KeyInput.KEY_L),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        app.getInputManager().addMapping("Walk Forward",
                new KeyTrigger(KeyInput.KEY_I),
                new KeyTrigger(KeyInput.KEY_UP));
        app.getInputManager().addMapping("Walk Backward",
                new KeyTrigger(KeyInput.KEY_K),
                new KeyTrigger(KeyInput.KEY_DOWN));
        app.getInputManager().addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_F),
                new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addMapping("Duck",
                new KeyTrigger(KeyInput.KEY_G),
                new KeyTrigger(KeyInput.KEY_LSHIFT),
                new KeyTrigger(KeyInput.KEY_RSHIFT));
        app.getInputManager().addMapping("Lock View",
                new KeyTrigger(KeyInput.KEY_RETURN));
        app.getInputManager().addMapping("GenTest",
                new KeyTrigger(KeyInput.KEY_V));
        app.getInputManager().addListener(this, "Strafe Left", "Strafe Right");
        app.getInputManager().addListener(this, "Rotate Left", "Rotate Right");
        app.getInputManager().addListener(this, "Walk Forward", "Walk Backward");
        app.getInputManager().addListener(this, "Jump", "Duck", "Lock View");
        app.getInputManager().addListener(this, "GenTest");
    }
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Strafe Left")) {
            if(app.getGameScreen()==1){
                if (value) {
                app.getMainGameAppstate().setLeftStrafe(true);
            } else {
                app.getMainGameAppstate().setLeftStrafe(false);
            }
            }
            
        } else if (binding.equals("Strafe Right")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().setRightStrafe(true);
                } else {
                    app.getMainGameAppstate().setRightStrafe(false);
                }
            }
        } else if (binding.equals("Rotate Left")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().setLeftRotate(true);
                } else {
                    app.getMainGameAppstate().setLeftRotate(false);
                }
            }
        } else if (binding.equals("Rotate Right")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().setRightRotate(true);
                } else {
                    app.getMainGameAppstate().setRightRotate(false);
                }
            }
        } else if (binding.equals("Walk Forward")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().setForward(true);
                } else {
                    app.getMainGameAppstate().setForward(false);
                }
            }
        } else if (binding.equals("Walk Backward")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().setBackward(true);
                } else {
                    app.getMainGameAppstate().setBackward(false);
                }
            }
        } else if (binding.equals("Jump")) {
            if(app.getGameScreen()==1){
               app.getMainGameAppstate().getPhysicsCharacter().jump();
            }else if(app.getGameScreen()==0){
                app.setScreenState(app.getMainGameAppstate());
            }
        } else if (binding.equals("Duck")) {
            if(app.getGameScreen()==1){
                
                if (value) {
                    app.getMainGameAppstate().getPhysicsCharacter().setDucked(true);
                } else {
                    app.getMainGameAppstate().getPhysicsCharacter().setDucked(false);
                }
            }
        }else if (binding.equals("GenTest")) {
            if(app.getGameScreen()==1){
                if (value) {
                    app.getMainGameAppstate().generateSide(1,10,10,4);
                } else {

                }
            }
            
        }
    }
    
}
