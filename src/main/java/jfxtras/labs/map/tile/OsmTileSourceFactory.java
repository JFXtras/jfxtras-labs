package jfxtras.labs.map.tile;

/**
 * Factory for OSM based tile sources.
 *
 * @author Mario Schroeder
 */
public class OsmTileSourceFactory implements TileSourceFactory<OsmType> {

    private static final int ZOOM = 18;

    @Override
    public TileSource create() {
        return create(OsmType.Mapnik);
    }

    @Override
    public TileSource create(OsmType type) {

        DefaultTileSource tileSource;
        
        switch (type) {
            case CycleMap:
                tileSource = createExtended("OSM Cycle Map", "http://%s.tile.opencyclemap.org/cycle");
                break;
            case Transport:
                tileSource = createExtended("OSM Transport Map", "http://%s.tile2.opencyclemap.org/transport");
                break;
            case Landscape:
                tileSource = createExtended("OSM Landscape Map", "http://%s.tile3.opencyclemap.org/landscape");
                break;
            default:
                tileSource = new DefaultTileSource("Mapnik", "http://tile.openstreetmap.org");
        }
        tileSource.setAttributionRequired(false);
        tileSource.setMaxZoom(ZOOM);

        return tileSource;
    }

    private DefaultTileSource createExtended(String name, String baseUrl) {

        DefaultTileSource osm = new DefaultTileSource(name, baseUrl);
        osm.setUrlBuilder(new AlternateOsmTileUrlBuilder());
        return osm;
    }
}
