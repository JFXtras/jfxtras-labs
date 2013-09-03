/**
 * BingMetaDataHandler.java
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

import java.util.ArrayList;
import java.util.List;

import jfxtras.labs.map.tile.Attribution;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parent class for bing metadata handling.
 * @author Mario Schroeder
 */
public abstract class BingMetaDataHandler extends DefaultHandler {

    protected Attribution attribution;

    protected List<Attribution> attributions;

    protected String text;
    
    private String apiKey;

    public BingMetaDataHandler(String apiKey) {
        this.apiKey = apiKey;
        attributions = new ArrayList<>();
    }
    
    public List<Attribution> getAttributions() {
        return attributions;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        text = new String(ch, start, length);
    }
    
    /**
     * This method returns the string to the metadata url including the api key.
     * @return url to metadata as string
     */
    public String getMetaDataUrl(){
        return getMetaData("&key="+ apiKey);
    }

    /**
     * This method needs to return the path to the metadata with the api key appended.
     * Sample: http://dev.xy.com/meta/xxx&key=uvwxyz
     * @return path to metadata as string
     */
    protected abstract String getMetaData(String keyToAppend);

    
}
