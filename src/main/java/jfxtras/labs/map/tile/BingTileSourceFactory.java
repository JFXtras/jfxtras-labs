package jfxtras.labs.map.tile;

/**
 * Factory for a tile source to the bing server.
 *
 * @author msc
 */
public class BingTileSourceFactory extends TileSourceFactory {

    private static final int ZOOM = 22;

    private static final String EXT = "jpeg";

    @Override
    public TileSource create(String type) {

        DefaultTileSource tileSource = new BingTileSource();
        tileSource.setMaxZoom(ZOOM);
        tileSource.setTileType(EXT);
        return tileSource;
    }
}
