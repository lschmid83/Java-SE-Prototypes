/*
 * Earthquake.java - Draw an earthquake on the GamePanel in ratio to the size. 
 *                   Earthquake is setVisible() when the user presses the right 
 *                   moused button during the game.
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import java.awt.geom.*;

public class Earthquake {

    private int size;
    private Color color;
    private boolean visible;
    private int timer;
    private float x;
    private float y;
    private int w;
    private int h;
    private Shape wave;

    /**
     * Construct Earthquake 
     */   
    public Earthquake() {
        color = new Color(255, 0, 0, 80);
        wave = new Ellipse2D.Double(x - timer, y - timer, size + (timer * 2),
                size + (timer * 2));
        size = 0;
        x = 50;
        y = 50;
    }

    /** Get visibility 
     */   
    public boolean getVisible() {
        return visible;
    }
    
    /**
     * Get area used to check for intersections with other objects 
     */   
    public Shape getArea(){return wave;}
    
    /**
     * Set visibility
     * @param new_visible Visibility 
     */       
    public void setVisible(boolean new_visible) {
        timer = 0;
        visible = new_visible;
    }

    /**
     * Set earthquakes x position
     * @param new_x Position of earthquake 
     */     
    public void setX(float new_x) {
        x = new_x;
    }
    
    /**
     * Set earthquakes y position
     * @param new_y Position of earthquake      
     */  
    public void setY(float new_y) {
        y = new_y;
    }
    
    /**
     * Get the width 
     */      
    public int getWidth() {
        return w;
    }
   
    /**
     * Get the height
     */      
    public int getHeight() {
        return w;
    }
    
    /**
     *Get the x position
     */    
    public float getX() {
        return x;
    }

    /**
     * Get the y position
     */       
    public float getY() {
        return y;
    }
    
    /**
     * Draw the earthquake on the graphics surface
     * @param g2 The graphics2D object to draw on   
     */        
    public void draw(Graphics2D g2) {
        if (visible) {

            g2.setColor(new Color(255, 0, 0, Settings.OPACITY));
            w = size + (timer*2);
            h = size + (timer*2);                 
            wave = new Ellipse2D.Float(x - timer, y - timer, size + (timer * 2),
                    size + (timer * 2));
            g2.draw(wave);
            if (timer < Settings.PANEL_W / 6) {
                timer += Settings.SPEED ;
            } else {
                timer = 0;
                visible = false;
            }

        } else {
            timer = 0;
        }
    }
}
