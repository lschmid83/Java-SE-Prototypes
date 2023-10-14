package com.java.spidersweb;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

// SpiderWeb.java - Draws a rotating spider web using coordinates, rotation, lines and gradient colours.
// Lawrence Schmid 10/06/2007
public class SpiderWeb  extends Applet implements Runnable
{
	int scrW;		//screen width
	int scrH;		//screen height
	int noOfLines;  //number of lines on the screen
	int maxLines;	//the maximum number of lines on the screen
	int spacingW;   //horizontal line spacing
	int spacingH;	//vertical line spacing
	int angleBG;	//background angle
	int angleFG;	//foreground angle
	int brush;		//brush width
	int r,g,b;		//red,green,blue color codes

	boolean colorCycle;  //enable color cycle
	boolean running;     //pause main loop if false

    // The object we will use to write with instead of the standard screen graphics
    Graphics2D bufferGraphics;

    // The image that will contain everything that has been drawn on bufferGraphics.
    BufferedImage offscreen;

    // Main executing thread
	Thread game;

	public SpiderWeb()
	{
		//The mouse listener is 'installed' as an inner class.
		class MyMouseListener implements MouseListener
		{
			public void mouseClicked(MouseEvent e)
		    {
			}
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton()== 1) //left click
				{
					if(running == true)
						stop();
					else
						start();
				}
				else //right click
				{
					if(running == true)
						colorCycle = !colorCycle;
				}

			}

			public void mouseReleased(MouseEvent e)	{}

			public void mouseEntered(MouseEvent e)	{}

			public void mouseExited(MouseEvent e)	{}

		}


		MyMouseListener listener = new MyMouseListener();
      	addMouseListener(listener);

	}

	public void init()
	{
		setSize(640, 480);
		
		scrW = 640;
		scrH = 480;

		noOfLines = 10;
		maxLines = 10;

		colorCycle = false;

		brush = 2;

        offscreen = new BufferedImage(scrW, scrH, BufferedImage.TYPE_INT_ARGB);

		// by doing this everything that is drawn by bufferGraphics will be written on the offscreen image.
        bufferGraphics = offscreen.createGraphics();
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
        //Wipe off everything that has been drawn before
        bufferGraphics.clearRect(0,0,scrW,scrH);

		noOfLines++;  //increase the number of lines draw on screen

		if(noOfLines >= maxLines)
			noOfLines = 1;

		//calculate line spacing
		spacingW = scrW / noOfLines;
		spacingH = scrH / noOfLines;

        //set background color
        //bufferGraphics.setColor(Color.black);

		//draw the rectangle
        bufferGraphics.fillRect(0,0,scrW,scrH);

		//create the AffineTransform instance
		AffineTransform affineTransform = new AffineTransform();

		//create the AffineTransform instance
		AffineTransform affineTransformBg = new AffineTransform();

		//set the translation to the mid of the component
		//affineTransform.setToTranslation(0,0);

		//rotate with the anchor point as the mid of the image
		affineTransform.rotate(Math.toRadians(angleFG--), scrW/2, scrH/2);
		affineTransformBg.rotate(Math.toRadians(angleBG++), scrW/2, scrH/2);

		//draw the images using the AffineTransform
		bufferGraphics.drawImage(drawStarBackground(scrW,scrH), affineTransform, this);
		bufferGraphics.drawImage(drawStarBackground(scrW, scrH), affineTransformBg, this);
		bufferGraphics.drawImage(drawGradientLines(scrW,scrH), affineTransform, this);

		// draw the offscreen image to the screen like a normal image.
        g.drawImage(offscreen, 0, 0, this);

	}

	//Draws a curved lines pattern with more detail and gradient filled lines
	private Image drawGradientLines(int width, int height)
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  //buffer to draw on
   		Graphics2D g = img.createGraphics();  //graphics object used to draw images into buffer

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g.setStroke(new BasicStroke(brush));

		width--;  height--; //fix remove border

		for(int i = 0; i <= noOfLines; i++)
		{
			drawGradientLine(g, width - (i * spacingW), 0, 0, i * spacingH);
			drawGradientLine(g, i * spacingW, 0, width, i * spacingH);

			drawGradientLine(g, 0, i * spacingH, i * spacingW, height);
			drawGradientLine(g, width - (i * spacingW), height, width, i * spacingH);
		}

		return(img);
	}

	//Draws a curved lines pattern with more detail and gradient filled lines
	private Image drawStarBackground(int width, int height)
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  //buffer to draw on
   		Graphics2D g = img.createGraphics();  //graphics object used to draw images into buffer

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g.setStroke(new BasicStroke(brush));

		//draw stars
		g.drawImage(drawStar(100, 100), (scrW/2) - 50, (scrH/2) - 50, null);
		g.drawImage(drawStar(200, 200), (scrW/2) - 100, (scrH/2) - 100, null);
		g.drawImage(drawStar(scrW, scrH), 0, 0, null);

		//draw +
		drawGradientLine(g, 0, height/2, width, height /2);
		drawGradientLine(g, width / 2, 0, width /2, height);

		//draw *
		drawGradientLine(g, width, 0, 0, height);
		drawGradientLine(g, 0, 0, width, height);


		return(img);
	}

	//Draws a star
	private Image drawStar(int width, int height)
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  //buffer to draw on
   		Graphics2D g = img.createGraphics();  //graphics object used to draw images into buffer

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g.setStroke(new BasicStroke(brush));

		//Draw 6 point star
		drawGradientLine(g, width / 2, 0, 0, height - (height / 4));
		drawGradientLine(g, width / 2, 0, width, height - (height /4));
		drawGradientLine(g, 0, height / 4, width, height / 4);
		drawGradientLine(g, width, height / 4, width / 2, height);
		drawGradientLine(g, width / 2, height, 0, height / 4);
		drawGradientLine(g, 0, height - (height/4), width, height - (height / 4));

		return(img);
	}

	//Draws a gradient filled line from point x1,y1 to x2,y2
	private void drawGradientLine(Graphics2D g2, int x1, int y1, int x2, int y2)
	{
		GradientPaint gradient;

		//keep color constants in range 0-255
		if(r >= 255)
			r = 1;

		if (g>= 245)
			g = 1;

		if(b>=250)
			b = 1;

		Color startCol = new Color(r++, g+=10, b+=5); //construct new colour value for each line

		if(colorCycle)
			gradient =  new GradientPaint(x1, y1, startCol, x2, y2, Color.white, false);
		else
			gradient =  new GradientPaint(x1, y1, Color.black, x2, y2, Color.white, false);

		Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);

		g2.setColor(Color.black);
	    g2.setPaint(gradient);
  		g2.fill(line);
		g2.draw(line);
	}

}
