/**
 * BingMapMetaDataHandler.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfxtras.labs.map.tile.Attribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Metadata XML handler for bin map.
 *
 * @author Mario Schroeder
 */
public class BingMapMetaDataHandler extends BingMetaDataHandler {
    
    private static final String METADATA = "http://dev.virtualearth.net/REST/V1/Imagery/Copyright/en-us/Road/1/0/0/0/0?output=xml&dir=0";

    private static final String CR_PATTERN = "\\S\\s\\d+\\s\\w+";

    private static final String CR_TAG = "Copyright";

    private static final String STRING = "string";
    
    private String copyRight;

    private Pattern pattern;

    private boolean isCopyRight;

    public BingMapMetaDataHandler(String apiKey) {
        super(apiKey);
        pattern = Pattern.compile(CR_PATTERN);
    }

    @Override
    public void startElement(String uri, String stripped, String tagName, Attributes attrs) throws SAXException {
        switch (tagName) {
            case CR_TAG:
                isCopyRight = true;
                break;
            case STRING:
                attribution = createAttribution();
                break;
            default:
                break;
        }
    }
    
    private Attribution createAttribution() {
        Attribution attr = new Attribution();
        //use a pointless max zoom level to indicate that we don't care about zoom and coordinates
        attr.setMaxZoom(Integer.MAX_VALUE);
        return attr;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        super.characters(ch, start, length);

        if (isCopyRight) {
            copyRight = extractDefaultCopyRight(text);
        }
    }

    /**
     * extract the dfault copyrights from the string.
     * @param string
     * @return 
     */
    protected String extractDefaultCopyRight(String string) {
        String cr = "";
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            cr = matcher.group();
        }
        return cr;
    }

    @Override
    public void endElement(String uri, String stripped, String tagName) throws SAXException {

        switch (tagName) {
            case CR_TAG:
                isCopyRight = false;
                break;
            case STRING:
                attribution.setText(text);
                attributions.add(attribution);
                break;
            default:
                break;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        attribution = createAttribution();
        attribution.setText(copyRight + " Corporation");
        attributions.add(attribution);
    }

    @Override
    public String getMetaData(String key) {
        return METADATA + key;
    }

}
