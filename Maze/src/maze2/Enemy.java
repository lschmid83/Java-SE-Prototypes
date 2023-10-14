/*
 * Enemy.java - Draw enemy with AI on the graphics surface.
 */
/**
 * @author Lawrence Schmid
 */

package maze2;

import java.awt.*;
import java.awt.Point;
import java.util.Random;


public class Enemy {

    private int x;
    private int y; 
    private int d;      //direction

    private Random r; 
    
    /**
     * Constructor
     */
    public Enemy() {
        r = new Random();
        x = 0;
        y = 0;
        d = r.nextInt(3);
    }

    /**
     * Set position of the enemy
     * @param p The X,Y value of the object 
     */
    public void setPos(Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Get position of the enemy
     * @return The X,Y value of the object 
     */
    public Point getPos() {
        Point p = new Point();
        p.x = x;
        p.y = y;
        return p;
    }
    
    /**
     * Move object in a different direction on collision with another object
     * @param m The map containing object definitions
     */
    public void move(Map m) {
        
        //set direction on collision          
        if (m.getPiece(new Point(x-1,y))> 0 && d==0 || x-1 < 0) { //left
            d=1;
        }
        else if (m.getPiece(new Point(x+1,y))>0 && d==1 || 
            x+2 > m.getSize().x ) { //right
            d=0;
        }
        else if (m.getPiece(new Point(x,y-1)) > 0 && d==2 || y-1 < 0 ) { //up
            d=3;        
        }
        else if (m.getPiece(new Point(x,y+1)) > 0 && d==3 ||
                 y+2 > m.getSize().y ) { //down
            d=2;
        }
        
        //move in direction
        if (d==0 && m.getPiece(new Point(x-1,y))!=1) { //left
            x--;
        } else if (d==1 && m.getPiece(new Point(x+1,y))!=1) { //right
            x++;
        } else if (d==2 && m.getPiece(new Point(x,y-1))!=1) { //up
            y--;
        } else if (d==3 && m.getPiece(new Point(x,y+1))!=1) { //down
            y++;
        }
    }

    /**
     * Draw enemy on the graphic surface
     * @param g2 The buffered surface to draw on
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(x * 16 + 3, y * 16 + 3, 10, 10);
    }
}
