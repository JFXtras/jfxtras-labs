//==============================================================================
//   Copyright (C) 2012 Jeffrey L Smith
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//    
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//    
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//    
//  For more information, please email jsmith.carlsbad@gmail.com
//    
//==============================================================================
package jfxtras.labs.map.tile;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A repository for the map tiles.
 * 
 * @author jsmith.carlsbad@gmail.com
 * @author Mario Schroeder
 * 
 */
public class TileRepository {

	private TileSource tileSource;

	private Map<String, TileInfo> cache;
	
	private long expire;
	
	/**
	 * Default expire time for the cache: one hour.
	 */
	public static final long DEFAULT_EXPIRE = 60 * 60 * 1000;
	
	public TileRepository(TileSource source) {
		tileSource = source;
		cache = new ConcurrentHashMap<>();
		expire = DEFAULT_EXPIRE;
	}

	public Tile getTile(int tilex, int tiley, int zoom) {
		Tile tile = null;

		if (isValid(tilex, tiley, zoom)) {

			cleanupCache();
			
            String location = tileSource.getTileUrl(zoom, tilex, tiley);
			TileInfo info = cache.get(location);

			if (info != null) {
				tile = new Tile(location, info.getImage());
			} else {
				tile = new Tile(location);
				tile.loadImage(cache);
			}
		}

		return tile;
	}

	/**
	 * Checks the expiration of images in the cache, and removes them eventually.
	 * @param location
	 */
	private void cleanupCache() {
		
		for(Entry<String, TileInfo> entry : cache.entrySet()){
			TileInfo info = entry.getValue();
			long current = System.currentTimeMillis();
			if(current - info.getTimeStamp() > expire){
				cache.remove(entry.getKey());
			}
		}
	}

	private boolean isValid(int tilex, int tiley, int zoom) {
		int max = (1 << zoom);
		return !(tilex < 0 || tilex >= max || tiley < 0 || tiley >= max);
	}

	public TileSource getTileSource() {
		return tileSource;
	}

	public void setTileSource(TileSource tileSource) {
		this.tileSource = tileSource;
	}
	
    /**
     * Set the time difference when a image in the cache expires.
     * @param expire difference as long
     */
	public void setExpire(long expire) {
		this.expire = expire;
	}
}
