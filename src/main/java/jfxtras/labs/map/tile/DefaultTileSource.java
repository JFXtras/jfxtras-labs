/**
 * 
 */
package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;
import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Abstract parent for tile sources.
 * @author Mario Schröder
 */
public class DefaultTileSource implements TileSource {

    protected String name;
    protected String baseUrl;
    protected String attrImgUrl;

    public DefaultTileSource(String name, String base_url) {
        this(name, base_url, null);
    }

    public DefaultTileSource(String name, String base_url, String attr_img_url) {
        this.name = name;
        this.baseUrl = base_url;
        attrImgUrl = attr_img_url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxZoom() {
        return 18;
    }

    @Override
    public int getMinZoom() {
        return 0;
    }

    public String getExtension() {
        return "png";
    }

    /**
     * @throws IOException when subclass cannot return the tile URL
     */
    public String getTilePath(int zoom, int tilex, int tiley) {
        return "/" + zoom + "/" + tilex + "/" + tiley + "." + getExtension();
    }

    public String getBaseUrl() {
        return this.baseUrl;
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
    public String getTileType() {
        return "png";
    }

    @Override
    public int getTileSize() {
        return 256;
    }

    @Override
    public Image getAttributionImage() {
        if (attrImgUrl != null) {
            return new Image(attrImgUrl, true);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean requiresAttribution() {
        return true;
    }

    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {
        return "";
    }

    @Override
    public String getAttributionLinkURL() {
        return "";
    }

    @Override
    public String getTermsOfUseURL() {
        return "";
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
}