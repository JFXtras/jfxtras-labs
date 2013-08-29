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
