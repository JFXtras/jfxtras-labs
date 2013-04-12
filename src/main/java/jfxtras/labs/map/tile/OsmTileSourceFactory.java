package jfxtras.labs.map.tile;

/**
 * Factory for OSM based tile sources.
 *
 * @author Mario Schröder
 */
public class OsmTileSourceFactory extends TileSourceFactory {

    private static final int ZOOM = 17;

    @Override
    public TileSource create() {
        return create(OsmType.Mapnik.name());
    }

    @Override
    public TileSource create(String type) {

        DefaultTileSource tileSource;
        
        String typeName = OsmType.Mapnik.name().toLowerCase();
        if (type != null) {
            typeName = type.toLowerCase();
        }
        
        switch (typeName) {
            case "cyclemap":
                tileSource = createExtended("OSM Cycle Map", "http://%s.tile.opencyclemap.org/cycle");
                break;
            case "transport":
                tileSource = createExtended("OSM Transport Map", "http://%s.tile2.opencyclemap.org/transport");
                break;
            case "landscape":
                tileSource = createExtended("OSM Landscape Map", "http://%s.tile3.opencyclemap.org/landscape");
                break;
            default:
                tileSource = new DefaultTileSource("Mapnik", "http://tile.openstreetmap.org");
        }

        tileSource.setAttributionLinkURL("http://openstreetmap.org/");
        tileSource.setTermsOfUserURL("http://www.openstreetmap.org/copyright");
        tileSource.setAttributionText("© OpenStreetMap contributors, CC-BY-SA ");

        return tileSource;
    }

    private DefaultTileSource createExtended(String name, String baseUrl) {

        DefaultTileSource osm = new DefaultTileSource(name, baseUrl);
        osm.setUrlBuilder(new AlternateOsmTileUrlBuilder());
        osm.setMaxZoom(ZOOM);

        return osm;
    }
}
