package jfxtras.labs.map.tile;

/**
 * This interface defines methods to load the tile. 
 * The loading algorithm can be influenced with the {@link TileLoadStrategy}.
 * @author Mario Schroeder
 *
 */
public interface TileProvideable extends TilesSourceAccessable{

	/**
	 * Returns the tile for x, y and zoom. 
	 * It might also return null if the arguments are invalid.
	 * @param tilex x position of the tile
	 * @param tiley y position of the tile
	 * @param zoom the zoom level
	 * @return the {@link Tile} or null if arguments are invalid.
	 */
	Tile getTile(int tilex, int tiley, int zoom);
	
	void setStrategy(TileLoadStrategy strategy);
	
	TileLoadStrategy getStrategy();
}
