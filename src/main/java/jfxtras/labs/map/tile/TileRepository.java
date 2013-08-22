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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

/**
 * A repository for the map tiles.
 *
 * @author jsmith.carlsbad@gmail.com
 * @author Mario Schroeder
 *
 */
public class TileRepository implements TilesProvideable{

    private TileSource tileSource;

    private TileInfoCache cache = new TileInfoCache();

    public TileRepository(TileSource source) {
        tileSource = source;
        cache = new TileInfoCache();
    }

    @Override
    public Tile findTile(int tilex, int tiley, int zoom) {
        Tile tile = null;

        if (isValid(tilex, tiley, zoom)) {

            cleanupCache();

            String location = tileSource.getTileUrl(zoom, tilex, tiley);
            TileInfo info = cache.get(location);

            if (info != null) {
                tile = new Tile(location, info.getImage());
            } else {
                tile = load(location);
            }
        }

        return tile;
    }

	private Tile load(String location) {
		Tile tile = new Tile(location);
		ChangeListener<Boolean> listener = new ImageLoadedListener(location, tile);
		tile.imageLoadedProperty().addListener(listener);
		tile.loadImage();
		return tile;
	}

    /**
     * Checks the expiration of images in the cache, and removes them eventually.
     *
     * @param location
     */
    private void cleanupCache() {

        cache.cleanup();
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

    private class ImageLoadedListener implements ChangeListener<Boolean> {

        private String location;

        private Tile tile;

        public ImageLoadedListener(String location, Tile tile) {
            this.location = location;
            this.tile = tile;
        }

        @Override
        public void changed(
            ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
            if (newVal.booleanValue()) {
                addImage(location, tile.getImageView().getImage());
            }
        }

        private void addImage(String tileLocation, Image image) {

            long timeStamp = System.currentTimeMillis();
            TileInfo info = new TileInfo(timeStamp, image);
            cache.put(tileLocation, info);
        }
    }
}
