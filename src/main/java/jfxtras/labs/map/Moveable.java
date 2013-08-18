package jfxtras.labs.map;

import java.awt.Point;

/**
 * This interface defines methods to move the map.
 *
 * @author Mario Schroeder
 */
public interface Moveable extends Zoomable{

    /**
     * Moves the map to the given coordinates so that x and y is in the center.
     *
     * @param x
     * @param y
     */
    void moveMap(int x, int y);

    /**
     * This method returns the center of the map as a point.
     *
     * @return the center as point
     */
    Point getCenter();

    int getMapHeight();
    
    int getMapWidth();
    
    int getMapX();

    int getMapY();
    
    /**
     * This method should return <code>true</code> if the map can be moved otherwise false.
     * @return
     */
    boolean isMapMoveable();
    
    /**
     * Centers the map: latitude and longitude are zero.
     */
    void centerMap();
    
}
