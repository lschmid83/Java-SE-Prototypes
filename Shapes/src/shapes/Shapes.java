/*
 * Shapes.java - Draw a shape o,|,T,[],L,S,0 on the GamePanel which can be 
 *               modified to change color, size and angle                
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import java.util.Random;
import java.awt.geom.*;

public class Shapes {

    private int x;   
    private int y;
    private int dX;  
    private int dY;
    private int w;   
    private int h;
    private int vX;  
    private int vY;
    private int size;
    private int newSize;
    private int angle;
    private int type;
    private Shape[] block = new Shape[4];
    private boolean enlarge;
    private boolean squash;
    private boolean collide;
    private Color normalColor;
    private Color squashColor;
    private Color enlargeColor;
    private Color collideColor;
    private Random generator = new Random();
    private AffineTransform atx;
    private int blocks;

    /**
     * Construct Shapes 
     */  
    public Shapes() {
        size = generator.nextInt(100)+ 70;
        setRandomVelocity();
        squashColor = new Color(180, 20, 16, Settings.OPACITY);
        enlargeColor = new Color(200, 200, 16, Settings.OPACITY);
        collideColor = new Color(0, 20, 180, Settings.OPACITY);
        newSize = size;
        normalColor = new Color(generator.nextInt(105), generator.nextInt(225),
                generator.nextInt(185), Settings.OPACITY);
        type = generator.nextInt(5);
        createShape();
        x = generator.nextInt(Settings.PANEL_W - w - 20);
        y = generator.nextInt(Settings.PANEL_W - h - 20);
    } 
    /**
     * Get squash state 
     */   
    public boolean getSquash() {
        return squash;
    }
    /**
     * Set the sqaush state
     * @param squashing Squash state
     */      
    public void setSquash(boolean squashing) {
        squash = squashing;
    }
     /**
     * Get enlarge state 
     */    
    public boolean getEnlarge() {
        return enlarge;
    }
    /**
     * Set enlarge state 
     * @param enlarging Enlarge state
     */       
    public void setEnlarge(boolean enlarging) {
        enlarge = enlarging;
    }
    /**
     * Get collision state 
     */  
    public boolean getCollision() {
        return collide;
    }
    /**
     * Get collision state 
     * @param collision Collision state
     */    
    public void setCollision(boolean collision) {
       if(!enlarge)
        collide = collision;
    }
    /**
     * Set the size of the shape if new_size < size enlarge else squash 
     * @param new_size New size
     */    
    public void setSize(int new_size) {
        if(new_size < Settings.PANEL_W / 10)
        {
        size = new_size;
        newSize = new_size;
        createShape();
        }
    }    
    /**
     * Get size of shape
     */ 
    public int getSize() {
        return size;
    }
    /**
     * Get type of shape 0=o,1=|,2=T,3=[],4=L,5=S,6=0
     */    
    public int getType() {
        return type;
    }
    /**
     * Set type of shape 0=o,1=|,2=T,3=[],4=L,5=S,6=0
     * @param shape Type of shape
     */ 
    public void setType(int shape) {
        type = shape;
    }
    /**
     * Set the normal color
     * @param shapeColor Normal color
     */ 
    public void setColor(Color shapeColor) {
        normalColor = shapeColor;
    }
    /**
     * Set the color on enlarge
     * @param shapeColor Enlarge color
     */     
    public void setEnlargeColor(Color shapeColor) {
        enlargeColor = shapeColor;
    }
    /**
     * Set the color on collision
     * @param shapeColor Collision color
     */     
    public void setCollisionColor(Color shapeColor) {
        collideColor = shapeColor;
    }
    /**
     * Set the color on squash
     * @param shapeColor Squash color
     */    
    public void setSquashColor(Color shapeColor) {
        squashColor = shapeColor;
    }
    /**
     * Set the velocity
     * @param v Velocity
     */     
    public void setVelocity(int v) {
        vX = v;
        vY = v;
    }
    /**
     * Set the random velocity
     */      
    public void setRandomVelocity()
    {
       if (generator.nextBoolean()) {
            vX = generator.nextInt(Settings.SPEED) + 1;
            vY = generator.nextInt(Settings.SPEED) + 1;
        } else {
            vX = -generator.nextInt(Settings.SPEED) - 1;
            vY = -generator.nextInt(Settings.SPEED) - 1;
       }              
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
        return h;
    }
    /**
     * Get the x position
     */       
    public int getX() {
        return dX;
    }
    /**
     * Set the y position
     */        
    public int getY() {
        return dY;
    }
    /**
     * Set the color
     */      
    public Color getColor()
    {
        return normalColor;   
    }
    /**
     * Squash shape size by amount
     * @param new_size Size
     */      
    public void squash(int new_size) {
        //squash = true;
        newSize = size - new_size;
    }
    /**
     * Enlarge shape by amount
     * @param new_size Size
     */        
    public void enlarge(int new_size) {
        if (size + new_size < Settings.PANEL_W- 200) {
            enlarge = true;
            newSize = size + new_size;
        }
               
    } 
    /**
     * Test for collision with another shapes dimensions
     * @param s Rect dimensions of the object to test for collision
     */     
    public boolean intersects(Shape s)
    {
        if(s.intersects(dX,dY,w,h))
            return true;
        else
            return false;
    }
    /**
     * Create a new shape made of 4 blocks
     */    
    private void createShape() {
        int b = (int) size / 4;
        switch (type) {
            case 0: //o
            {
                block[0] = new Rectangle(dX, dY, b, b);    
                blocks = 0;
                w = b;
                h = b;
                break;
            }                  
            case 1: //|
            {
                block[0] = new Rectangle(dX, dY, b, b);
                block[1] = new Rectangle(dX, dY + b, b, b);
                block[2] = new Rectangle(dX, dY + b * 2, b, b);
                block[3] = new Rectangle(dX, dY + b * 3, b, b);
                w = b;
                h = b * 4;
                blocks = 3;
                break;
            }
            case 2: //T
            {
                block[0] = new Rectangle(dX, dY, b, b);
                block[1] = new Rectangle(dX + b, dY, b, b);
                block[2] = new Rectangle(dX + b, dY + b, b, b);
                block[3] = new Rectangle(dX + b * 2, dY, b, b);
                w = b * 3;
                h = b * 2;
                blocks = 3;
                break;
            }
            case 3: //[]
            {
                block[0] = new Rectangle(dX, dY, b, b);
                block[1] = new Rectangle(dX + b, dY, b, b);
                block[2] = new Rectangle(dX, dY + b, b, b);
                block[3] = new Rectangle(dX + b, dY + b, b, b);
                w = b * 2;
                h = b * 2;
                blocks = 3;
                break;
            }
            case 4: //L
            {
                block[0] = new Rectangle(dX, dY, b, b);
                block[1] = new Rectangle(dX, dY + b, b, b);
                block[2] = new Rectangle(dX, dY + b * 2, b, b);
                block[3] = new Rectangle(dX + b * 1, dY + b * 2, b, b);
                w = b * 2;
                h = b * 3;
                blocks = 3;
                break;
            }
            case 5: //S
            {
                block[0] = new Rectangle(dX, dY + b, b, b);
                block[1] = new Rectangle(dX + b, dY + b, b, b);
                block[2] = new Rectangle(dX + b, dY, b, b);
                block[3] = new Rectangle(dX + b * 2, dY, b, b);
                w = b * 3;
                h = b * 2;
                blocks =3;
                break;
            }
            case 6: //0
            {
                block[0] = new Ellipse2D.Double(dX, dY, b * 2, b * 2);
                block[1] = new Line2D.Double(dX + b, dY, dX + b, dY + b * 2);
                block[2] = new Line2D.Double(dX, dY + b, dX + b * 2, dY + b);
                w = b * 2;
                h = b * 2;
                blocks = 2;
                break;
            }            
        }
    }
    /**
     * Draw the shape on the graphics surface
     * @param g2 The graphics2D object to draw on   
     */      
    public void draw(Graphics2D g2, int cX, int cY) {

        if (size - 2  > newSize) {
            size-=2;//Settings.SPEED;
        } else if (size + 2  < newSize) {
            size+=2;//Settings.SPEED;
        } else {
            squash = false;
            enlarge = false;
        }
        //set draw x,y based on camera position
        dX = x - cX;
        dY = y - cY;
        //set angle for rotation transformation
        if (Settings.ROTATE && angle < 360) 
            angle++;
        else
            angle = 0;
        //set shape colour depending on state
        if (!collide && !squash && !enlarge) //normal
        {
            g2.setColor(normalColor);
        } else if (collide && !squash && !enlarge) //collision
        {
            g2.setColor(collideColor);
        } else if (enlarge && !squash) {
            g2.setColor(enlargeColor);
        } else if (squash) {
            g2.setColor(squashColor);
        }
        //draw shape
        createShape();
        for (int i = 0; i <= blocks; i++) {
            atx = new AffineTransform();
            atx.rotate(angle * Math.PI / 180, dX + (w / 2), dY + (h / 2));
            block[i] = atx.createTransformedShape(block[i]);
            if (Settings.FILL) {
                g2.fill(block[i]);
                g2.draw(block[i]);
            } else {
                g2.draw(block[i]);
            }        
        }
        //move the shapes
        x += vX;
        y += vY;

        if (x < 5) //left
        {
           x = 5;
            vX = -vX; //reverse direction
        } else if (x > (Settings.PANEL_W - w) - 5) {
            x = (Settings.PANEL_W - w) - 5;
            vX = -vX;
        }
        if (y < 5) {
            y = 5;
            vY = -vY;
        } else if (y > (Settings.PANEL_W - h) - 5) {
            y = (Settings.PANEL_H - h) - 5;
            vY = -vY;
        }  
    }
}



