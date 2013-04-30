package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schroeder
 */
public class GoogleTilePathBuilder extends AbstractTilePathBuilder{

    @Override
    public String buildPath(int zoom, int tilex, int tiley) {
        StringBuilder builder = new StringBuilder();
        builder.append(SLASH).append("x=").append(tilex);
        builder.append(AMPERSAND).append("y=").append(tiley);
        builder.append(AMPERSAND).append("z=").append(zoom);
        return builder.toString();
    }
    
}
