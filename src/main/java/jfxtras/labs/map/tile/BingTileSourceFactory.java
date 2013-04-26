package jfxtras.labs.map.tile;

import java.util.List;
import javafx.scene.image.Image;

/**
 * Factory for a tile source to the bing server.
 *
 * @author Mario Schröder
 */
public class BingTileSourceFactory extends ApiKeyTileSourceFactory<BingType> {

    private static final int ZOOM = 21;

    private static final String TILE_URL = "http://ecn.t2.tiles.virtualearth.net/tiles/";

    private static final String IMG_METADATA = "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial/0,0?zl=1&mapVersion=v1&include=ImageryProviders&output=xml&key=";

    private static final String MAP_METADATA = "http://dev.virtualearth.net/REST/V1/Imagery/Copyright/en-us/Road/14/0/0/0/0?output=xml&dir=0&key=";

    public BingTileSourceFactory(String apiKey) {
        super(apiKey);
    }

    @Override
    public TileSource create() {
        return create(BingType.Map);
    }

    @Override
    public TileSource create(BingType type) {

        BingTileSource tileSource;

        BingTilePathBuilder tilePathBuilder = new BingTilePathBuilder();
        List<Attribution> attributions = loadAttributions(type);

        if (BingType.Aerial.equals(type)) {
            tileSource = new BingTileSource("Bing Aerial Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/a");
        } else {
            tileSource = new BingTileSource("Bing Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/r");
        }

        tileSource.setTilePathBuilder(tilePathBuilder);
        tileSource.setAttributions(attributions);
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

    private List<Attribution> loadAttributions(BingType type) {

        String url;
        BingMetadataHandler handler;

        if (BingType.Aerial.equals(type)) {
            handler = new BingImageMetadataHandler();
            url = buildMetadataUrl(IMG_METADATA);
        } else {
            handler = new BingMetadataHandler();
            url = buildMetadataUrl(MAP_METADATA);
        }

        BingAttributionLoader loader = new BingAttributionLoader(url, handler);
        loader.load();
        return loader.getAttributions();
    }

    private String buildMetadataUrl(String urlString) {
        StringBuilder builder = new StringBuilder();
        builder.append(urlString).append(getApiKey());
        return builder.toString();
    }
}
