/*
 * GameFrame.java - JFrame containing the GamePanel and SettingsPanel
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    public static GamePanel gPanel = null;
    public static SettingsPanel sPanel = null;

    /**
     * Construct GameFrame 
     */     
    public GameFrame() {
        setTitle("Shapes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //construct settings panel
        sPanel = new SettingsPanel();
        sPanel.setSize(200, 512);

        //construct drawing panel
        gPanel = new GamePanel(Settings.PANEL_W, Settings.PANEL_W);
        gPanel.setSize(Settings.VIEW_PORT, Settings.VIEW_PORT);
        gPanel.start(); //start game     

        //add panels to frame
        add(gPanel, BorderLayout.CENTER);
        add(sPanel, BorderLayout.EAST);
        setSize(gPanel.getWidth()+sPanel.getWidth() - 17,gPanel.getHeight()+33);
  
       // sPanel.setVisible(false);
       // setSize(400, 400);
        sPanel.setVisible(true);
        //setSize(512 + Main.frame.sPanel.getWidth(),540);
        
        centerScreen();       
        setVisible(true);        
    }
    
    /**
    * Center the frame for the screen resolution
    */   
    public void centerScreen()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int x = ((toolkit.getScreenSize().width - getWidth()) / 2)- 125;
        int y = (toolkit.getScreenSize().height - getHeight()) / 2;
        setLocation(x, y);     
        Main.map.setLocation(x + getWidth() + 40, (y + getHeight()) 
                - Main.map.getHeight());  
    }        
               
}
