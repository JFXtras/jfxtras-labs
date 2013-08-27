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
