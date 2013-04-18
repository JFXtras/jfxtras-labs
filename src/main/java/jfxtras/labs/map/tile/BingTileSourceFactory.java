package jfxtras.labs.map.tile;

/**
 * Factory for a tile source to the bing server.
 *
 * @author Mario Schröder
 */
public class BingTileSourceFactory extends TileSourceFactory<BingType> {

    private static final int ZOOM = 21;

    private static final String EXT = "jpeg";

    private static final String TILE_URL = "http://ecn.t2.tiles.virtualearth.net/tiles/";
    
    private String apiKey;

    public BingTileSourceFactory(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public TileSource create() {
        return create(BingType.Map);
    }

    @Override
    public TileSource create(BingType type) {

        BingTileSource tileSource;

        if (BingType.Aerial.equals(type)) {
            tileSource = new BingTileSource("Bing Aerial Maps", TILE_URL);
            tileSource.setTilePath("tiles/a");
        } else {
            tileSource = new BingTileSource("Bing Maps", TILE_URL);
            tileSource.setTilePath("tiles/r");
        }

        tileSource.setApiKey(apiKey);
        tileSource.setMaxZoom(ZOOM);
        tileSource.setTileType(EXT);
        tileSource.setTermsOfUserURL("http://opengeodata.org/microsoft-imagery-details");

        // FIXME: I've set attributionLinkURL temporarily to ToU URL to comply with bing ToU
        // (the requirement is that we have such a link at the bottom of the window)
        tileSource.setAttributionLinkURL("http://go.microsoft.com/?linkid=9710837");

        tileSource.setAttributionRequired(true);

        return tileSource;
    }
}
