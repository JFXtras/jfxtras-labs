/**
 * TileSource.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
