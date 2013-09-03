/**
 * AlternateOsmTileUrlBuilder.java
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

package jfxtras.labs.map.tile.osm;

import jfxtras.labs.map.tile.TileUrlBuildable;

/**
 * This class builds the urls to an osm tiles server.
 * 
 * @author Mario Schroeder
 */
public class AlternateOsmTileUrlBuilder implements TileUrlBuildable{
    
    private String[] servers;
    
    private int serverNumber = 0;

    public AlternateOsmTileUrlBuilder() {
        servers = new String[]{"a", "b", "c"};
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }
    
    /**
     * This method builds the url.
     * The url is alternated each time the method is called.<br/>
     * Sample:<br/>
     * <ol>
     * <li>http://a.tile.opencyclemap.org/cycle</li>
     * <li>http://b.tile.opencyclemap.org/cycle</li>
     * </ol>
     *
     * The parameter must contain a '%s', because it will be replaced.
     * 
     * @param baseUrl the url which will alternated. Example: http://%s.tile.opencyclemap.org/cycle
     * @return the url as string
     */
    @Override
    public String build(String baseUrl){
        String url = String.format(baseUrl, new Object[]{servers[serverNumber]});
        serverNumber = (serverNumber + 1) % servers.length;
        return url;
    }
    
}
