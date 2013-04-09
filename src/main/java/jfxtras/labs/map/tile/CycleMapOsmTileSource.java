package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public class CycleMapOsmTileSource extends OsmTileSource {

    private static final String PATTERN = "http://%s.tile.opencyclemap.org/cycle";

    private static final String[] SERVER = {"a", "b", "c"};

    private int serverNumber = 0;

    public CycleMapOsmTileSource() {
        super("OSM Cycle Map", PATTERN);
    }

    @Override
    public String getBaseUrl() {
        String url = String.format(this.baseUrl, new Object[]{SERVER[serverNumber]});
        serverNumber = (serverNumber + 1) % SERVER.length;
        return url;
    }

    @Override
    public int getMaxZoom() {
        return 17;
    }
}
