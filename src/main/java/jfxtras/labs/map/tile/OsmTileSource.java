package jfxtras.labs.map.tile;

public class OsmTileSource {

    public static class Mapnik extends AbstractTileSource {

        private static final String MAP_MAPNIK = "http://tile.openstreetmap.org";

        public Mapnik() {
            super("Mapnik", MAP_MAPNIK);
        }
    }

    public static class CycleMap extends AbstractTileSource {

        private static final String PATTERN = "http://%s.tile.opencyclemap.org/cycle";

        private static final String[] SERVER = {"a", "b", "c"};

        private int serverNumber = 0;

        public CycleMap() {
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

    public static abstract class OsmaSource extends AbstractTileSource {

        private static final String MAP_OSMA = "http://tah.openstreetmap.org/Tiles";

        private String osmaSuffix;

        public OsmaSource(String name, String osmaSuffix) {
            super(name, MAP_OSMA);
            this.osmaSuffix = osmaSuffix;
        }

        @Override
        public int getMaxZoom() {
            return 17;
        }

        @Override
        public String getBaseUrl() {
            return MAP_OSMA + "/" + osmaSuffix;
        }
    }

    public static class TilesAtHome extends OsmaSource {

        public TilesAtHome() {
            super("TilesAtHome", "tile");
        }
    }

    public static class Maplint extends OsmaSource {

        public Maplint() {
            super("Maplint", "maplint");
        }
    }
}
