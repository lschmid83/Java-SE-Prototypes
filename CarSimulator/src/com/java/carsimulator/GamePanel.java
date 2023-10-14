package com.java.carsimulator;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.awt.event.*;

/**
    This class draws the graphics and collision detection between the car and other objects.
	
	@version 1.0
	@modified 1/02/2011
	@author Lawrence Schmid<BR><BR>
	
	This file is part of Car Simulator.<BR><BR>
	
	Car Simulator is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.<BR><BR>
	
	Car Simulator is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.<BR><BR>
	
	You should have received a copy of the GNU General Public License
	along with Car Simulator. If not, see http://www.gnu.org/licenses/.<BR><BR>
	
	Copyright 2012 Lawrence Schmid
*/

public class GamePanel extends JPanel implements KeyListener, MouseListener,
        MouseMotionListener, MouseWheelListener, Runnable {

	private BufferedImage map;
	private BufferedImage collisionMap;
	private BufferedImage steering;
	private Camera cam;
	private float zoom = 0;
	private Car car;
	private Camera mouse;
    /** The main game thread */    
    private Thread thread;	  
    /** The image that will contain everything that has been drawn on bufferGraphics */ //http://www.realapplets.com/tutorial/DoubleBuffering.html
    private BufferedImage offscreen;
    /** The object we will use to write with instead of the standard screen graphics */
    private Graphics2D bufferGraphics;   
    /** The object we will use to write the objects on the game map */
    private Graphics2D mapGraphics; 
    /** Distinguishes the class when it is serialized and deserialized */
    public final static long serialVersionUID = 3000000;
    
    private BufferedImage mapImage;

	/**
	 * Constructs the GamePanel
	 */
    public GamePanel() {
   	
    	//create double buffer graphics
        offscreen = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        bufferGraphics = (Graphics2D) offscreen.getGraphics();
       
        //create game objects
        cam = new Camera(0, 0);
        zoom = 0;
        collisionMap = new Image("res/collision map.png").getImage();  
        mapImage = new Image("res/map.png").getImage();
        map = new BufferedImage(mapImage.getWidth(), mapImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        mapGraphics = (Graphics2D) map.getGraphics();       
        car = new Car(848, 2315);
        mouse = new Camera(0,0);
        steering = new Image("res/steering.png").getImage();
   	
        //clear map background
        mapGraphics.setColor(Color.WHITE);
    	mapGraphics.fillRect(0, 0, mapImage.getWidth(), mapImage.getHeight());
        
    	//enable anti-aliasing
        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   
                            RenderingHints.VALUE_ANTIALIAS_ON);  
        
        //add event listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setFocusable(true);
        requestFocus();
    }
    
    /**
     * Starts the main game thread
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Repaint the game panel in a thread
     */
    public void run() {
    	
    	//http://www.csc.kth.se/utbildning/kth/kurser/DH2640/grip07/pdf/cargame-updated.pdf
    	//---------------------------------------------------------------------------------
		long time = System.currentTimeMillis();
		while (true) {
			// Update position, velocity etc. of vehicles
			long t = System.currentTimeMillis();
			long dt = t - time;
			//float secs = (float) dt / 1000.0f; // Convert to seconds
			car.update(dt);		
			time = System.currentTimeMillis();
			repaint();
			// Sleep for a short amount of time to allow the system to catch up.
			// This improves framerate substantially and avoids hiccups
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}		
		} 
		//---------------------------------------------------------------------------------
    }

	/**
	 * Render the graphics
	 * @param gl The graphics context
	 */
    public void paintComponent(Graphics g) {
   	
    	//calculate the width and height of the map sub-image including zoom level
    	float width, height;	
     	width = 640 + (int)zoom;   		    	
    	height = 480 + (int)zoom;
   		
    	//allow the camera to move out of map bounds creating a border around the map
    	int sX, sY; //map sub-image coordinates (held at min/max values when camera is out of bounds)
    	
    	cam.x = car.x - (width/2.4f);
    	cam.y = car.y - (height/2.9f);
    	
    	if(cam.x > 0)
    	{
    		if(cam.x + width < map.getWidth()) //camera x is within width of map
    		{
	    		sX = (int)cam.x;
    		}
    		else //camera x is greater than width of map
    		{
	    		sX =  map.getWidth() - 640 - (int)zoom; //sub-image x held at maximum value			
    			
    		}
    	}
    	else //camera x is less than 0
    	{
    		sX = 0; //stop getSubImage() exception y lies out of raster
    	}
    	
    	if(cam.y > 0)	
    	{
    		if(cam.y + height < map.getHeight()) //camera y is within height of map
    		{
	    		sY =  (int)cam.y; 
    		}
    		else //camera y is greater than height of map
    		{
	    		sY =  map.getHeight() - 480 - (int)zoom; //sub-image y held at maximum value
    		}
    	}
    	else //camera y is less than 0
    	{
    		sY = 0; 
    	}
    	
	
    	//redraw map in view-port to update front tire steering area
    	mapGraphics.drawImage(mapImage.getSubimage(sX, sY, (int)width, (int)height), sX, sY, this); 
    	
    	//draw car
    	car.draw(mapGraphics);
    	bufferGraphics.drawImage(map.getSubimage(sX, sY, (int)width, (int)height), 0, 0, 640, 480, this);    	
    	
    	if(Settings.DrawCollisionBounds)
    		bufferGraphics.drawImage(collisionMap.getSubimage(sX, sY, (int)width, (int)height), 0, 0, 640, 480, this);    	
    	
    	Graphics2D g2d = (Graphics2D) bufferGraphics;	
    	
		AffineTransform at = new AffineTransform();
        at.translate(440, 280);
        at.rotate(Math.toRadians(car.steeringWheelAngle), 97, 97);           
        g2d.drawImage(steering, at, null);     
    	
    	//debug
    	bufferGraphics.setColor(Color.BLACK);
    	bufferGraphics.drawString("Car x=" + (int)car.x + " y=" + (int)car.y, 5, 15);  
    	float speedmph = car.speed / 0.18f;
    	bufferGraphics.drawString("Speed =" + (int)(speedmph * 30) , 5, 30);
    	bufferGraphics.drawString("Steering =" + (int)car.frontWheelAngle, 5, 45);
    	bufferGraphics.drawString("Camera x=" + (int)cam.x + " y=" + (int)cam.y + " z=" + ((int)zoom), 5, 60);    	
    	bufferGraphics.drawString("Mouse x=" + (int)mouse.x + " y=" + (int)mouse.y, 5, 75);
    	
        g.drawImage(offscreen, 0, 0, getWidth() + 2, getHeight() + 1, this); 
    }


    /**
     * Invoked when a key has been pressed<BR>
     * Sets the player controls, pause the game or pass event the level editor panel
     * @param e event which indicates that a keystroke occurred in a component
    */
    public void keyPressed(KeyEvent e) {
    	
		if (e.getKeyCode() == KeyEvent.VK_UP)
			car.up = true;

		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			car.down = true;

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			car.left = true;

		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			car.right = true;

		if (e.getKeyCode() == 45) { //-
			if (zoom + Settings.CameraShift < 1500)
				zoom += Settings.CameraShift;
			else
				zoom = 1500;
		}
		
		if (e.getKeyCode() == 61) { //+
			if (zoom - Settings.CameraShift > 0)
				zoom -= Settings.CameraShift;
			else
				zoom = 0;
		}
    }

    /**
     * Invoked when a key has been released
     * @param e event which indicates that a keystroke occurred in a component
    */
    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) 
			car.up = false;
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) 
			car.down = false;  	
    	
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) 
    		car.left = false;	
    	
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
    		car.right = false;	
    }

    /**
     * Invoked when a key has been typed
     * @param e event which indicates that a keystroke occurred in a component
     */
    public void keyTyped(KeyEvent e) {
    }


    /**
     * Invoked when the mouse enters a component
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component
     * @param e An event which indicates that a mouse action occurred in a component
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseMoved(MouseEvent e) {   	
    	
    	//calculate percent mouse position in window (640x480)   
    	float pX, pY;
    	pX = (e.getX() / 640.0f);
    	pY = (e.getY() / 480.0f);
    	
    	//dimensions of view-port (map sub-image width/height)
    	float vW, vH;
    	vW = 640 + (int)zoom;
    	vH = 480 + (int)zoom;
    	
    	//position of mouse in the dimensions of the view-port
    	float mX = (pX * vW);
    	float mY = (pY * vH);
    	
    	//mouse coordinates in the dimensions of the view-port plus camera coordinates   	
    	mouse.x = cam.x + mX;
    	mouse.y = cam.y + mY;
    }

    /**
     * Invoked when the mouse exits a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mouseReleased(MouseEvent e) {
 
    }

    /**
     * Invoked when a mouse button has been pressed on a component<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that a mouse action occurred in a component
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Invoked when the mouse wheel is rotated<BR>
     * Pass event to the level editor panel
     * @param e event which indicates that the mouse wheel was rotated in a component
     */
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
