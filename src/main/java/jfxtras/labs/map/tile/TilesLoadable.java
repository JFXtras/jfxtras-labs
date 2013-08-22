package jfxtras.labs.map.tile;

import java.util.List;

import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.tile.TileImage;

/**
 * This interface defines a feature to load tiles.
 * 
 * @author Mario Schroeder
 *
 */
public interface TilesLoadable {

	List<TileImage> loadTiles(Moveable mapController);
}
