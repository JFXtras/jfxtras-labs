package jfxtras.labs.map.tile;

import javafx.scene.image.Image;

/**
 * Factory for a tile source to the bing server.
 *
 * @author Mario Schröder
 */
public class BingTileSourceFactory extends ApiKeyTileSourceFactory<BingType> {

    private static final int ZOOM = 21;

    private static final String TILE_URL = "http://ecn.t2.tiles.virtualearth.net/tiles/";
    
    public BingTileSourceFactory(String apiKey) {
        super(apiKey);
    }
    
    @Override
    public TileSource create() {
        return create(BingType.Map);
    }

    @Override
    public TileSource create(BingType type) {

        DefaultTileSource tileSource;
        
        BingTilePathBuilder tilePathBuilder = new BingTilePathBuilder();

        if (BingType.Aerial.equals(type)) {
            tileSource = new BingAerialTileSource("Bing Aerial Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/a");
        } else {
            tileSource = new DefaultTileSource("Bing Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/r");
            tileSource.setAttributionText("© Nokia © Microsoft ");
        }

        tileSource.setTilePathBuilder(tilePathBuilder);
        tileSource.setApiKey(getApiKey());
        tileSource.setMaxZoom(ZOOM);
        tileSource.setTermsOfUserURL("http://opengeodata.org/microsoft-imagery-details");
        
        Image image = new Image(getClass().getResourceAsStream("bing_maps.png"));
        tileSource.setAttributionImage(image);
        

        // FIXME: I've set attributionLinkURL temporarily to ToU URL to comply with bing ToU
        // (the requirement is that we have such a link at the bottom of the window)
        tileSource.setAttributionLinkURL("http://go.microsoft.com/?linkid=9710837");

        tileSource.setAttributionRequired(true);

        return tileSource;
    }
}
