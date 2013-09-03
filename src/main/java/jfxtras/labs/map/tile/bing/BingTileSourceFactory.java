/**
 * BingTileSourceFactory.java
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

package jfxtras.labs.map.tile.bing;

import jfxtras.labs.map.tile.ApiKeyTileSourceFactory;
import jfxtras.labs.map.tile.Attribution;
import jfxtras.labs.map.tile.TileSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * Factory for a tile source to the bing server.
 *
 * @author Mario Schroeder
 */
public class BingTileSourceFactory extends ApiKeyTileSourceFactory<BingType> {

    private Map<BingType, List<Attribution>> attrMap;

    private static final int ZOOM = 19;

    private static final String TILE_URL = "http://ecn.t2.tiles.virtualearth.net/tiles/";

    public BingTileSourceFactory(String apiKey) {
        super(apiKey);
        attrMap = new HashMap<>();
    }

    @Override
    public TileSource create() {
        return create(BingType.Map);
    }

    @Override
    public TileSource create(BingType type) {

        BingTileSource tileSource;

        BingTilePathBuilder tilePathBuilder = new BingTilePathBuilder();
        List<Attribution> attributions = getAttributions(type);

        if (BingType.Aerial.equals(type)) {
            tileSource = new BingTileSource("Bing Aerial Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/a");
        } else {
            tileSource = new BingTileSource("Bing Maps", TILE_URL);
            tilePathBuilder.setTilePath("tiles/r");
        }

        tileSource.setTilePathBuilder(tilePathBuilder);
        tileSource.setAttributions(attributions);
        tileSource.setApiKey(getApiKey());
        tileSource.setMaxZoom(ZOOM);
        tileSource.setTermsOfUserURL("http://opengeodata.org/microsoft-imagery-details");

        Image image = new Image(getClass().getResourceAsStream("bing_maps.png"));
        tileSource.setAttributionImage(image);

        // FIXME: I've set attributionLinkURL temporarily to ToU URL to comply with bing ToU
        // (the requirement is that we have such a link at the bottom of the window)
        tileSource.setAttributionLinkURL("http://go.microsoft.com/?linkid=9710837");

        tileSource.setAttributionRequired(true);

        return tileSource;
    }

    private List<Attribution> getAttributions(BingType type) {

        List<Attribution> attr = attrMap.get(type);
        if (attr == null) {
            attr = loadAttributions(type);
            attrMap.put(type, attr);
        }
        return attr;
    }

    private List<Attribution> loadAttributions(BingType type) {

        BingMetaDataHandler handler;

        if (BingType.Aerial.equals(type)) {
            handler = new BingImageMetaDataHandler(getApiKey());
        } else {
            handler = new BingMapMetaDataHandler(getApiKey());
        }

        BingAttributionLoader loader = new BingAttributionLoader(handler);
        return loader.load();
    }
}
