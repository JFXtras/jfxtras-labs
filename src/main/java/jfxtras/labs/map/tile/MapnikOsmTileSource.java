package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public class MapnikOsmTileSource extends OsmTileSource {

    private static final String MAP_MAPNIK = "http://tile.openstreetmap.org";

    public MapnikOsmTileSource() {
        super("Mapnik", MAP_MAPNIK);
    }
}
