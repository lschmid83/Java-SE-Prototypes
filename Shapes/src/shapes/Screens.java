/*
 * Screens.java - Draws title, win, lose overlay on the GamePanel 
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

import java.awt.*;
import java.util.Random;

public class Screens {

    private int screen;
    private int size;
    private int total;
    private int w;
    private int h;
    private int x;
    private int y;
    private int tmr;
    private boolean drawing;
    private Point pos[] = new Point[60];
    private Point moveTo[] = new Point[60]; 
    private Random generator = new Random();

    
    /**
     * Construct Screens 
     */       
    public Screens(int new_width, int new_height) {
        w = new_width;
        h = new_height;
        screen = 0;
        total = 0;
    }

    /**
     *  Set the screen overlay
     *  @param new_screen Set the screen 0 = title 1 = win 2 = lose
     */   
    public void setScreen(int new_screen) {
        screen = new_screen;
        tmr = 0;

 	for(int i = 0; i < 56; i++)
            pos[i] = new Point(generator.nextInt(Settings.PANEL_W),
                    generator.nextInt(Settings.PANEL_H));       
        
        w = Settings.PANEL_W;
        h = Settings.PANEL_H;
        if (new_screen == 0) {
            createTitleScreen();
        } else if (new_screen == 3) {
            createWinScreen();
        } else if (new_screen == 4) {
            createLoseScreen();
        }
    }
    
 
    /**
     *  Redraw the screen overlay with the width and height of GamePanel
     *  @param new_width The width of the GamePanel
     *  @param new_height The height of the GamePanel  
     */       
    public void setSize(int new_width, int new_height)
    {
         setScreen(screen);      
    }

    /**
     *  Create the WIN screen
    */    
    public void createWinScreen() {
        size = Settings.PANEL_W / 30;
        x = (w / 2) - size * 6;
        y = (h / 2) - size * 2;

        //W
        moveTo[0] = new Point(x, y);
        moveTo[1] = new Point(x, y + size);
        moveTo[2] = new Point(x, y + size * 2);
        moveTo[3] = new Point(x, y + size * 3);
        moveTo[4] = new Point(x + size, y + size * 4);
        moveTo[5] = new Point(x + size * 2, y + size * 3);
        moveTo[6] = new Point(x + size * 3, y + size * 4);
        moveTo[7] = new Point(x + size * 4, y + size * 3);
        moveTo[8] = new Point(x + size * 4, y + size * 2);
        moveTo[9] = new Point(x + size * 4, y + size);
        moveTo[10] = new Point(x + size * 4, y);

        x = x + size * 6;

        //I
        moveTo[11] = new Point(x, y);
        moveTo[12] = new Point(x, y + size * 2);
        moveTo[13] = new Point(x, y + size * 3);
        moveTo[14] = new Point(x, y + size * 4);
        x = x + size * 2;

        //N
        moveTo[15] = new Point(x, y);
        moveTo[16] = new Point(x, y + size);
        moveTo[17] = new Point(x, y + size * 2);
        moveTo[18] = new Point(x, y + size * 3);
        moveTo[19] = new Point(x, y + size * 4);

        moveTo[20] = new Point(x + size, y + size);
        moveTo[21] = new Point(x + size * 2, y + size * 2);
        moveTo[22] = new Point(x + size * 3, y + size * 3);
        moveTo[23] = new Point(x + size * 4, y + size * 4);
        moveTo[24] = new Point(x + size * 4, y + size * 3);
        moveTo[25] = new Point(x + size * 4, y + size * 2);

        moveTo[26] = new Point(x + size * 4, y + size);
        moveTo[27] = new Point(x + size * 4, y);

        total = 27;

    }

    /**
     *  Create the LOSE screen
    */         
    public void createLoseScreen() {
        size = Settings.PANEL_W / 30;

        x = (w / 2) - size * 8;
        y = (h / 2) - size * 2;

        //L
        moveTo[0] = new Point(x, y);
        moveTo[1] = new Point(x, y + size);
        moveTo[2] = new Point(x, y + size * 2);
        moveTo[3] = new Point(x, y + size * 3);
        moveTo[4] = new Point(x, y + size * 4);
        moveTo[5] = new Point(x + size, y + size * 4);
        moveTo[6] = new Point(x + size * 2, y + size * 4);

        x = x + size * 4;

        //O
        moveTo[7] = new Point(x + size, y);
        moveTo[8] = new Point(x + size * 2, y);
        moveTo[9] = new Point(x, y + size);
        moveTo[10] = new Point(x, y + size * 2);
        moveTo[11] = new Point(x, y + size * 3);

        moveTo[12] = new Point(x + size, y + size * 4);
        moveTo[13] = new Point(x + size * 2, y + size * 4);


        moveTo[14] = new Point(x + size * 3, y + size * 3);
        moveTo[15] = new Point(x + size * 3, y + size * 2);
        moveTo[16] = new Point(x + size * 3, y + size);

        x = x + size * 5;

        //S
        moveTo[17] = new Point(x + size, y);
        moveTo[18] = new Point(x + size * 2, y);
        moveTo[19] = new Point(x, y + size);
        moveTo[20] = new Point(x + size, y + size * 2);
        moveTo[21] = new Point(x + size * 2, y + size * 3);
        moveTo[22] = new Point(x + size, y + size * 4);
        moveTo[23] = new Point(x, y + size * 4);

        x = x + size * 4;

        //E
        moveTo[24] = new Point(x, y);
        moveTo[25] = new Point(x + size, y);
        moveTo[26] = new Point(x + size * 2, y);
        moveTo[27] = new Point(x, y + size);
        moveTo[28] = new Point(x, y + size * 2);
        moveTo[29] = new Point(x + size, y + size * 2);
        moveTo[30] = new Point(x, y + size * 3);
        moveTo[31] = new Point(x, y + size * 4);
        moveTo[32] = new Point(x + size, y + size * 4);
        moveTo[33] = new Point(x + size * 2, y + size * 4);
        total = 33;
    }

    /**
     *  Create the title SHAPES screen 
    */       
    public void createTitleScreen() {
        size = Settings.PANEL_W / 28;

        x = ((w / 2) - size * 12)  + 5;
        y = (h / 2) - size * 3;

        //S
        moveTo[0] = new Point(x + size, y);
        moveTo[1] = new Point(x + size * 2, y);
        moveTo[2] = new Point(x, y + size);
        moveTo[3] = new Point(x + size, y + size * 2);
        moveTo[4] = new Point(x + size * 2, y + size * 3);
        moveTo[5] = new Point(x + size, y + size * 4);
        moveTo[6] = new Point(x, y + size * 4);

        x = x + size * 4;

        //H
        moveTo[7] = new Point(x, y);
        moveTo[8] = new Point(x, y + size);
        moveTo[9] = new Point(x, y + size * 2);
        moveTo[10] = new Point(x, y + size * 3);
        moveTo[11] = new Point(x, y + size * 4);
        moveTo[12] = new Point(x + size, y + size * 2);
        moveTo[13] = new Point(x + size * 2, y);
        moveTo[14] = new Point(x + size * 2, y + size);
        moveTo[15] = new Point(x + size * 2, y + size * 2);
        moveTo[16] = new Point(x + size * 2, y + size * 3);
        moveTo[17] = new Point(x + size * 2, y + size * 4);

        x = x + size * 4;

        //A
        moveTo[18] = new Point(x, y + size);
        moveTo[19] = new Point(x, y + size * 2);
        moveTo[20] = new Point(x, y + size * 3);
        moveTo[21] = new Point(x, y + size * 4);
        moveTo[22] = new Point(x + size, y);
        moveTo[23] = new Point(x + size, y + size * 2);
        moveTo[24] = new Point(x + size * 2, y + size);
        moveTo[25] = new Point(x + size * 2, y + size * 2);
        moveTo[26] = new Point(x + size * 2, y + size * 3);
        moveTo[27] = new Point(x + size * 2, y + size * 4);

        x = x + size * 4;

        //P
        moveTo[28] = new Point(x, y);
        moveTo[29] = new Point(x, y + size);
        moveTo[30] = new Point(x, y + size * 2);
        moveTo[31] = new Point(x, y + size * 3);
        moveTo[32] = new Point(x, y + size * 4);
        moveTo[33] = new Point(x + size, y);
        moveTo[34] = new Point(x + size * 2, y);
        moveTo[35] = new Point(x + size * 2, y + size);
        moveTo[36] = new Point(x + size * 2, y + size * 2);
        moveTo[37] = new Point(x + size * 1, y + size * 2);

        x = x + size * 4;

        //E
        moveTo[38] = new Point(x, y);
        moveTo[39] = new Point(x + size, y);
        moveTo[40] = new Point(x + size * 2, y);
        moveTo[41] = new Point(x, y + size);
        moveTo[42] = new Point(x, y + size * 2);
        moveTo[43] = new Point(x + size, y + size * 2);
        moveTo[44] = new Point(x, y + size * 3);
        moveTo[45] = new Point(x, y + size * 4);
        moveTo[46] = new Point(x + size, y + size * 4);
        moveTo[47] = new Point(x + size * 2, y + size * 4);

        x = x + size * 4;

        //S
        moveTo[48] = new Point(x + size, y);
        moveTo[49] = new Point(x + size * 2, y);
        moveTo[50] = new Point(x, y + size);
        moveTo[51] = new Point(x + size, y + size * 2);
        moveTo[52] = new Point(x + size * 2, y + size * 3);
        moveTo[53] = new Point(x + size, y + size * 4);
        moveTo[54] = new Point(x, y + size * 4);

        total = 54;
    }

    
    /**
     * Draw the screen overlay on the GamePanel
     */     
    public void paint(Graphics2D g2, int camX, int camY) { 
        tmr++;
         g2.setColor(new Color(255, 0, 0, Settings.OPACITY));
         
        if (screen != 1) {
           for (int i = 0; i <= total; i++) {
               if(moveTo[i] != null) 
               {   
                   int s = Settings.SPEED + 2;
                    
                   //move to position
                   if (pos[i].x + s < moveTo[i].x) {
                       pos[i].x += s;
                   } else if (pos[i].x- s > moveTo[i].x) {
                       pos[i].x -= s;
                   } else {
                       pos[i].x = moveTo[i].x;
                   }

                   if (pos[i].y + s < moveTo[i].y) {
                       pos[i].y += s;
                   } else if (pos[i].y -s > moveTo[i].y) {
                       pos[i].y -= s;
                   } else {
                       pos[i].y = moveTo[i].y;
                   }              
            
                   g2.drawRect(pos[i].x- Main.frame.gPanel.cX, 
                           pos[i].y- Main.frame.gPanel.cY, size, size);
                  g2.fillRect(pos[i].x- Main.frame.gPanel.cX, 
                          pos[i].y- Main.frame.gPanel.cY, size, size);
               }
            }
        }

        if (tmr < 20) {
            drawing = true;
        } else {
            drawing = false;
        }

        if (screen == 0) {
            g2.setColor(new Color(0, 0, 0, Settings.OPACITY));
            x = 30;
            y = 30;
           
        }        
    }
}

