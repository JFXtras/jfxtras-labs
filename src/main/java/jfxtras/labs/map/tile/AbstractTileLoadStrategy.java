/**
 * AbstractTileLoadStrategy.java
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

import java.util.Objects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

/**
 * Abstract parent class for tile load commands.
 * @author Mario Schroeder
 *
 */
public abstract class AbstractTileLoadStrategy implements TileLoadStrategy{
	protected TileInfoCache cache;
	
	public void setCache(TileInfoCache cache){
		this.cache = cache;
	}
	
	Tile createTile(String location) {
		Tile tile = new Tile(location);
		loadImage(tile);
		return tile;
	}
	
	private void loadImage(Tile tile) {
		ChangeListener<Boolean> listener = new ImageLoadedListener(tile);
		tile.imageLoadedProperty().addListener(listener);
		tile.loadImage();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if(obj != null && getClass() == obj.getClass()){
			equal = Objects.equals(this.toString(), obj.toString());
		}
		return equal;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	private class ImageLoadedListener implements ChangeListener<Boolean> {
		
        private Tile tile;

        public ImageLoadedListener(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void changed(
            ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) {
            if (newVal.booleanValue()) {
                addImage(tile.getLocation(), tile.getImageView().getImage());
            }
        }

        private void addImage(String tileLocation, Image image) {

            long timeStamp = System.currentTimeMillis();
            TileInfo info = new TileInfo(timeStamp, image);
            cache.put(tileLocation, info);
        }
    }
}
