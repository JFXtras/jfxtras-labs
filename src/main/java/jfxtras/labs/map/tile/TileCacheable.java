package jfxtras.labs.map.tile;

/**
 * Interface for a tile cache.
 *
 * @author Mario Schroeder
 */
public interface TileCacheable {

    Tile getTile(int tilex, int tiley, int zoom);

    TileSource getTileSource();

    void setTileSource(TileSource tileSource);
}
