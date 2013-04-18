package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public interface TilePathBuildable {
    
    String SLASH = "/";

    String DOT = ".";
    
    String PNG_EXT = "png";
    
    String buildPath(int zoom, int tilex, int tiley);

    String getTileType();

    void setTileType(String tileType);
    
    
}
