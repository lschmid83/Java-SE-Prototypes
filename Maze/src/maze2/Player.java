/*
 * Player.java - Draw moveable player on the graphics surface.
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package maze2;

import java.awt.*;
import java.awt.Point;

public class Player {

    private int x;
    private int y;

    /**
     * Constructor
     */
    public Player() {
        x = 0;
        y = 0;
    }

    /**
     * Set position of the player 
     * @param p X,Y value of the object
     */
    public void setPos(Point p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Get the position of the player
     * @return X,Y value of the object
     */
    public Point getPos() {
        return new Point(x,y);
    }

    /**
     * Move player 1 square left
     */
    public void moveLeft() {
        x--;
    }

    /**
     * Move player 1 square right
     */
    public void moveRight() {
        x++;
    }

    /**
     * Move player 1 square up
     */
    public void moveUp() {
        y--;
    }

    /**
     * Move player 1 square down
     */
    public void moveDown() {
        y++;
    }

    /**
     * Draw player on the graphic surface
     * @param g2 The buffered surface to draw on
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillRect(x*16+3, y*16+3 ,10 ,10);
    }
}
