/*
 * Main.java - Create a GameFrame containing the drawPanel and settingsPanel and 
 *             a MapFrame containg the mapPanel
 */

/**
 * Not for duplication or distribution without the permission of the author
 * @author  - Lawrence Schmid
 */

package shapes;

public class Main {

    public static GameFrame frame;
    public static MapFrame map;
    
    /**
     * Construct Map and Game frames
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        map = new MapFrame(); 
        frame = new GameFrame();
        
        Main.map.setVisible(true);
    }
}
