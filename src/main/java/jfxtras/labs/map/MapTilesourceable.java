package jfxtras.labs.map;

import jfxtras.labs.map.tile.TileSource;

/**
 * This interface defines methods to deal with tiles of the map.
 * @author Mario Schroeder
 *
 */
public interface MapTilesourceable extends CursorLocationable{
	
    TileSource getTileSource();

}
