package jfxtras.labs.map.tile.bing;

import jfxtras.labs.map.tile.AbstractTilePathBuilder;

/**
 *
 * @author Mario Schroeder
 */
public class BingTilePathBuilder extends AbstractTilePathBuilder{
    
    private String tilePath;

    public BingTilePathBuilder() {
        setTileType(JPEG_EXT);
    }
   
    @Override
    public String buildPath(int zoom, int tilex, int tiley) {

        String quadtree = computeQuadTree(zoom, tilex, tiley);
        StringBuilder builder = new StringBuilder();
        builder.append(tilePath).append(quadtree).append(DOT).append(getTileType()).append("?g=587");
        return builder.toString();
    }
    
    private String computeQuadTree(int zoom, int tilex, int tiley) {
        StringBuilder k = new StringBuilder();
        for (int i = zoom; i > 0; i--) {
            char digit = 48;
            int mask = 1 << (i - 1);
            if ((tilex & mask) != 0) {
                digit += 1;
            }
            if ((tiley & mask) != 0) {
                digit += 2;
            }
            k.append(digit);
        }
        return k.toString();
    }

    public void setTilePath(String tilePath) {
        this.tilePath = tilePath;
    }
    
}
