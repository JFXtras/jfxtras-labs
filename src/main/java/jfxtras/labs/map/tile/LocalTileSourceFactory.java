package jfxtras.labs.map.tile;

/**
 * Factory for a tile source which is based on files.
 * @author Mario Schöder
 */
public class LocalTileSourceFactory extends TileSourceFactory {
    
    private static final int ZOOM = 16;

    private static final String TILES_ROOT = "GEOTILES";

    @Override
    public TileSource create(String rootDir) {

        LocalTileSource tileSource = new LocalTileSource("Local Tile Source", "/");
        tileSource.setMaxZoom(ZOOM);
        tileSource.setAttributionRequired(false);

        if (rootDir == null) {
            tileSource.setTilesRootDir(System.getenv(TILES_ROOT));
        } else {
            tileSource.setTilesRootDir(rootDir);
        }

        return tileSource;
    }
}
