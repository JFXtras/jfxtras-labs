package jfxtras.labs.map.tile;

/**
 * Interface for all {@link TileSource} factories.
 * @author Mario Schroeder
 */
public interface TileSourceFactory<T> {
    
    /**
     * Creates a new tile source.
     * @return new {@link TileSource}
     */
   TileSource create();
    
    /**
     * Creates a new tile source based on the given type.<br/>
     * The type can be null.
     * 
     * @return new {@link TileSource}
     */
    TileSource create(T type);
}
