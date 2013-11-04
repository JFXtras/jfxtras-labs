/**
 * DefaultTileSource.java
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

import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.tile.osm.OsmTilePathBuilder;

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

    private int minZoom = ZoomBounds.MIN.getValue();

    private int maxZoom = ZoomBounds.MAX.getValue();

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
        return Math.atan(Math.sinh(Math.PI - (Math.PI * y / Math.pow(2.0, zoom - 1)))) * 180 / Math.PI;
    }

    @Override
    public double tileXToLon(int x, int zoom) {
        return x * 45.0 / Math.pow(2.0, zoom - 3) - 180.0;
    }

    protected final void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public final void setMaxZoom(int maxZoom) {
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