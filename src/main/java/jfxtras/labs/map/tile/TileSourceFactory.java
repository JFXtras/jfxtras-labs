package jfxtras.labs.map.tile;

/**
 * Parent class for all {@link TileSource} factories.
 * @author Mario Schröder
 */
public abstract class TileSourceFactory<T> {
    
    /**
     * Creates a new tile source.
     * @return new {@link TileSource}
     */
    public abstract TileSource create();
    
    /**
     * Creates a new tile source based on the given type.<br/>
     * The type can be null.
     * 
     * @return new {@link TileSource}
     */
    public abstract TileSource create(T type);
}
