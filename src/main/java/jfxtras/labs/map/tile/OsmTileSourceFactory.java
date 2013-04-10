package jfxtras.labs.map.tile;

/**
 * Factory for OSM based tile sources.
 * @author Mario Schröder
 */
public class OsmTileSourceFactory extends TileSourceFactory {

    private static final int ZOOM = 17;

    @Override
    public DefaultTileSource create(String type) {

        DefaultTileSource osm = new DefaultTileSource("Mapnik", "http://tile.openstreetmap.org");
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
        
        osm.setAttributionLinkURL("http://openstreetmap.org/");
        osm.setTermsOfUserURL("http://www.openstreetmap.org/copyright");
        osm.setAttributionText("© OpenStreetMap contributors, CC-BY-SA ");

        return osm;
    }

    private DefaultTileSource createExtended(String name, String baseUrl) {

        DefaultTileSource osm = new DefaultTileSource(name, baseUrl);
        osm.setUrlBuilder(new AlternateOsmTileUrlBuilder());
        osm.setMaxZoom(ZOOM);

        return osm;
    }
}
