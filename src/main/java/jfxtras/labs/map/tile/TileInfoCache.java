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
