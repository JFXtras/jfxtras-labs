package jfxtras.labs.map.tile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

/**
 * Abstract parent class for tile load commands.
 * @author Mario Schroeder
 *
 */
public abstract class TileLoadCommand {
	protected final TileInfoCache cache;
	
	TileLoadCommand(TileInfoCache cache) {
		this.cache = cache;
	}
	
	abstract Tile execute(String location);
	
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
