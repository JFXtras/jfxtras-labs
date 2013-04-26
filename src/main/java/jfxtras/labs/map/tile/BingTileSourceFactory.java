package jfxtras.labs.map.tile;

import jfxtras.labs.map.tile.attribution.BingMapMetaDataHandler;
import jfxtras.labs.map.tile.attribution.BingAttributionLoader;
import jfxtras.labs.map.tile.attribution.BingMetaDataHandler;
import jfxtras.labs.map.tile.attribution.BingImageMetaDataHandler;
import jfxtras.labs.map.tile.attribution.Attribution;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;

/**
 * Factory for a tile source to the bing server.
 *
 * @author Mario Schröder
 */
public class BingTileSourceFactory extends ApiKeyTileSourceFactory<BingType> {

    private Map<BingType, List<Attribution>> attrMap;

    private static final int ZOOM = 21;

    private static final String TILE_URL = "http://ecn.t2.tiles.virtualearth.net/tiles/";

    public BingTileSourceFactory(String apiKey) {
        super(apiKey);
        attrMap = new HashMap<>();
    }

    @Override
    public TileSource create() {
        return create(BingType.Map);
    }

    @Override
    public TileSource create(BingType type) {

        BingTileSource tileSource;

        BingTilePathBuilder tilePathBuilder = new BingTilePathBuilder();
        List<Attribution> attributions = getAttributions(type);

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

    private List<Attribution> getAttributions(BingType type) {

        List<Attribution> attr = attrMap.get(type);
        if (attr == null) {
            attr = loadAttributions(type);
            attrMap.put(type, attr);
        }
        return attr;
    }

    private List<Attribution> loadAttributions(BingType type) {

        BingMetaDataHandler handler;

        if (BingType.Aerial.equals(type)) {
            handler = new BingImageMetaDataHandler(getApiKey());
        } else {
            handler = new BingMapMetaDataHandler(getApiKey());
        }

        BingAttributionLoader loader = new BingAttributionLoader(handler);
        return loader.load();
    }
}
