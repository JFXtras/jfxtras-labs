package jfxtras.labs.map.tile;

/**
 * Factory for a tile source to the bing server.
 *
 * @author msc
 */
public class BingTileSourceFactory extends TileSourceFactory {

    private static final int ZOOM = 22;

    private static final String EXT = "jpeg";
    
     @Override
    public TileSource create() {
        return create(null);
    }

    @Override
    public TileSource create(String type) {

        DefaultTileSource tileSource = new BingAerialTileSource();
        tileSource.setMaxZoom(ZOOM);
        tileSource.setTileType(EXT);
        tileSource.setTermsOfUserURL("http://opengeodata.org/microsoft-imagery-details");
        
        // FIXME: I've set attributionLinkURL temporarily to ToU URL to comply with bing ToU
        // (the requirement is that we have such a link at the bottom of the window)
        tileSource.setAttributionLinkURL("http://go.microsoft.com/?linkid=9710837");
        
        return tileSource;
    }
}
