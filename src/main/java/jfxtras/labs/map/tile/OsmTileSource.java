package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;

public class OsmTileSource extends DefaultTileSource {
    
    public OsmTileSource(String name, String base_url) {
        super(name, base_url, null);
    }

    public OsmTileSource(String name, String base_url, String attr_img_url) {
        super(name, base_url, attr_img_url);
    }

    
    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {
        return "© OpenStreetMap contributors, CC-BY-SA ";
    }

    @Override
    public String getAttributionLinkURL() {
        return "http://openstreetmap.org/";
    }

    @Override
    public String getTermsOfUseURL() {
        return "http://www.openstreetmap.org/copyright";
    }
}
