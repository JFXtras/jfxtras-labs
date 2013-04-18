package jfxtras.labs.map.tile;

import javafx.scene.image.Image;
import jfxtras.labs.map.Coordinate;

//License: GPL. Copyright 2008 by Jan Peter Stotz

/**
 *
 * @author Jan Peter Stotz
 */
public interface TileSource {

    /**
     * Specifies the maximum zoom value. The number of zoom levels is [0..
     * {@link #getMaxZoom()}].
     *
     * @return maximum zoom value that has to be smaller or equal to
     *         {@link JMapViewer#MAX_ZOOM}
     */
    public int getMaxZoom();

    /**
     * Specifies the minimum zoom value. This value is usually 0.
     * Only for maps that cover a certain region up to a limited zoom level
     * this method should return a value different than 0.
     *
     * @return minimum zoom value - usually 0
     */
    public int getMinZoom();

    /**
     * A tile layer name has to be unique and has to consist only of characters
     * valid for filenames.
     *
     * @return Name of the tile layer
     */
    public String getName();

    /**
     * Constructs the tile url.
     *
     * @param zoom
     * @param tilex
     * @param tiley
     * @return fully qualified url for downloading the specified tile image
     */
    public String getTileUrl(int zoom, int tilex, int tiley);

    
    /**
     * Specifies how large each tile is.
     * @return The size of a single tile in pixels.
     */
    public int getTileSize();

    /**
     * @return True if the tile source requires attribution in text or image form.
     */
    public boolean isAttributionRequired();

    /**
     * @param zoom The optional zoom level for the view.
     * @param botRight The bottom right of the bounding box for attribution.
     * @param topLeft The top left of the bounding box for attribution.
     * @return Attribution text for the image source.
     */
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight);

    /**
     * @return The URL for the attribution image. Null if no image should be displayed.
     */
    public Image getAttributionImage();

    /**
     * @return The URL to open when the user clicks the attribution image.
     */
    public String getAttributionLinkURL();

    /**
     * @return The URL to open when the user clicks the attribution "Terms of Use" text.
     */
    public String getTermsOfUseURL();

    public double latToTileY(double lat, int zoom);

    public double lonToTileX(double lon, int zoom);

    public double tileYToLat(int y, int zoom);

    public double tileXToLon(int x, int zoom);
}
