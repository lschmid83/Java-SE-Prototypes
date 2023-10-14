/*
 * MapPanel.java - JPanel draws the game map which shows the view port position
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Vector;

public class MapPanel extends JPanel implements  
        MouseMotionListener,KeyListener {

    private static Vector<Shapes> shapes = null;
    private int width;
    private int height;
    private float mouseX;
    private float mouseY;
    private BufferedImage img = null;
    private Image bg = null;
    private Graphics2D sg;
    public static boolean resized;   
    float vX;
    float vY;
    float vS;
   
    /**
     * Construct MapPanel 
     */       
    public MapPanel(int new_width, int new_height) {
        super(new BorderLayout());
        width = new_width;
        height = new_height;
        resized = false;
        setImg();
        vX = 0;
        vY = 0;
        vS = Settings.VIEW_PORT;
        shapes =new Vector();      
        mouseX = ((float) Settings.PANEL_W/2) / (float) Settings.PANEL_W;
        mouseY = ((float) Settings.PANEL_H/2) / (float) Settings.PANEL_H;
        
        ImageIcon imgicon = new ImageIcon(this.getClass().
                getClassLoader().getResource("shapes2dgame/bg/bg1.jpg"));
        bg = imgicon.getImage();
  
        addMouseMotionListener(this);
        addKeyListener(this);

        setFocusable(true);
        repaint();
    }
  
    /**
     * Pass key press event to the GamePanel
     */    
    public void keyPressed(KeyEvent e) {
        Main.frame.gPanel.keyPressed(e);               
    }  
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}   
    
    /**
     *  Move viewport around the GamePanel
     */  
    public void mouseDragged(MouseEvent e) {
        requestFocusInWindow();
        mouseX = ((float) e.getX() / (float) getWidth());
        mouseY = (float) e.getY() / (float) getHeight();
        vS = Settings.VIEW_PORT;
        vX = (mouseX * Settings.PANEL_W) - vS /2 ;
        vY = (mouseY * Settings.PANEL_H) - vS /2;        
        
        if(vX < -10)
           vX = 0;
 
        if(vY < -10)
           vY = 0;         
            
       if(vX + vS > Settings.PANEL_W)
            vX = (Settings.PANEL_W - vS) ;
   
        if(vY + vS > Settings.PANEL_H)
            vY = (Settings.PANEL_H - vS);    
        
        GamePanel.setCamera((int)vX, (int)vY);  
        repaint();
    }
     
    public void mouseMoved(MouseEvent e) {}  

    /**
     * Set the image used for the double buffer to the size of the panel
     */   
    public void setImg() { 
   
    	this.img=new BufferedImage(Settings.PANEL_W,Settings.PANEL_H,
                BufferedImage.TYPE_INT_RGB);
        sg=(Graphics2D)img.getGraphics();
        sg.setBackground(Color.white);    
        sg.setStroke(new BasicStroke(8));
        sg.setColor(new Color(0, 0, 0, Settings.OPACITY));           
        repaint();   
    }
    

    /**
     * Draw the map panel graphics
     */  
    protected void paintComponent(Graphics g) {
    
        vX = GamePanel.cX;
        vY = GamePanel.cY;
        vS = Settings.VIEW_PORT; 
       
        if(vX < -10)
           vX = 0;
 
        if(vY < -10)
           vY = 0;         
            
        if(vX + vS > Settings.PANEL_W)
            vX = (Settings.PANEL_W - vS) ;
   
        if(vY + vS > Settings.PANEL_H)
            vY = (Settings.PANEL_H - vS);          
        
        shapes = Main.frame.gPanel.getShapes();

        if(resized)
        {
            setImg();
            resized = false;
        }
        
        //set width / height of drawPanel to the panelSize
        width = Settings.PANEL_W;
        height = Settings.PANEL_H;
        
        sg.clearRect(0, 0, width, height);   
        //sg.drawRect(5, 5, width-10, height-10);
 
        for (int i = 0; i < shapes.size() - 1; i++) {           
         sg.drawRect(shapes.get(i).getX(), shapes.get(i).getY(), 
                 shapes.get(i).getWidth(), shapes.get(i).getHeight());  
        }
  
        
        if(Main.frame.gPanel.quake.getVisible())
        {
            sg.setColor(new Color(0,0,255,Settings.OPACITY));
            sg.drawOval((int)Main.frame.gPanel.quake.getX() - 
                    Main.frame.gPanel.quake.getWidth() /2, 
                    (int)Main.frame.gPanel.quake.getY() -
                    Main.frame.gPanel.quake.getHeight()/2 , 
                        Main.frame.gPanel.quake.getWidth(),
                        Main.frame.gPanel.quake.getHeight());            
            sg.setColor(new Color(0,0,0,Settings.OPACITY));
        }
        sg.draw(new Rectangle.Float(vX, vY, vS, vS));
         
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(),getHeight(), this);
        }
    }
}







