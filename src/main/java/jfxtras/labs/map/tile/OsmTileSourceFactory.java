package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public class OsmTileSourceFactory extends TileSourceFactory {

    private static final int ZOOM = 17;

    @Override
    public OsmTileSource create(String type) {

        OsmTileSource osm = new OsmTileSource("Mapnik", "http://tile.openstreetmap.org");
        if (type != null) {
            String typeName = type.toLowerCase();
            switch (typeName) {
                case "cyclemap":
                    osm = createExtended("OSM Cycle Map", "http://%s.tile.opencyclemap.org/cycle");
                    break;
                case "transport":
                    osm = createExtended("OSM Transport Map", "http://%s.tile2.opencyclemap.org/transport");
                    break;
                case "landscape":
                    osm = createExtended("OSM Landscape Map", "http://%s.tile3.opencyclemap.org/landscape");
                    break;
                default:
                //keep Mapnik
            }
        }

        return osm;
    }

    private OsmTileSource createExtended(String name, String baseUrl) {

        OsmTileSource osm = new OsmTileSource(name, baseUrl);
        osm.setUrlBuilder(new OsmTileUrlBuilder());
        osm.setMaxZoom(ZOOM);

        return osm;
    }
}
