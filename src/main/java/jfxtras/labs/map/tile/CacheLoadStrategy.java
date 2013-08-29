package jfxtras.labs.map.tile;

/**
 * This class checks the {@link TileInfoCache} first if the 
 * {@link Tile} exists and loads it from there.
 * 
 * @author Mario Schroeder
 *
 */
public class CacheLoadStrategy extends AbstractTileLoadStrategy {

	@Override
	public Tile execute(String location) {
		Tile tile;
		TileInfo info = cache.get(location);

        if (info != null) {
            tile = new Tile(location, info.getImage());
        } else {
            tile = createTile(location);
        }
        return tile;
	}

}
