package jfxtras.labs.map.tile;

/**
 * Interface for way to load the tiles.
 * @author Mario Schroeder
 *
 */
public interface TileLoadStrategy {

	Tile execute(String location);
	
	void setCache(TileInfoCache cache);
}
