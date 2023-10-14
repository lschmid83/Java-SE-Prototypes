package com.java.connect4;

import java.awt.*;
import java.awt.image.*;
import java.util.Random;

//This is a game of Connect 4
public class C4Game
{
	Image bg;
	Image red;
	Image yellow;

	int player = 1;
	int noOfPlayers = 1;
	int freeRow = 0;
	int dropHeight = 1000;

	boolean winColor = true;
	int column = 0;
	int speed = 3;
	int row = 0;
	int x = 0;
	int y = 6;

	int COL_SIZE = 7;
	int ROW_SIZE = 6;

	int cpu_col;		//the next best go
	int cpu_row;

	boolean title = false;
	boolean drop = false;


	Random generator = new Random();

	char board[][] = new char [8][7];   //the connect 4 board

	//Intialize the game variables and load images
	public C4Game()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		try
		{
			bg = toolkit.getImage(getClass().getResource("board.png"));
			red = toolkit.getImage(getClass().getResource("red.png"));
			yellow = toolkit.getImage(getClass().getResource("yellow.png"));
		}
		catch (Exception e)
		{
			System.out.println("couldn't load file");
		}

		reset();
		setRndBoard();
		title = true;
	}

	//Reset the game
	public void reset()
	{
		player = 1; //todo random player
		drop = false;

		for (int c = 0; c<7; c++) //clear board
		{
			for (int i = 0; i<6; i++)
			{
				board[c][i] = ' ';
			}
		}

	}

	public void setNoOfPlayers(int players)
	{
		noOfPlayers = players;
	}

	//Returns the current player or 0 = draw, -1 = player 1 wins, -2 = player 2 wins
	//checks for winning lines and changes marker color
	public int getCurrentPlayer()
	{
		//use temp board to store winning lines
		char tmp[][] = new char [8][7];   //the connect 4 board

		int winner;     //the winner
		boolean win = false; //has the game been won

		//Check to see if the last player to got a connect of 4 markers
		if (player == 1)
	    	winner = -2;
	    else
			winner = -1;

		//check for win horizontally
		for (int row=0; row<ROW_SIZE; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row] &&
				    board[col][row] == board[col+2][row] &&
				    board[col][row] == board[col+3][row] &&
				    board[col][row] != ' ')
				{
				   	win = true;
					//store winning markers in temp board so the board
					//can be checked again for any other winning lines
					tmp[col][row] = 'w';
					tmp[col+1][row] = 'w';
					tmp[col+2][row] = 'w';
				   	tmp[col+3][row] = 'w';
				}
			}
		}

		//check for win vertically
		for (int row=0; row<ROW_SIZE-3; row++)
		{
		    for (int col=0; col<COL_SIZE; col++)
		    {
				if (board[col][row] == board[col][row+1] &&
				    board[col][row] == board[col][row+2] &&
				    board[col][row] == board[col][row+3] &&
				    board[col][row] != ' ')
				{
					win = true;
					tmp[col][row] = 'w';
					tmp[col][row+1] = 'w';
					tmp[col][row+2] = 'w';
				   	tmp[col][row+3] = 'w';
				}
		    }
		}

		//check for win diagonally (upper left to lower right)
		for (int row=0; row<ROW_SIZE-3; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row+1] &&
				    board[col][row] == board[col+2][row+2] &&
				    board[col][row] == board[col+3][row+3] &&
				    board[col][row] != ' ')
				{
				    win = true;
					tmp[col][row] = 'w';
					tmp[col+1][row+1] = 'w';
					tmp[col+2][row+2] = 'w';
				   	tmp[col+3][row+3] = 'w';
				}
		    }
		}

		//check for win diagonally (lower left to upper right)
		for (int row=3; row<ROW_SIZE; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row-1] &&
				    board[col][row] == board[col+2][row-2] &&
				    board[col][row] == board[col+3][row-3] &&
				    board[col][row] != ' ')
				{
				    win = true;
				    tmp[col][row] = 'w';
					tmp[col+1][row-1] = 'w';
					tmp[col+2][row-2] = 'w';
				   	tmp[col+3][row-3] = 'w';
				}
		    }
		}

		//check if all spaces have been used
		int s = 0;
		for (int c = 0; c < COL_SIZE; c++)
		{
			for (int i = 0; i < ROW_SIZE; i++)
			{
				if(board[c][i] == 'o' ||  board[c][i] == 'x')
					s++;
			}
		}

		if(s == (COL_SIZE) * (ROW_SIZE) )
			return 0; //all spaces used game is a draw

		if(win == true) //return winning player
		{
			//check for winning markers
			for (int c = 0; c < COL_SIZE; c++)
			{
				for (int i = 0; i < ROW_SIZE; i++)
				{
					if(tmp[c][i] == 'w')
						board[c][i] = 'w'; //update game board with winning markers
				}
			}

			return winner;

		}
		else //no one has won game continues
			return player;
	}

	//Right mouse click on applet window
	public void clicked(int mouseX, int mouseY)
	{
		if(title == true) //title screen
		{
			if(mouseX >= 65 && mouseX <= 110 && mouseY >= 295 && mouseY <= 310) //1 player
			{
				setNoOfPlayers(1);
				title = false;
				reset();
			}
			else if(mouseX >= 205 && mouseX <= 255 && mouseY >= 295 && mouseY <= 310) //2 player
			{
				setNoOfPlayers(2);
				title = false;
				reset();
			}
		}
		else //in game
		{
			int col = getColumn(mouseX);

			if(drop == false)
				dropMarker(col);

			if(getCurrentPlayer() <= 0) //gameover return to title screen
			{
				reset();
				setRndBoard();
				drop = false;
				title = true;
				//return true;
			}
		}
	}

	//Setup a random game board
	public void setRndBoard()
	{
		//check for winning markers
		for (int c = 0; c < COL_SIZE; c++)
		{

			int number = generator.nextInt(6) - 1; //place a random number of pieces in column

			for (int i = 6; i > number; i--)
			{
				int color = generator.nextInt(2);

				if(color == 1)
					board[c][i] = 'x'; //place random marker
				else
					board[c][i] = 'o'; //place random marker
			}
		}

	}

	//Checks that the col is not full already and drops piece into col
	public boolean dropMarker(int col)
	{
		if(col < 0 || col > 6)
			return false;

		for(int i = 5; i >= 0; i--) //loop through rows
		{
			if (board[col][i] == 'o' || board[col][i] == 'x') //row is in use already
			{
				freeRow = i - 1;
				row = freeRow;
			}
			else
			{
				freeRow = i;
				break;
			}

		}

		if (freeRow >= 0 && drop == false) //place marker in square
		{
			drop = true;
			dropHeight = (50 + (freeRow * 40));

			column = col;
			row = freeRow;

			x = 24 + (col * 40); //set drop x position of piece

			return true;
		}
		else //col has been filled with pieces
			return false;

	}

	//Gets the current marker placed in a square
	public char get(int col, int row)
	{
		return board[col][row];
	}

	//Returns the column for the mouse x position
	public int getColumn(int mouseX)
	{
		int col = (mouseX / 43);

		return col;

	}

	//Draw the board with game pieces
	public Image drawBoard(int mouseX, int mouseY)
	{
 		BufferedImage scr = new BufferedImage(320, 320, BufferedImage.TYPE_INT_ARGB);  //screen buffer to draw on
   		Graphics2D g = scr.createGraphics();  //graphics object used to draw images into screen buffer

        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		winColor = !winColor; //alternate color

		//draw pieces on the game board
		for (int c = 0; c<7; c++)
		{
			for (int i = 0; i<6; i++)
			{
				if(board[c][i] == 'x') //red piece
					g.drawImage(red, (40 * c) + 24, (40 * i) + 50, null);

				else if(board[c][i] == 'o') //yellow piece
					g.drawImage(yellow, (40 * c) + 24, (40 * i) + 50, null);

				else if(board[c][i] == 'w') //winning piece
				{
					if(winColor)
						g.drawImage(yellow, (40 * c) + 24, (40 * i) + 50, null);
					else
						g.drawImage(red, (40 * c) + 24, (40 * i) + 50, null);
				}
			}
		}


		if(title == true) //title screen
		{
			g.setColor(Color.white);
			g.drawString("Connect 4", 135, 28); //title
			g.drawString("--------------", 135, 35);

			if(mouseX >= 65 && mouseX <= 110 && mouseY >= 295 && mouseY <= 310) //highlight  1 player
			{
				g.setColor(Color.red);
				g.drawString("1 Player", 65, 310);
				g.setColor(Color.white);
				g.drawString("2 Player", 205, 310);
			}
			else if(mouseX >= 205 && mouseX <= 255 && mouseY >= 295 && mouseY <= 310) //highlight  1 player
			{
				g.setColor(Color.white);
				g.drawString("1 Player", 65, 310);
				g.setColor(Color.red);
				g.drawString("2 Player", 205, 310);
			}
			else
			{
				g.setColor(Color.white);
				g.drawString("1 Player", 65, 310);
				g.drawString("2 Player", 205, 310);
			}

		}
		else //in-game
		{
			//update status
			if(getCurrentPlayer() == 1)
			{
				g.setColor(Color.red);
				g.drawString("Player 1", 12, 310);
			}
			else if(getCurrentPlayer() == 2)
			{
				if(noOfPlayers == 2)
				{
					g.setColor(Color.yellow);
					g.drawString("Player 2", 12, 310);
				}
				else
				{
					g.setColor(Color.yellow);
					g.drawString("Computer", 12, 310);
				}
			}
			else if(getCurrentPlayer() == 0)
			{
				g.setColor(Color.blue);
				g.drawString("The game is a draw", 12, 310);
			}
			else if(getCurrentPlayer() == -1)
			{
				g.setColor(Color.red);
				g.drawString("Player 1 Wins", 12, 310);
			}
			else if(getCurrentPlayer() == -2)
			{
				if(noOfPlayers == 2)
				{
					g.setColor(Color.yellow);
					g.drawString("Player 2 Wins", 12, 310);
				}
				else
				{
					g.setColor(Color.yellow);
					g.drawString("Computer Wins", 12, 310);
				}
			}

			if(getCurrentPlayer() <= 0)
			{
				g.setColor(Color.white);
				g.drawString("Game Over !", 125, 28); //title
			}

			if(drop == true) //repeat until piece reaches drop height
			{
				try
				{
					Thread.sleep(3);
				}
				catch(InterruptedException exception) {}

				g.drawImage(drawPiece(player), x, y, null);

				y+=speed;
				if(y >= dropHeight) //marker has reached position on board
				{
					char marker;
					drop = false;
					y = 6;

					if (player == 1)
					{
						marker = 'x';
						player = 2;
					}

					else
					{
						marker = 'o';
						player = 1;
					}

					board[column][row] = marker; //place marker in borad array

					//random computer move needs AI
					if(noOfPlayers == 1)
					{
						int lastMove = column; //store the last move might be needed later

						if(getCurrentPlayer() > 0)
						{
							if(player == 2)
							{
								int nextMove = nextBestMove();
								dropMarker(nextMove);
							}
						}
					}
				}

			}
			else //piece has reached drop height (free row) then continue game
			{

				if(getCurrentPlayer() > 0) //draw player piece if game has not finished
				{
					if(mouseX <40)
						g.drawImage(drawPiece(player), 40 - (32/2), y, null);
					else if (mouseX < 280)
						g.drawImage(drawPiece(player), mouseX - (32/2), y, null);
					else
						g.drawImage(drawPiece(player), 280 - (32/2), y, null);
				}
			}
		}

		g.drawImage(bg, 10, 44, null);

		return (scr);
	}

	//Draw a red or yellow piece on the board
	public Image drawPiece(int player)
	{
		if (player == 1)
			return(red);
		else
			return(yellow);
	}

	//Pause the game
	private void pause(int sec)
	{
		try
		{
			Thread.sleep(sec * 1000);
		}
		catch(InterruptedException exception) {}
	}


	//Places markers turn by turn until a winning line is found and returns the best move
	//to win or block opponent
	private int nextBestMove()
	{
		int noMoveCol = 0;
		int possMoveCol = 0;

		//check for any lines which can be made on this go
		for (int c = 0; c<7; c++)
		{
			if(placeMarker(c, 'o') == true)
			{
				if(checkWin() == true)
				{
					removeLastMarker();
					return c; //return win move
				}
				else
					removeLastMarker();
			}
		}


		//check for any lines which human player can get on their next go
		for (int c = 0; c<7; c++)
		{
			if(placeMarker(c, 'x') == true)
			{
				if(checkWin() == true)
				{
					removeLastMarker();
					return c; //return block move
				}
				else
					removeLastMarker();
			}
		}


		//check for any lines which would be made available to human after computer has gone
		for (int c = 0; c<7; c++)
		{
			if(placeMarker(c, 'o') == true)
			{
				//check for any lines after human goes again
				for (int c2 = 0; c2<7; c2++)
				{
					if(placeMarker(c2, 'x') == true)
					{
						if(checkWin() == true)
						{
							noMoveCol = c;
						}

						removeLastMarker();

					}
				}

				removeLastMove(c, 'o');
			}


		}

		//check for any lines which can be made in 2 go's time
		for (int c = 0; c<7; c++)
		{
			if(placeMarker(c, 'o') == true)
			{
				//check for any lines after cpu goes again
				for (int c2 = 0; c2<7; c2++)
				{
					if(placeMarker(c2, 'o') == true)
					{
							if(checkWin() == true)
							{
								possMoveCol = c;
							}

							removeLastMarker();
					}
				}

				removeLastMove(c, 'o');

			}
		}

		//check for any lines which can be made by human in 2 go's time
		for (int c = 0; c<7; c++)
		{
			if(placeMarker(c, 'x') == true)
			{
				//check for any lines after cpu goes again
				for (int c2 = 0; c2<7; c2++)
				{
					if(placeMarker(c2, 'x') == true)
					{
						if(checkWin() == true)
						{
							possMoveCol = c;
							if(c == c2 + 1)
								possMoveCol--;
						}

						removeLastMarker();
					}
				}

				removeLastMove(c, 'x');

			}
		}



		if(possMoveCol != noMoveCol)
		{
			return possMoveCol;
		}
		else
		{
			int col = generator.nextInt(7); //use random column
			while(dropMarker(col)==false || col == noMoveCol)
				col = generator.nextInt(7);
			return col;
		}


	}


	//Checks if there is winning line in the grid and returns winning player number
	//used recursively by AI to find winning moves
	private boolean checkWin()
	{
		boolean win = false;

		//check for win horizontally
		for (int row=0; row<ROW_SIZE; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row] &&
				    board[col][row] == board[col+2][row] &&
				    board[col][row] == board[col+3][row] &&
				    board[col][row] != ' ')
				{
				   	win = true;
				}
			}
		}

		//check for win vertically
		for (int row=0; row<ROW_SIZE-3; row++)
		{
		    for (int col=0; col<COL_SIZE; col++)
		    {
				if (board[col][row] == board[col][row+1] &&
				    board[col][row] == board[col][row+2] &&
				    board[col][row] == board[col][row+3] &&
				    board[col][row] != ' ')
				{
					win = true;

				}
		    }
		}

		//check for win diagonally (upper left to lower right)
		for (int row=0; row<ROW_SIZE-3; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row+1] &&
				    board[col][row] == board[col+2][row+2] &&
				    board[col][row] == board[col+3][row+3] &&
				    board[col][row] != ' ')
				{
				    win = true;

				}
		    }
		}

		//check for win diagonally (lower left to upper right)
		for (int row=3; row<ROW_SIZE; row++)
		{
		    for (int col=0; col<COL_SIZE-3; col++)
		    {
				if (board[col][row] == board[col+1][row-1] &&
				    board[col][row] == board[col+2][row-2] &&
				    board[col][row] == board[col+3][row-3] &&
				    board[col][row] != ' ')
				{
				    win = true;

				}
		    }
		}

		return win;


	}


	//Checks that the col is not full already and drops piece into col
	public boolean placeMarker(int col, char player)
	{
		if(col < 0 || col > 6)
			return false;

		for(int i = 5; i >= 0; i--) //loop through rows
		{
			if (board[col][i] == 'o' || board[col][i] == 'x') //row is in use already
			{
				freeRow = i - 1;
				row = freeRow;
			}
			else
			{
				freeRow = i;
				break;
			}

		}

		if (freeRow >= 0 && drop == false) //place marker in square
		{
			column = col;
			row = freeRow;

			board[column][row] = player; //place marker in borad array

			return true;
		}
		else //col has been filled with pieces
			return false;
	}

	public void removeMarker(int col, int row)
	{
		board[col][row] = ' '; //remove last marker placed on board
	}

	public void removeLastMarker()
	{
		board[column][row] = ' '; //remove last marker placed on board
	}

	//remove the last placed marker in a column
	public boolean removeLastMove(int col, char player)
	{
		for(int i = 0; i <= 5; i++) //loop through rows
		{
			if (board[col][i] == player) //row is in use already
			{
				board[col][i] = ' '; //remove last marker placed on board
				return true;
			}
		}

		return false;

	}

}
