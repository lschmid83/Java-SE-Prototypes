/*
 * DrawPanel - JPanel contains main game thread and key handling events.
 */
/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */
package maze2;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class Canvas extends JPanel implements KeyListener, Runnable {

    private Thread th;         //thread
    private Map mp;            //game map
    private Player pl;         //player
    private Enemy en[];        //enemy
    private int ec;            //enemy count
    private Graphics2D g2;     //double buffer
    private BufferedImage img; //image  
    private int lvl;           //level
    private int tl;            //time limit
    private int t;             //current time
    private int tmr;           //timer

    /**
     * Constructor
     */
    public Canvas() {

        //create map, player and enemies
        reset();

        //create double buffer
        img = new BufferedImage(mp.getSize().x * 16, mp.getSize().y * 16,
                BufferedImage.TYPE_INT_RGB);

        g2 = (Graphics2D) img.getGraphics();
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //create thread
        th = new Thread(this);
        start();        
        
        addKeyListener(this);
        setFocusable(true);
        requestFocus();  
    }

    /**
     * Handle key events
     * @param e The keycode of the pressed key
     */
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();

        if (kc == KeyEvent.VK_R) {
            reset();
        }

        if (kc == KeyEvent.VK_LEFT) {
            if (mp.getPiece(new Point(pl.getPos().x - 1, pl.getPos().y)) == 0) {
                pl.moveLeft();
            } else if (mp.getPiece(new Point(pl.getPos().x - 1,
                    pl.getPos().y)) == 2) {
                win();
            }
        }

        if (kc == KeyEvent.VK_RIGHT) {
            if (mp.getPiece(new Point(pl.getPos().x + 1, pl.getPos().y)) == 0) {
                pl.moveRight();
            } else if (mp.getPiece(new Point(pl.getPos().x + 1,
                    pl.getPos().y)) == 2) {
                win();
            }
        }

        if (kc == KeyEvent.VK_UP) {
            if (mp.getPiece(new Point(pl.getPos().x, pl.getPos().y - 1)) == 0) {
                pl.moveUp();
            } else if (mp.getPiece(new Point(pl.getPos().x,
                    pl.getPos().y - 1)) == 2) {
                win();
            }
        }
        if (kc == KeyEvent.VK_DOWN) {
            if (mp.getPiece(new Point(pl.getPos().x, pl.getPos().y + 1)) == 0) {
                pl.moveDown();
            } else if (mp.getPiece(new Point(pl.getPos().x,
                    pl.getPos().y + 1)) == 2) {
                win();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Start game thread
     */
    public void start() {
        th.start();
    }

    /**
     * Run game thread and repaint surface
     */
    public void run() {
        while (true) {
            repaint();
            tmr++;
            Main.f.setTitle("Maze!    Time = " + t + "    Level = " + lvl);
            if (tmr % 70 == 0) 
            {
                if (t  > 0) {
                    t--;
                } else {
                    lost();
                    t = tl;
                }
            } else if (tmr % 10 == 0) //slow down enemy movement
            {
                //Enemy movement
                for (int i = 0; i < ec; i++) {
                    en[i].move(mp);
                }
            }

            //colision detection player and enemies (begin 2 sec into game)
            for (int i = 0; i < ec; i++) {
                if (pl.getPos().x == en[i].getPos().x &&
                    pl.getPos().y == en[i].getPos().y && t < tl-2) {
                    lost();
                }
            }


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Create n amount of enemies at random positions on the map
     * @param n The number of enemies
     */
    public void createEnemies(int n) {
        ec = n;
        en = new Enemy[n];
        for (int i = 0; i < n; i++) {
            en[i] = new Enemy();
            en[i].setPos(mp.getFreeSpace());
        }
    }
    
    public void createPlayer()
    {
        Point p = mp.getFreeSpace();
        
        for(int i = 0; i<ec; i++)     
            if(p.x == en[i].getPos().x && p.y == en[i].getPos().y)
                createPlayer();       

        pl = new Player();  
        pl.setPos(new Point(p.x,p.y));   
    }
    

    /**
     * Reset game, create new map and add 10 to timelimit
     */
    public void reset() {
        lvl = 1;
        timeLimit(5);
        mp = new Map(40,40,lvl * 15);   
        createEnemies(lvl * 4);
        createPlayer();
    }

    /**
     * Display message box to show player has won, advance level, 
     * increase timelimit, create new map and reset player position
     */
    public void win() {
        lvl++;
        JOptionPane.showMessageDialog(null, "                You Win!", "", 1);
        //timeLimit(lvl * 5);
        timeLimit(t + 5);
        mp = new Map(40, 40, lvl * 15); 
        createEnemies(lvl * 4);        
        createPlayer();
    }

    /**
     * Display message box to show player has lost and reset game
     */
    public void lost() {
        JOptionPane.showMessageDialog(null, "               You Lose!", "", 0);
        reset();
    }

    /**
     * Set new timelimit
     * @param n The new time limit
     */
    public void timeLimit(int n) {
        tl=n;
        t=n;
    }

    /**
     * Redraw scene, background, border, walls, end marker and player objects
     * @param g The unbuffered graphic surface to draw on
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g2.clearRect(0, 0, mp.getSize().x * 16, mp.getSize().y * 16);

        //walls
        for (int x = 0; x < mp.getSize().x; x++) {
            for (int y = 0; y < mp.getSize().y; y++) {
                if (mp.getMap()[x][y] == 1) {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(x * 16, y * 16, 16, 16);
                }
            }
        }

        //end marker
        for (int x = 0; x < mp.getSize().x; x++) {
            for (int y = 0; y < mp.getSize().y; y++) {
                if (mp.getMap()[x][y] == 2) {
                    g2.setColor(Color.GREEN);
                    g2.fillRect((x * 16) + 5, (y * 16) + 5, 6, 6);
                }
            }
        }

        //player
        if(t > tl-2)
        {
            if(tmr % 2 == 0)
                pl.draw(g2);
        }
        else
            pl.draw(g2);   
               
        //enemy
        for (int i = 0; i < ec; i++) {
            en[i].draw(g2);
        }

        //draw buffer on graphics surface
        if (img != null) {
            g.drawImage(img, 2, 2, getWidth() - 4, getHeight() - 4, this);
        }
    }
}

