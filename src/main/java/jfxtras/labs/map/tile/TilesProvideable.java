package jfxtras.labs.map.tile;

/**
 * Interface for a tile repository.
 *
 * @author Mario Schroeder
 */
public interface TilesProvideable {
	
	/**
	 * Returns the tile for x, y and zoom. 
	 * It might also return null if the arguments are invalid.
	 * @param tilex x position of the tile
	 * @param tiley y position of the tile
	 * @param zoom the zoom level
	 * @return the {@link Tile} or null if arguments are invalid.
	 */
    Tile findTile(int tilex, int tiley, int zoom);

    TileSource getTileSource();

    void setTileSource(TileSource tileSource);
}
