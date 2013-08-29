package jfxtras.labs.map.tile;

import java.util.List;

import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.tile.TileImage;

/**
 * This interface defines a method to load tiles.
 * 
 * @author Mario Schroeder
 *
 */
public interface TilesLoadable {

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 * @param mapController
	 * @return
	 */
	List<TileImage> loadTiles(Moveable mapController);
}
