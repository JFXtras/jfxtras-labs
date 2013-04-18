package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public abstract class AbstractTilePathBuilder implements TilePathBuildable{
    
    private String tileType;

    public AbstractTilePathBuilder() {
        tileType = PNG_EXT;
    }
    
    @Override
    public final void setTileType(String tileType) {
        this.tileType = tileType;
    }
    
    @Override
    public String getTileType() {
        return tileType;
    }
}
