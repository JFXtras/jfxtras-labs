package jfxtras.labs.map.tile;

/**
 * Parent class for all {@link TileSource} factories.
 * @author msc
 */
public abstract class TileSourceFactory {
    
    /**
     * Creates a new tile source based on the given type.<br/>
     * The type can be null.
     * 
     * @return new {@link TileSource}
     */
    public abstract TileSource create(String type);
}
