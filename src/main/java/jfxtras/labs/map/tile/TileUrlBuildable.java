package jfxtras.labs.map.tile;

/**
 * Interface for a tile server url builder.
 * @author Mario Schroeder
 */
public interface TileUrlBuildable {
    
    /**
     * Build the url to the tile server.
     * @param baseUrl base url which will be modified
     * @return url as string.
     */
    String build(String baseUrl);
}
