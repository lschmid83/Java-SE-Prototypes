

/*
 * MapFrame.java - JFrame containing the MapPanel which is used to scroll 
 *                 the gamePanel area 
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MapFrame extends JFrame implements 
        WindowListener  {

    public static MapPanel mPanel = null;

    /**
     * Construct MapFrame 
     */      
    public MapFrame() {
        setTitle("Map");
        //construct drawing panel
        mPanel = new MapPanel(Settings.PANEL_W, Settings.PANEL_W);
        mPanel.setSize(Settings.VIEW_PORT, Settings.VIEW_PORT);  
        //add panels to frame
        add(mPanel, BorderLayout.CENTER);
        addWindowListener(this);
        setSize(150, 150);
        setResizable(false);  
    } 

    /**
     * Change settings panel map visible 
     */     
    public void windowClosing(java.awt.event.WindowEvent e) {
         Main.frame.sPanel.setMapVisibleCheck(false);
    };
    
    public void actionPerformed(java.awt.event.ActionEvent e) {};
    public void windowActivated(java.awt.event.WindowEvent e) {};
    public void windowClosed(java.awt.event.WindowEvent e) {};    
    public void windowDeactivated(java.awt.event.WindowEvent e) {};
    public void windowDeiconified(java.awt.event.WindowEvent e) {};
    public void windowIconified(java.awt.event.WindowEvent e) {};
    public void windowOpened(java.awt.event.WindowEvent e) {};  
}