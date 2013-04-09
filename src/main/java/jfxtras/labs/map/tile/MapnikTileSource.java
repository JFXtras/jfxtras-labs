package jfxtras.labs.map.tile;

/**
 *
 * @author Mario Schröder
 */
public class MapnikTileSource extends OsmTileSource {

    private static final String MAP_MAPNIK = "http://tile.openstreetmap.org";

    public MapnikTileSource() {
        super("Mapnik", MAP_MAPNIK);
    }
}
