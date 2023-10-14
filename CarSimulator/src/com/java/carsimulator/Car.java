package com.java.carsimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.AlphaComposite;
import java.awt.Composite;

import java.awt.Polygon;

public class Car {

	/** The car image */
	private BufferedImage car;
	/** The wheel image */
	private BufferedImage wheel;
    /** The image that will contain the car with a border for steering axis  */ 
    private BufferedImage offscreen;
    /** The object we will use to write with instead of the standard screen graphics */
    private Graphics2D bufferGraphics;   
    /** The x coordinate of the car */ 
	public float x;
	/** The y coordinate of the car */
	public float y;
	/** The angle of the front wheels */
	public float frontWheelAngle;
	/** The angle of the steering wheel graphic */
	public float steeringWheelAngle;
	/** The bounding rectangle for the car */
	private Polygon bounds;
	
	//http://www.freeactionscript.com/2010/06/as3-car-movement-acceleration-turning-braking/
	//--------------------------------------------------------------------------------------
	public float speed = 0;
	private float speedMax = 10;
	private float speedMaxReverse = -3;
	private float speedAcceleration = 0.01f;
	//private float speedDeceleration = 0.90f;
	private float groundFriction = 0.95f;
	public float steering = 0;
	private float steeringMax = 30;
	private float steeringAcceleration = 0.10f;
	private float steeringFriction = 0.98f;
	private float velocityX = 0;
	private float velocityY = 0;
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right =  false;
	public float rotation;
	//--------------------------------------------------------------------------------------

	public Car(int x, int y)
	{	
		this.x = x;
		this.y = y;
        car = new Image("res/car.png").getImage();
        wheel = new Image("res/wheel.png").getImage();
        frontWheelAngle = 0;
        steeringWheelAngle = 0;
    	//create double buffer graphics
        offscreen = new BufferedImage(90, 154, BufferedImage.TYPE_INT_ARGB);
        bufferGraphics = (Graphics2D) offscreen.getGraphics();
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) bufferGraphics;	

		float percent = steering / 0.74f;
		frontWheelAngle = percent * 30;	
		steeringWheelAngle =  percent * Settings.MaxSteeringAngle;
		
		//undo alpha composite
		//http://www.javaprogrammingforums.com/awt-java-swing/5675-how-do-i-undo-alphacomposite.html
		Composite c = g2d.getComposite(); //store current composite
		
