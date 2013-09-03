/**
 * TileRepository.java
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

/**
 * A repository for the map tiles.
 *
 * @author jsmith.carlsbad@gmail.com
 * @author Mario Schroeder
 *
 */
public class TileRepository implements TileProvideable{

    private TileSource tileSource;

    private TileInfoCache cache = new TileInfoCache();
    
    private TileLoadStrategy strategy = new CacheLoadStrategy();

    public TileRepository(TileSource source) {
        tileSource = source;
        strategy.setCache(cache);
    }

	@Override
    public Tile getTile(int tilex, int tiley, int zoom) {
        Tile tile = null;

        if (isValid(tilex, tiley, zoom)) {
        	
        	String location = getLocation(zoom, tilex, tiley);
            tile = loadTile(location, strategy);
        }

        return tile;
    }
    
    private String getLocation(int zoom, int tilex, int tiley){
    	return tileSource.getTileUrl(zoom, tilex, tiley);
    }
    
    private Tile loadTile(String location, TileLoadStrategy strategy){
    	cache.cleanup();
        return strategy.execute(location);
    }

    private boolean isValid(int tilex, int tiley, int zoom) {
        boolean valid = false;
        if (tileSource != null) {
            int max = (1 << zoom);
            valid = !(tilex < 0 || tilex >= max || tiley < 0 || tiley >= max);
        }
        return valid;
    }
    
    @Override
    public TileLoadStrategy getStrategy() {
		return strategy;
	}

    @Override
	public final void setStrategy(TileLoadStrategy strategy) {
    	if(strategy == null){
    		throw new IllegalArgumentException("The strategy can not be null!");
    	}
    	if(!this.strategy.equals(strategy)){
    		this.strategy = strategy;
    		this.strategy.setCache(cache);
    	}
	}

    @Override
    public TileSource getTileSource() {
        return tileSource;
    }

    @Override
    public void setTileSource(TileSource tileSource) {
        this.tileSource = tileSource;
        cache.clear();
    }

    /**
     * Set the time difference when a image in the cache expires.
     *
     * @param expire difference as long
     */
    public void setExpire(long expire) {
        cache.setExpire(expire);
    }
}
