package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;

/**
 * Class for OSM based tile sources.
 * @author Mario Schröder
 *
 */
public class OsmTileSource extends DefaultTileSource {

    private OsmTileUrlBuilder urlBuilder;

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

    public void setUrlBuilder(OsmTileUrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    @Override
    public String getBaseUrl() {
        
        String url = super.getBaseUrl();
        
        if (urlBuilder != null) {
            url = urlBuilder.build(url);
        } 

        return url;
    }
}
