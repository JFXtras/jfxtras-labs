/**
 * TileInfoCache.java
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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the cache for the tiles.
 * @author Mario Schroeder
 *
 */
public class TileInfoCache extends ConcurrentHashMap<String,TileInfo>{

	private static final long serialVersionUID = 5447380251151974820L;

	/**
     * Default expire time for the cache: one hour.
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 1000;
    
    private long expire;
    
    public TileInfoCache() {
		expire = DEFAULT_EXPIRE;
	}
    
    /**
     * Set the time difference when a image in the cache expires.
     *
     * @param expire difference as long
     */
    public void setExpire(long expire) {
		this.expire = expire;
	}
    
    /**
     * Checks the expiration of images in the cache, and removes them eventually.
     *
     * @param location
     */
    public void cleanup() {

        Set<Entry<String, TileInfo>> entries = entrySet();
        for (Entry<String, TileInfo> entry : entries) {
            TileInfo info = entry.getValue();
            long current = System.currentTimeMillis();
            if (current - info.getTimeStamp() > expire) {
                remove(entry.getKey());
            }
        }
    }
}
