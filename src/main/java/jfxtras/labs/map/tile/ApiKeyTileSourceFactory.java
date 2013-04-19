package jfxtras.labs.map.tile;

/**
 * This is a parent class for factories where a API key for the {@link TileSource} is required.
 * @author Mario Schröder
 */
public abstract class ApiKeyTileSourceFactory<T> implements TileSourceFactory<T>{
    
    private String apiKey;

    public ApiKeyTileSourceFactory(String apiKey) {
        this.apiKey = apiKey;
    }

    protected String getApiKey() {
        return apiKey;
    }
    
}
