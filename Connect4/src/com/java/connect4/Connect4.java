package com.java.connect4;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.Cursor.*;

// Connect4.java - Plays a game of connect 4.
// Lawrence Schmid 10/06/2007
public class Connect4 extends Applet implements Runnable
{
	int freeRow;
	int scrW;
	int scrH;
	int x = 0;
	int y = 0;
	int col = 0;
	Color bgCol;  		//background colour
	boolean running;    //pause main loop if false

    // The object we will use to write with instead of the standard screen graphics
    Graphics2D bufferGraphics;

    // The image that will contain everything that has been drawn on bufferGraphics.
    BufferedImage offscreen;

    // Main executing thread
	Thread game;

	C4Game myGame = new C4Game();

	public Connect4()
	{
	
		//The mouse listener is 'installed' as an inner class.
		class MyMouseListener implements MouseListener
		{
			public void mouseClicked(MouseEvent e) { }
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton()== 1) //left click
				{
					myGame.clicked(x,y);
				}
			}

			public void mouseReleased(MouseEvent e)	{}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		}

		MyMouseListener listener = new MyMouseListener();
      	addMouseListener(listener);

 	   	// The Mouse Motion Listener is installed as an inner class.
	 	class MyMouseMotionListener implements MouseMotionListener
		{
			public void mouseMoved(MouseEvent event)
		    {

		    	x = event.getX();
		   		y = event.getY();
		   		if(myGame.title == true)
		   		{

					if(x >= 65 && x <= 110 && y >= 295 && y <= 310) //highlight  1 player
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					else if(x >= 205 && x <= 255 && y >= 295 && y <= 310) //highlight  1 player
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					else
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				else
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		    }

		    public void mouseDragged(MouseEvent event)   {   }

		}

		MyMouseMotionListener motionListener = new MyMouseMotionListener();
      	addMouseMotionListener(motionListener);

	}

	public void init()
	{
		setSize(320,320);
		
		scrW = 320;
		scrH = 320;

		x = scrW / 20;

		bgCol = Color.black;
	}

	// Java calls start() after init(), this is where the main thread should go.
	public void start()
	{
		game = new Thread( this ); // Create thread from "this" object.
		running = true;
		game.start();              // Thread in turn will execute run().
	}

 	// Java calls stop() when you leave the page.
 	 public void stop()
  	{
    	running = false;
  	}

   public void run()
   {
	   while(running) //the main game loop
	   {
			repaint();
			try
			{
			  Thread.sleep(1000 / 60); // 1000ms / 60 fps
			}
			catch( Exception e ){}
		}
	}

	// Overide paint event faster redraw
    public void update ( Graphics g )
    {
        paint(g);
    }

	public void paint(Graphics g)
	{
		Graphics2D screen = (Graphics2D)g;

        offscreen = new BufferedImage(scrW, scrH, BufferedImage.TYPE_INT_ARGB);

		// by doing this everything that is drawn by bufferGraphics will be written on the offscreen image
        bufferGraphics = offscreen.createGraphics();

		//bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Wipe off everything that has been drawn before
        bufferGraphics.clearRect(0,0,scrW,scrH);

        //set background color
        bufferGraphics.setColor(Color.black);

		//draw the rectangle
        bufferGraphics.fillRect(0,0,scrW,scrH);

		//game board
		bufferGraphics.drawImage(myGame.drawBoard(x,y), 0, 0, this);

		// draw the offscreen image to the screen like a normal image.
        screen.drawImage(offscreen, 0, 0, scrW, scrH, this);
	}

}
