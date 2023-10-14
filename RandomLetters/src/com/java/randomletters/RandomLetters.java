package com.java.randomletters;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color;

// RandomLetters.java - Draws letters of random size, position and color on the applet window.
// Lawrence Schmid 03/03/2007
public class RandomLetters extends Applet
{
	int scrW;			//screen width
	int scrH;			//screen height
	int rndX;  			//x pos
	int rndY;  			//y pos
	int rndW;  			//letter width
	int rndH;  			//letter height
	int rndLet; 		//number between 0-4 which correspond to the letters X,L,O,A,M
	int noOfLetters;	//number of lettters to draw
	Color rndCol; 		//letter color
	boolean color;		//draw in color
	int maxLetters;     //max no of letters to draw

	Random generator = new Random();

    // Main executing thread
	Thread game;

	public RandomLetters()
	{
		//The mouse listener is 'installed' as an inner class.
		class MyMouseListener implements MouseListener
		{
			public void mouseClicked(MouseEvent e)
		    {
			}
			public void mousePressed(MouseEvent e)
			{
				while(noOfLetters < 20)
					noOfLetters = generator.nextInt(maxLetters);

				if(e.getButton()== 1) //left click
				{
					repaint();
				}
				else //right click
				{
					color = !color;
					repaint();
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
		scrW = getWidth();
		scrH = getHeight();

		maxLetters = 70;

		while(noOfLetters < 20)
			noOfLetters = generator.nextInt(maxLetters);

		color = true;
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		scrW = getWidth();
		scrH = getHeight();

       	//Wipe off everything that has been drawn before
        g2.clearRect(0,0,scrW,scrH);

        //set background color
        g2.setColor(Color.black);

		//Draw the random letters using the numbers initialized in the contructor
		for(int c = 0; c < noOfLetters; c++)
		{
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

			randomize();

			if(color)
				g2.setColor(rndCol);
			else
				g2.setColor(Color.black);

			if (rndLet == 0)
				drawX(g2, rndX, rndY, rndW, rndH);
			else if (rndLet == 1)
				drawL(g2, rndX, rndY, rndW, rndH);
			else if (rndLet == 2)
				drawO(g2, rndX, rndY, rndW, rndH);
			else if (rndLet == 3)
				drawA(g2, rndX, rndY, rndW, rndH);
			else if (rndLet == 4)
				drawM(g2, rndX, rndY, rndW, rndH);
		}


	}

	private void drawX(Graphics2D g2, int x, int y, int width, int height)
	{
		int pen_width = width/20 + 1;;
		g2.setStroke(new BasicStroke(pen_width));
		g2.drawLine(x, y, x+width, y+height);
		g2.drawLine(x+width, y, x, y+height);
	}

	private void drawL(Graphics2D g2, int x, int y, int width, int height)
	{
		int pen_width = width/20 + 1;;
		g2.setStroke(new BasicStroke(pen_width));
		g2.drawLine(x, y, x, y+height);
		g2.drawLine(x, y+height, x+width, y+height);
	}

	private void drawO(Graphics2D g2, int x, int y, int width, int height)
	{
		int pen_width = width/20 + 1;;
		g2.setStroke(new BasicStroke(pen_width));
		Ellipse2D.Double circle = new Ellipse2D.Double(x, y, width, height);
		g2.draw(circle);
	}

	private void drawA(Graphics2D g2, int x, int y, int width, int height)
	{
		int pen_width = width/20 + 1;;
		g2.setStroke(new BasicStroke(pen_width));
		g2.drawLine((width / 2) + x, y, x, y+height);
		g2.drawLine((width / 2) + x, y, x+width, y+height);
		g2.drawLine(x + (width /4), (height /2) + y, (x + width) - (width / 4), (height /2) + y);
	}

	private void drawM(Graphics2D g2, int x, int y, int width, int height)
	{
		int pen_width = width/20 + 1;;
		g2.setStroke(new BasicStroke(pen_width));
		g2.drawLine(x, y, x, y+height);
		g2.drawLine(x, y, (width/2) + x, (height/2)+ y);
		g2.drawLine(x + width, y, (width/2)+x, (height/2)+y);
		g2.drawLine(x + width, y, x+width,  y+height);
	}

	private void randomize()
	{
		rndX = generator.nextInt(scrW - 150);  //-150 stops letters from being drawn off screen
		rndY = generator.nextInt(scrH - 150);

		rndW = generator.nextInt(150);
		rndH = generator.nextInt(150);

		if(rndY - rndH < 0)  //letter is off screen
			rndY+=generator.nextInt(rndH);

		rndCol = new Color(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255));
		rndLet = generator.nextInt(5);
	}
}
