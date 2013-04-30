package jfxtras.labs.map.tile;

/**
 * Interface for builders which construct the path to the tile.
 * @author Mario Schroeder
 */
public interface TilePathBuildable {

    String SLASH = "/";

    String DOT = ".";
    
    String AMPERSAND = "&";

    /**
     * png image
     */
    String PNG_EXT = "png";
    
    String JPEG_EXT = "jpeg";

    String buildPath(int zoom, int tilex, int tiley);

    /**
     * Specifies the tile image type. For tiles rendered by Mapnik or Osmarenderer this is usually
     * <code>"png"</code>.
     *
     * @return file extension of the tile image type
     */
    String getTileType();

    void setTileType(String tileType);
}
