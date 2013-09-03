/**
 * BingImageMetaDataHandler.java
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

import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.tile.Attribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Metadata XML handler for bing images.
 * @author Mario Schroeder
 */
public class BingImageMetaDataHandler extends BingMetaDataHandler {
    
    private static final String METADATA = "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial/0,0?zl=1&mapVersion=v1&include=ImageryProviders&output=xml";

    private double southLat;

    private double northLat;

    private double eastLon;

    private double westLon;

    private boolean inCoverage = false;

    public BingImageMetaDataHandler(String apiKey) {
        super(apiKey);
    }
    
    @Override
    public void startElement(String uri, String stripped, String tagName, Attributes attrs) throws SAXException {
        switch (tagName) {
            case "ImageryProvider":
                attribution = new Attribution();
                break;
            case "CoverageArea":
                inCoverage = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String stripped, String tagName) throws SAXException {

        if ("ImageryProvider".equals(tagName)) {
            attributions.add(attribution);
        } else if ("Attribution".equals(tagName)) {
            attribution.setText(text);
        } else if (inCoverage && "ZoomMin".equals(tagName)) {
            attribution.setMinZoom(Integer.parseInt(text));
        } else if (inCoverage && "ZoomMax".equals(tagName)) {
            attribution.setMaxZoom(Integer.parseInt(text));
        } else if (inCoverage && "SouthLatitude".equals(tagName)) {
            southLat = Double.parseDouble(text);
        } else if (inCoverage && "NorthLatitude".equals(tagName)) {
            northLat = Double.parseDouble(text);
        } else if (inCoverage && "EastLongitude".equals(tagName)) {
            eastLon = Double.parseDouble(text);
        } else if (inCoverage && "WestLongitude".equals(tagName)) {
            westLon = Double.parseDouble(text);
        } else if ("BoundingBox".equals(tagName)) {
            attribution.setMin(new Coordinate(southLat, westLon));
            attribution.setMax(new Coordinate(northLat, eastLon));
        } else if ("CoverageArea".equals(tagName)) {
            inCoverage = false;
        }
        text = "";
    }

    @Override
    public String getMetaData(String key) {
        return METADATA + key;
    }

}
