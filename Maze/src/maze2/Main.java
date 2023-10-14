/* ****************************************************************
 * Maze! v1.0 - A simple maze with enemies and playable character.
 * ****************************************************************
 *  
 * Main.java - Create the GameFrame containing the GamePanel.
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package maze2;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static Canvas c;
    public static JFrame f;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //construct frame
	f=new JFrame();     
  	f.setTitle("Maze");   
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
	//construct drawing panel
        c=new Canvas();

        //add panels to frame
        f.add(c,BorderLayout.CENTER);
        f.setSize(400,400);
                
        //set frame to centre screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Point p = new Point((tk.getScreenSize().width-f.getWidth())/2,
                            (tk.getScreenSize().height-f.getHeight())/2);       
        
        f.setLocation(p);               
	f.setVisible(true);         
    }
}
