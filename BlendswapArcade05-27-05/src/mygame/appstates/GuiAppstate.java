/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import mygame.Main;

/**
 *
 * @author User
 */
public class GuiAppstate extends AbstractAppState{
    private Main app;
    
    public GuiAppstate(){
    }
    
     @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
          
          this.app=(Main) app;
       
    }
    
      @Override
      public void stateAttached(AppStateManager stateManager) {
          
      }
    @Override
    public void update(float tpf) {
         
    }

    
      
      
      
      
      @Override
      public void stateDetached(AppStateManager stateManager) {
          
      }
    
        @Override
        public void cleanup() {
            super.cleanup();

        }
    
   
    

}
