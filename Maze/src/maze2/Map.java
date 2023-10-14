/*
 * Map.java - Create, load and edit the game map. (16x16 block size)        
 */
/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package maze2;

import java.util.Random;
import java.awt.Point;

public class Map {

    private int[][] m;     //array containing objects
    private Point d;       //dimensions    
    private Random r;

    /**
     * Constructor
     *  @param w The width of the map
     *  @param h The height of the map
     *  @param n The number of walls in the map
     */
    public Map(int w, int h, int n) {
        r = new Random();
        createMap(w, h, n);
    }

    /**
     * Get the size of the map
     * @return The width and height of the map
     */
    public Point getSize() {
        return d;
    }

    /**
     * Add object to the map 0-empty, 1-wall, 2-end
     * @param p The X,Y value of the object on the map
     * @param obj The value of the object
     */
    public void setPiece(Point p, int obj) {
        m[p.x][p.y] = obj;
    }

    /**
     * Get object from the map
     * @param p The X,Y value of the object on the map
     * @return Return the type of object 0-empty, 1-wall, 2-end 
     */
    public int getPiece(Point p) {
        try {
            return m[p.x][p.y];
        } catch (Exception e) //throw error and return 1 if index out of range
        {
            return -1;
        }
    }
    
    /**
     * Get the entire map
     * @return Array containing objects on the map
     */
    public int[][] getMap() {
        return m;
    }

    /**
     * Get a free space on the map
     * @return The X,Y value of the free map space 
     */
    public Point getFreeSpace() {
        Point free = new Point(0, 0);
        int a, b;
        a = r.nextInt(d.x) + 1;
        b = r.nextInt(d.y) - 1;

        for (int x = 0; x < a; x++) {
            for (int y = 0; y < b; y++) {
                if (this.m[x][y] == 0) {
                    free.x = x;
                    free.y = y;
                }
            }
        }
        return free;
    }

    /**
     * Clear map with empty values
     */
    public void clear() {
        for (int x = 0; x < d.x; x++) {
            for (int y = 0; y < d.y; y++) {
                m[x][y] = 0;
            }
        }
    }

    /**
     * Create a new map map
     * @param w The width of the map
     * @param h The height of the map
     * @param n The number of walls in the map
     */
    public void createMap(int w, int h, int n) {
        d = new Point(w, h);
        m = new int[d.x][d.y];
        clear();

        //create random maze 
        int a, b;
        for (int x = 0; x < n; x++) {
            a = r.nextInt(d.x);
            b = r.nextInt(d.y);
            setPiece(new Point(a,b),1);
        }

        //add end marker
        a = r.nextInt(d.x);
        b = r.nextInt(d.y);
        for (int x = 0; x < d.x; x++) {
            for (int y = 0; y < d.y; y++) {
                if (x == a && y == b) {
                    setPiece(new Point(a,b),2);
                }
            }
        }
    }
}
