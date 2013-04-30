package jfxtras.labs.map.tile;

import javafx.scene.image.Image;

/**
 *
 * @author Mario Schroeder
 */
public class GoogleTileSourceFactory extends ApiKeyTileSourceFactory<GoogleType>{
    
    private static final String TILE_URL =  "http://%s.google.com/vt";

    public GoogleTileSourceFactory(String apiKey) {
        super(apiKey);
    }

    @Override
    public TileSource create() {
        return create(GoogleType.Street);
    }

    @Override
    public TileSource create(GoogleType type) {
        
        DefaultTileSource tileSource = new DefaultTileSource("Google Maps", TILE_URL);
        
        AlternateOsmTileUrlBuilder urlBuilder = new AlternateOsmTileUrlBuilder();
        urlBuilder.setServers(new String[]{"mt0", "mt1", "mt2", "mt3"});
        tileSource.setUrlBuilder(urlBuilder);
        
        TilePathBuildable tilePathBuilder = new GoogleTilePathBuilder();
        tileSource.setTilePathBuilder(tilePathBuilder);
        
        Image image = new Image(getClass().getResourceAsStream("google_white.png"));
        tileSource.setAttributionImage(image);
        
        return tileSource;
    }
    
}
