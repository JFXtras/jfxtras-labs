package jfxtras.labs.map;

import java.awt.Point;

/**
 * Interface which defines methods for zoom.
 * 
 * @author Mario Schroeder
 */
public interface Zoomable {
    
    int getZoom();

    void setZoom(int zoom);

    void zoomIn();

    void zoomIn(Point point);

    void zoomOut();
    
    boolean isIgnoreRepaint();

    void zoomOut(Point mapPoint);
    
}
