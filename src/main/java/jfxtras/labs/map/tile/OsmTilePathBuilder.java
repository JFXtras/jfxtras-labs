package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schroeder
 */
public class OsmTilePathBuilder extends AbstractTilePathBuilder{
    
    @Override
    public String buildPath(int zoom, int tilex, int tiley) {
        
        StringBuilder builder = new StringBuilder();
        builder.append(SLASH).append(zoom).append(SLASH);
        builder.append(tilex).append(SLASH).append(tiley);
        builder.append(DOT).append(getTileType());
        return builder.toString();
    }
}
