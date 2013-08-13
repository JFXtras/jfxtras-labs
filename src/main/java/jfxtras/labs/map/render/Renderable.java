package jfxtras.labs.map.render;

import jfxtras.labs.map.MapTileable;

/**
 * Interface for additional layers on the map.
 * 
 * @author Mario Schroeder
 */
public interface Renderable {

    /**
     * Paints the map marker on the map. 
     *
     * @param mapController
     */
    void render(MapTileable viewer);
    
}