		//filling a BufferedImage with transparent pixels
		//http://stackoverflow.com/questions/5672697/java-filling-a-bufferedimage-with-transparent-pixels
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,90,154); 
		g2d.fill(rect);
		
		//set the composite and do drawing
		g2d.setComposite(c);

		//draw car image
		g2d.drawImage(car, 16, 0, null); 

		//front left wheel
		drawWheel(g2d, 18, 15, frontWheelAngle);
		
        //front right wheel
		drawWheel(g2d, 67, 15, frontWheelAngle);
		
		//back left wheel
		drawWheel(g2d, 18, 107, 0);
		
		//back right wheel
		drawWheel(g2d, 67, 107, 0);
		
		Graphics2D g2 = (Graphics2D) g;	
		
		AffineTransform at = new AffineTransform();
        at.translate((int)x-16, (int)y);
        at.rotate(Math.toRadians(rotation), 45, 77);           
        g2.drawImage(offscreen, at, null);  
                
        //bounding rectangle
        g2.setColor(Color.RED);
        
        //bounding rectangle corner points
        int[] pX = new int[4];
        int[] pY = new int[4];
 
        //top left
        int tx = 30;    int ty = 77;      
        double rot = rotation * Math.PI/180;     
        double newX = (-tx)*Math.cos(rot) - (-ty)*Math.sin(rot) + this.x + 59/2;    
        double newY = (-tx)*Math.sin(rot) + (-ty)*Math.cos(rot) + this.y + 154/2; 
        pX[0] = (int)newX;
        pY[0] = (int)newY;
     
        //top right
        tx = 30;     ty = 77;      
        rot = rotation * Math.PI/180;     
        newX = (tx)*Math.cos(rot) - (-ty)*Math.sin(rot) + this.x + 59/2;    
        newY = (tx)*Math.sin(rot) + (-ty)*Math.cos(rot) + this.y + 154/2; 
        pX[1] = (int)newX;
        pY[1] = (int)newY;     
        
        //bottom-left
        tx = 30;     ty = 77;      
        rot = rotation * Math.PI/180;     
        newX = (-tx)*Math.cos(rot) - (ty)*Math.sin(rot) + this.x + 59/2;    
        newY = (-tx)*Math.sin(rot) + (ty)*Math.cos(rot) + this.y + 154/2; 
        pX[2] = (int)newX;
        pY[2] = (int)newY;

        //bottom-right
        //http://stackoverflow.com/questions/5935424/rectangle-coordinates-after-rotation
        //-------------------------------------------------------------------------------
        tx = 30;     ty = 77;      
        rot = rotation * Math.PI/180;     
        newX = (tx)*Math.cos(rot) - (ty)*Math.sin(rot) + this.x + 59/2;    
        newY = (tx)*Math.sin(rot) + (ty)*Math.cos(rot) + this.y + 154/2; 
        pX[3] = (int)newX;
        pY[3] = (int)newY;
        //-------------------------------------------------------------------------------
        
        //create polygon using corner points
        Polygon p = new Polygon(pX, pY, 4);
        
        //top
        g2.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[1], p.ypoints[1]);
        //left
        g2.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[2], p.ypoints[2]);
        //right
        g2.drawLine(p.xpoints[1], p.ypoints[1], p.xpoints[3], p.ypoints[3]);
        //bottom
        g2.drawLine(p.xpoints[2], p.ypoints[2], p.xpoints[3], p.ypoints[3]); 
     
        bounds = p;
        
        /*
        g2.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[0], p.ypoints[0]);
        g2.drawLine(p.xpoints[1], p.ypoints[1], p.xpoints[1], p.ypoints[1]);
        g2.drawLine(p.xpoints[2], p.ypoints[2], p.xpoints[2], p.ypoints[2]);
        g2.drawLine(p.xpoints[3], p.ypoints[3], p.xpoints[3], p.ypoints[3]);
        */
	}
		
	public Polygon getBounds()
	{
		return bounds;
	}
	
	
	public void update(float dt) 
	{
		//http://www.asawicki.info/Mirror/Car%20Physics%20for%20Games/Car%20Physics%20for%20Games.html
		
		if (up)
		{
			//check if below speedMax
			if (speed < speedMax) 			
			{ 					
				//speed up 					
				speed += speedAcceleration; 					
				//check if above speedMax 					
				if (speed > speedMax)
				{
					//reset to speedMax
					speed = speedMax;
				}
			}
		}
		if (down)
		{
			//check if below speedMaxReverse
			if (speed > speedMaxReverse)
			{
				//speed up (in reverse)
				speed -= speedAcceleration;
				//check if above speedMaxReverse
				if (speed < speedMaxReverse) 					
				{ 						
					//reset to speedMaxReverse 						
					speed = speedMaxReverse; 					
				} 				
			} 
		}

		if (left) 			
		{ 				
			//turn left 				
			steering -= steeringAcceleration; 				
			//check if above steeringMax 				
			if (steering > steeringMax)
			{
				//reset to steeringMax
				steering = steeringMax;
			}		
		}
		
		if (right)
		{
			//turn right
			steering += steeringAcceleration;
			//check if above steeringMax
			if (steering < -steeringMax) 				
			{ 					
				//reset to steeringMax 					
				steering = -steeringMax; 				
			} 
		
		} 	
		
		// friction	 			
		speed *= groundFriction; 			 			
		// prevent drift 	
		/*
		if(speed > 0 && speed < 0.05) 			
		{ 				
			speed = 0; 			
		} 
		*/			 			
		
		// calculate velocity based on speed 			
		velocityX = (float)Math.sin (rotation * Math.PI / 180) * speed; 			
		velocityY = (float)Math.cos (rotation * Math.PI / 180) * -speed; 	
		
		// update position	 			
		this.x += velocityX * dt; 			
		this.y += velocityY * dt; 			 			
		
		// prevent steering drift (right) 			
		if(steering > 0)
		{
			// check if steering value is really low, set to 0
			if(steering < 0.05)
			{
				steering = 0;
			}
		}
		// prevent steering drift (left)
		else if(steering < 0) 			
		{ 				
			// check if steering value is really low, set to 0 				
			if(steering > -0.05)
			{
				steering = 0;
			}
		}
		// apply steering friction
		steering = steering * steeringFriction;
		// make car go straight after driver stops turning
		steering -= (steering * 0.1);
		// rotate
		this.rotation += steering * speed * dt;
	}
	
	
	private void drawWheel(Graphics2D g, float x, float y, float angle)
	{
		//wheel
		AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), 3, 11);           
        g.drawImage(wheel, at, null);     	
        
        //axis line      
        g.setColor(Color.GREEN);
        int sX = (int)x + (wheel.getWidth() / 2);
        int sY = (int)y + (wheel.getHeight() / 2);
        drawLine(bufferGraphics, sX, sY, 20, angle);
        drawLine(bufferGraphics, sX, sY, -20, angle);
		
	}

	
	private void drawLine(Graphics g, float x, float y, float w, float angle)
	{
        //rotate line - http://answers.yahoo.com/question/index?qid=20071031123836AAipLKx
        //start point (centre of front left wheel)
        double eX, eY; //end point
        eX = x + w * Math.cos(Math.toRadians(angle)); 
        eY = y + w * Math.sin(Math.toRadians(angle));       
        g.drawLine((int)x, (int)y, (int)eX, (int)eY);		
		
	}

}
