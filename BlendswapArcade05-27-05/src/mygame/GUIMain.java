package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author User
 */
public class GUIMain extends SimpleApplication{
    
    public int winCount = 0;
    private Screen screen;

    
    public GUIMain(Screen screen){
        this.screen = screen;
    }
    
    public final void createNewWindow(String someWindowTitle) {
        AlertBox nWin = new AlertBox(this.screen,"Window" + winCount,new Vector2f( this.screen.getWidth()/2, this.screen.getHeight()/2)) {
            String uid="Window"+(winCount);
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(screen.getElementById(uid));
            }
        };
        nWin.setWindowTitle("Error");
        nWin.setMsg(someWindowTitle);
        nWin.setButtonOkText("Close");
        nWin.setResizeN(false);
        nWin.setResizeE(false);
        nWin.setResizeS(false);
        nWin.setResizeW(false);
        nWin.setWindowIsMovable(false);
        nWin.moveTo(nWin.getX()-(nWin.getWidth()/2), nWin.getY()-(nWin.getHeight()/2));
        
        
        this.screen.addElement(nWin);
        winCount++;
    }
    
    /*public final void createNewWindow(String someWindowTitle) {
        Window nWin = new Window(screen,"Window"+count,new Vector2f( (screen.getWidth()/2)-175, (screen.getHeight()/2)-100 )
        );
        nWin.setWindowTitle(someWindowTitle);
        screen.addElement(nWin);
        count++;
    }*/
    
   @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        
        setUpGui();
    }
    
    public void setUpGui(){
        /*screen = new Screen(this);*/
        this.screen.initialize();
        this.screen.setUseCustomCursors(true);
        guiNode.addControl(this.screen);
        // Add window
        Window win = new Window(this.screen, "Login", new Vector2f(15, 15));
        win.setIsMovable(false);
        win.setWindowIsMovable(false);
        win.setResizeN(false);
        win.setResizeS(false);
        win.setResizeE(false);
        win.setResizeW(false);
        // create button and add to window
        ButtonAdapter btn1 = new ButtonAdapter( this.screen, "Btn1", new Vector2f(15, 55),  new Vector2f(15, 55)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                createNewWindow("Failure to Connect to Server");
            }
        };
        btn1.setText("Swag");
        
        ButtonAdapter btn2 = new ButtonAdapter( this.screen, "YOLO BTN", new Vector2f(75, 55) ) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                createNewWindow("YOOLOLOLOLOL");
            }
        };
        btn2.setText("Swag");
        
        ButtonAdapter btn3 = new ButtonAdapter( this.screen, "YOLO BTN", new Vector2f(75, 55) ) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                createNewWindow("YOOLOLOLOLOL");
            }
        };
        btn3.setText("Swag");

        // Add it to out initial window
        win.addChild(btn1);
        win.addChild(btn2);

        // Add window to the screen
       this.screen.addElement(win);
    }
}