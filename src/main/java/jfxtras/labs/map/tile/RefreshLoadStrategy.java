package jfxtras.labs.map.tile;

/**
 * This class reloads the {@link Tile} in the {@link TileInfoCache}.
 * 
 * @author Mario Schroeder
 *
 */
public class RefreshLoadStrategy extends AbstractTileLoadStrategy {

	@Override
	public Tile execute(String location) {
		TileInfo info = cache.get(location);

        if (info != null) {
        	cache.remove(location);
        } 
        
        return createTile(location); 
	}

}
