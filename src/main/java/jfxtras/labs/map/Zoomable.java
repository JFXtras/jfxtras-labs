package jfxtras.labs.map;

import java.awt.Point;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Interface which defines methods for zoom.
 * 
 * @author Mario Schroeder
 */
public interface Zoomable {
	
    int getMinZoom();
    
    int getMaxZoom();
    
    SimpleIntegerProperty zoomProperty();
    
    void setZoom(int zoom);

    void zoomIn();

    void zoomIn(Point point);

    void zoomOut();
    
    void zoomOut(Point mapPoint);
    
}
