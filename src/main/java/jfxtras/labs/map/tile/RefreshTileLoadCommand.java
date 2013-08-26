package jfxtras.labs.map.tile;

/**
 * This class reloads the {@link Tile} in the {@link TileInfoCache}.
 * 
 * @author Mario Schroeder
 *
 */
public class RefreshTileLoadCommand extends TileLoadCommand {

	RefreshTileLoadCommand(TileInfoCache cache) {
		super(cache);
	}

	@Override
	Tile execute(String location) {
		TileInfo info = cache.get(location);

        if (info != null) {
        	cache.remove(location);
        } 
        
        return createTile(location);
        
	}

}
