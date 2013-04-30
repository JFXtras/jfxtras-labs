package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;
import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Abstract parent for tile sources.
 *
 * @author Mario Schroeder
 */
public class DefaultTileSource implements TileSource {

    public static final int DEFAULT_TILE_SIZE = 256;

    protected String name;

    protected String baseUrl;

    private Image attributionImage;

    private int minZoom = 1;

    private int maxZoom = 18;

    private boolean attributionRequired = true;

    private TileUrlBuildable urlBuilder;
    
    private TilePathBuildable tilePathBuilder;

    private String termsOfUserURL;

    private String attributionLinkURL;

    private String attributionText;

    protected String apiKey;

    public DefaultTileSource(String name, String base_url) {
    	this.name = name;
        this.baseUrl = base_url;
        
        tilePathBuilder = new OsmTilePathBuilder();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxZoom() {
        return maxZoom;
    }

    @Override
    public int getMinZoom() {
        return minZoom;
    }

    /**
     * @throws IOException when subclass cannot return the tile URL
     */
    public String getTilePath(int zoom, int tilex, int tiley) {
        
        return tilePathBuilder.buildPath(zoom, tilex, tiley);
    }

    public void setUrlBuilder(TileUrlBuildable urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public String getBaseUrl() {

        String url;

        if (urlBuilder != null) {
            url = urlBuilder.build(baseUrl);
        } else {
            url = baseUrl;
        }

        return url;
    }

    @Override
    public String getTileUrl(int zoom, int tilex, int tiley) {
        return this.getBaseUrl() + getTilePath(zoom, tilex, tiley);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int getTileSize() {
        return DEFAULT_TILE_SIZE;
    }

    @Override
    public Image getAttributionImage() {
		return attributionImage;
	}

	public void setAttributionImage(Image attributionImage) {
		this.attributionImage = attributionImage;
	}

	@Override
    public boolean isAttributionRequired() {
        return attributionRequired;
    }

    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {
        return attributionText;
    }

    public void setAttributionText(String attributionText) {
        this.attributionText = attributionText;
    }

    @Override
    public String getAttributionLinkURL() {
        return attributionLinkURL;
    }

    public void setAttributionLinkURL(String attributionLinkURL) {
        this.attributionLinkURL = attributionLinkURL;
    }

    @Override
    public String getTermsOfUseURL() {
        return termsOfUserURL;
    }

    public void setTermsOfUserURL(String termsOfUserURL) {
        this.termsOfUserURL = termsOfUserURL;
    }

    @Override
    public double latToTileY(double lat, int zoom) {
        double l = lat / 180 * Math.PI;
        double pf = Math.log(Math.tan(l) + (1 / Math.cos(l)));
        return Math.pow(2.0, zoom - 1) * (Math.PI - pf) / Math.PI;
    }

    @Override
    public double lonToTileX(double lon, int zoom) {
        return Math.pow(2.0, zoom - 3) * (lon + 180.0) / 45.0;
    }

    @Override
    public double tileYToLat(int y, int zoom) {
//        return OsmMercator.YToLat( y, zoom);
        return Math.atan(Math.sinh(Math.PI - (Math.PI * y / Math.pow(2.0, zoom - 1)))) * 180 / Math.PI;
    }

    @Override
    public double tileXToLon(int x, int zoom) {
//        return OsmMercator.XToLon( x,zoom);
        return x * 45.0 / Math.pow(2.0, zoom - 3) - 180.0;
    }

    protected final void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    protected final void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void setAttributionRequired(boolean required) {
        this.attributionRequired = required;
    }

    protected String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setTilePathBuilder(TilePathBuildable tilePathBuilder) {
        this.tilePathBuilder = tilePathBuilder;
    }
    
    
}