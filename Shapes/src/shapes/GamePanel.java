/*
 * GamePanel.java - JPanel which is used to draw the graphics for the game
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Vector;
import java.awt.geom.*;
import java.util.Random;

public class GamePanel extends JPanel
        implements KeyListener, MouseListener, MouseMotionListener, 
        MouseWheelListener, Runnable {

    private Thread game;
    private Random generator = new Random();
    private static int score;
    private static int quakes;
    private static int time;
    private static int level;
    private static int state; //0=title 1=game 2=paused 3=win 4=lose
    private static Vector<Shapes> shape = null;
    private static Screens screen = new Screens(Settings.VIEW_PORT, 
                                                Settings.VIEW_PORT);
    public static Earthquake quake = new Earthquake();
    private int width;
    private int height;
    public static float mouseX;
    public static float mouseY;
    private BufferedImage img = null;
    private Image bg = null;
    private Graphics2D sg;
    private AudioClip quakeSFX;
    private AudioClip deleteSFX;
    public static int cX;
    public static int cY;
    private int timer;
    private boolean start = false;
    private boolean delete = false;
    public boolean running = true;
    private int shapeToDelete;
    private Font font = new Font("SansSerif", Font.BOLD, 12);
    private Rectangle2D click = new Rectangle2D.Double(0, 0, 0, 0);
    private int scroll;
    int hit = 0;

    /**
     * Construct GamePanel 
     */        
    public GamePanel(int new_width, int new_height) {

        quake = new Earthquake();
        state = 0;
        width = new_width;
        height = new_height;
        time = Settings.TIME_LIMIT;
        level = 0;
        start = true;
        setImg();
        shape = new Vector();
    
        //anti-aliasing
        sg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON);

        setBackground(1);

        quakeSFX = Applet.newAudioClip(this.getClass().
               getClassLoader().getResource("shapes2dgame/sfx/earthquake.wav"));
        deleteSFX = Applet.newAudioClip(this.getClass().
                getClassLoader().getResource("shapes2dgame/sfx/delete.wav"));

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);

        reset();

        if (!Settings.MOUSE_CURSOR) {
            Cursor nullCursor = null;
            Toolkit t = Toolkit.getDefaultToolkit();
            Dimension d = t.getBestCursorSize(1, 1);
            if ((d.width | d.height) != 0) {
                nullCursor = t.createCustomCursor(new BufferedImage(d.width,
                   d.height, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "");
            }
            setCursor(nullCursor);
        }

        setFont(font);
        setFocusable(true);
        requestFocusInWindow();

    }
    
    /**
     * Zoom ScrollUp = Zoom in ScrollDown = Zoom out
     */    
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = 0;
        if (state != 0) {
            notches = -e.getWheelRotation();
        }

        if (notches < 0) {
            Main.frame.sPanel.setZoomLevel(notches * Settings.PANEL_W / 20);
        } else {
            Main.frame.sPanel.setZoomLevel(notches * Settings.PANEL_W / 20);
        }
    }
    
    /**
     *  Button 1 = Squash Shape Button2 = Quake
     */      
    public void mousePressed(MouseEvent e) {

        requestFocusInWindow();
        mouseX = (float) e.getX() / (float) getWidth();
        mouseY = (float) e.getY() / (float) getHeight();
        if (e.getButton() == e.BUTTON1) {
            if (state == 0) //title
            {
                newLevel();
            } else if (state == 3) {
                newLevel();

            } else if (state == 4) {
                reset();
            }
        }
        try {
            click = new Rectangle.Float((mouseX * Settings.VIEW_PORT) - 10, 
                    (mouseY * Settings.VIEW_PORT) - 10,
                    30, 30); //change to make it easier to squash shapes

            for (int i = 0; i <= shape.size(); i++) {
                if (e.getButton() == e.BUTTON1) {
                    if (state == 1) //game
                    {
                        if (shape.get(i).intersects(click)) {
                            if (!shape.get(i).getSquash() && Settings.SOUND) {
                            }
                            score += 10;
                           shape.get(i).squash(shape.get(i).getSize() / 2 + 10);
                            shape.get(i).setSquash(true);
                            if (shape.get(i).getSize() < 60) {
                                score += 100;
                            }
                        }
                    }
                } else if (e.getButton() == e.BUTTON3 && state == 1) {
                    if (!quake.getVisible() && quakes > 0) {
                        quakes--;
                        if (Settings.SOUND) {
                            quakeSFX.play();
                        }
                        quake.setX((int) (mouseX * Settings.VIEW_PORT));
                        quake.setY((int) (mouseY * Settings.VIEW_PORT));
                        quake.setVisible(true);
                    }
                }
            }
        } catch (Exception exception) {}

    }
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}   
 
    /**
     * S = Settings  S = Scrolling -/+ = Speed 
     * R = Reset P = Pause M = Map PgUp = Zoom in PgDown = Zoom out
     */    
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_RIGHT) 
        {
            cX += width / 40;
        } else if (keyCode == e.VK_LEFT) {
            cX -= width / 40;
        } else if (keyCode == e.VK_UP) 
        {
            cY -= height / 40;
        } else if (keyCode == e.VK_DOWN) {
            cY += height / 40;
        }

        if (keyCode == e.VK_S) {
            if (Main.frame.sPanel.isVisible()) {

                Main.frame.sPanel.setVisible(false);
                Main.frame.setSize(400, 400);
            } else {

                Main.frame.sPanel.setVisible(true);
                Main.frame.setSize(512 + Main.frame.sPanel.getWidth(),
                        540);
            }
            Main.frame.centerScreen();
        }

        if (keyCode == 61 || keyCode == e.VK_ADD) {// e.VK_PLUS) {
            Settings.SPEED++;
            setSpeed();
        }
        if (keyCode == e.VK_MINUS || keyCode == e.VK_SUBTRACT) {
            Settings.SPEED--;
            setSpeed();
        }
        if (keyCode == e.VK_R) {
            reset();
        }

        if (state == 1) {
            if (keyCode == e.VK_P) {
                running = !running;
            }
            if (keyCode == e.VK_M) {
                Main.map.setVisible(!Main.map.isVisible());
            }
            if (keyCode == e.VK_PAGE_UP) {
                Main.frame.sPanel.setZoomLevel(1 * 15);
            }
            if (keyCode == e.VK_PAGE_DOWN) {
                Main.frame.sPanel.setZoomLevel(-1 * 15);
            }
        }
    } 
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    /**
     * Set the image used for the double buffer to the size of the view port
     * and set screen size
     */   
    public void setImg() {

        this.img = new BufferedImage(Settings.VIEW_PORT, Settings.VIEW_PORT,
                BufferedImage.TYPE_INT_RGB);
        sg = (Graphics2D) img.getGraphics();
        sg.setBackground(Color.white);
        screen.setSize(Settings.VIEW_PORT, Settings.VIEW_PORT);
        sg.setStroke(new BasicStroke(Settings.BRUSH));      
        repaint();
    }

    /**
     * Set the camera position in the GamePanel for panel sizes larger than 
     * the viewport
     * @param new_cX Camera x position
     * @param new_cY Camera y position   
     */   
    public static void setCamera(int new_cX, int new_cY) {
        cX = new_cX;
        cY = new_cY;
    }
    
    /**
     * Set the background image from the images included with the game
     * @param bgNo The level number of the background
     */   
    public void setBackground(int bgNo) {
        try {
            ImageIcon imgicon = new ImageIcon(this.getClass().
                getClassLoader().getResource("shapes2dgame/bg/bg"+bgNo+".jpg"));
            bg = imgicon.getImage();
        } catch (Exception e) {
            bg = null;
        }
    }

    /**
     * Get the vector containing the shapes
     */      
    public Vector<Shapes> getShapes() {
        return shape;
    }
   
    /**
     * Clear and add random number of shapes to vector
     */     
    public static void createRandomShapes() {
        shape.clear();
        for (int i = 0; i < Settings.SHAPES; i++) {
            shape.add(new Shapes());
        }
    }

    /**
     * Set the speed of the game panel to the value in Settings.SPEED
     */
    public static void setSpeed() {
        if (Settings.SPEED > 0) {
            for (int i = 0; i < shape.size(); i++) {
                shape.get(i).setRandomVelocity();
            }
        }
    }

    /**
     * Create random number shapes, set time limit, increase game speed, 
     * reset time, random rotation, random brush, random fill.
     */       
    public void newLevel() {
        level++;
        Settings.SHAPES = level * 5 + 5;

        if (Settings.SHAPES > 100) {
            Settings.SHAPES = 100;
        }

        time = Settings.SHAPES + 20;

        /*
        if (level % 2 == 0) {
           Settings.SPEED++;
        }
         */
        
        if(generator.nextInt() % 3 == 0){     
            Settings.FILL = false;
        } else {
            Settings.FILL = true;
        }
        state = 1;
        screen.setScreen(1);
        Settings.OPACITY = generator.nextInt(100) + 100;        
        createRandomShapes();
        setBackground(generator.nextInt(6));
        Settings.BRUSH = (generator.nextInt(5) + 2);
        if (generator.nextInt() % 3 == 0) {
            Settings.ROTATE = false;
        } else {
            Settings.ROTATE = true;
        }
         if(generator.nextInt() % 3 == 0)
             Settings.FILL = true;
         else
             Settings.FILL = false;
        quakes = 3;
        cX = 0;
        cY = 0;
        sg.setStroke(new BasicStroke(generator.nextInt(10)+5));
    }

    /**
     * Reset game variables and show title screen 
     */     
    public void reset() {
        state = 0;
        level = 0;
        score = 0;
        quakes = 0;
        screen.setScreen(0);
        time = Settings.TIME_LIMIT;
        Settings.SHAPES = 25;
        Settings.ROTATE = false;
        setBackground(generator.nextInt(6));
        Settings.FILL = true;
        createRandomShapes();
        cX = 0;
        cY = 0;
        sg.setStroke(new BasicStroke(Settings.BRUSH));
    }

    /**
     * Start game thread
     */    
    public void start() {
        game = new Thread(this); //create thread
        game.start();            //start thread    
    }

    /**
     * Main game thread
     */  
    public void run() {
        while (true) {

            if (running) {
                repaint();
                Main.map.repaint();
            }
            try {

                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Reset time limit to value in settings 
     */      
    public static void resetTimer() {
        time = Settings.TIME_LIMIT;
    }

    /**
     * Draw the games graphics on the panel and collision detection
     */        
    protected void paintComponent(Graphics g) {
   	
        super.paintComponent(g);
        width = Settings.PANEL_W;
        height = Settings.PANEL_H;

        //keep camera in panel area
        if (cX < 0) {
            cX = 0;
        } else if (cX > width - Settings.VIEW_PORT + 1) {
            cX = width - Settings.VIEW_PORT;
        } else if (cY < 0) {
            cY = 0;
        } else if (cY > height - Settings.VIEW_PORT + 2) {
            cY = height - Settings.VIEW_PORT + 2;
        }

        if (delete) {
            shape.remove(shapeToDelete);
            delete = false;
        }

        //background
        if (!Settings.SCROLLING) {       
        	if(bg != null)
        		sg.drawImage(bg, -cX, -cY, width, height, this);
        	else
        		sg.clearRect(0, 0, width, height); 
        } else {        
            if (scroll < width && Settings.SCROLLING) {
                scroll+= Settings.SPEED / 3 + 1;
            } else {
                scroll = 0;
            }
            sg.drawImage(bg, -scroll, -scroll, width, height, this);
            sg.drawImage(bg, width - scroll, -scroll, width, height, this);
            sg.drawImage(bg, width - scroll,height-scroll, width, height, this);
            sg.drawImage(bg, -scroll, height - scroll, width, height, this);
            
            if(bg == null)
            	sg.clearRect(0, 0, width, height); 
        }

        //draw and detect colision between shapes in the vector 
        for (int i = 0; i < shape.size() - 1; i++) {
            shape.get(i).draw(sg, cX, cY);

            if (shape.get(i).getSize() <= 30) {
                shapeToDelete = i;
                delete = true;

                if (Settings.SOUND) {
                    deleteSFX.play();
                }

            }
            if (shape.get(i).intersects(quake.getArea()) && quake.getVisible()){
                score += 1;
                shape.get(i).squash(shape.get(i).getSize() / 2);
            shape.get(i).setSquash(true);
            }

            //colision detection 
            for (int c = 0; c < shape.size() - 1; c++) {
                if (i != c) {
                    if (shape.get(i).intersects(new Rectangle(
                            shape.get(c).getX(), shape.get(c).getY(), 
                            shape.get(c).getWidth(), shape.get(c).getHeight()))) 
                    {
                        if (shape.get(i).getType() == shape.get(c).getType()) {
                            if (!shape.get(i).getEnlarge()) {
                               shape.get(i).enlarge(shape.get(i).getSize() * 2);
                            }
                        } else {

                            if (!shape.get(i).getEnlarge()) {
                                shape.get(i).setCollision(true);
                                break;
                            }
                        }
                    } else
                    {
                        shape.get(i).setCollision(false);
                    }
                }
            }
        }

        quake.draw(sg);
        sg.setColor(new Color(0, 0, 0, Settings.OPACITY));

        if (Settings.SCORE) {
            for (int i = 0; i <= shape.size() - 1; i++) {
                if (shape.get(i).getSquash()) {
                    if (shape.get(i).getSize() > 60) {
                        sg.drawString("+10", shape.get(i).getX() +
                                (shape.get(i).getWidth() / 2) - cX,
                                shape.get(i).getY() - 5 - cY);
                    } else {
                        sg.drawString("+100", shape.get(i).getX() +
                                (shape.get(i).getWidth() / 2) - cX,
                                shape.get(i).getY() - 5 - cY);
                    }
                }
            }
        }

        //draw border       
        sg.drawRect(5 - cX, 5 - cY, width - 10, height - 10);

        //time        
        timer++;

        //game logic and screens
        if (state == 1) {
            if (shape.size() <= 1) {
                screen.setScreen(3);
                state = 3;
            }

            if (time < 1) {
                screen.setScreen(4);
                state = 4;

            }

            if (timer % 80 == 0) {
                if (time > 0) {
                    time--;
                } else //time up
                {
                    screen.setScreen(4);
                    state = 4;
                }
            }

        } else {
            screen.paint(sg, cX, cY);
        }

        if (!Settings.MOUSE_CURSOR) {
            sg.draw(new Rectangle.Float(mouseX * Settings.VIEW_PORT, 
                    mouseY * Settings.VIEW_PORT, 4, 4));
        }

        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(),
                    getHeight(), this);
        }

        //draw status information
        if (Settings.STATUS && state != 0) {
            g.drawString("Time: " + time, getWidth() / 2 - 35, 40);
            g.drawString("Score: " + score, getWidth() / 4 * 3 - 30, 40);
            g.drawString("Level: " + level, getWidth() / 4 * 1 - 35, 40);

        } else {
            if (timer % 30 == 0) {
                start = !start;
            }
            g.drawString("Lawrence Schmid", 20, 30);
            if (start) {
                g.drawString("Click To Start Game", (getWidth() / 2) - 50, 
                        (getHeight() / 4) * 3 + 20);
            }
        }
    }
}




