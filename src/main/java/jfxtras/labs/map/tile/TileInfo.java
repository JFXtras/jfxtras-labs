package jfxtras.labs.map.tile;

import javafx.scene.image.Image;

/**
 * This class keeps the timestamp and image together.
 * 
 * @author Mario Schroeder
 * 
 */
class TileInfo {

	private long timeStamp;

	private Image image;

	TileInfo(long timeStamp, Image image) {
		this.timeStamp = timeStamp;
		this.image = image;
	}

    /**
     * Returns the time stampe when the image was loaded.
     * @return time in milli seconds
     */
	long getTimeStamp() {
		return timeStamp;
	}

    /**
     * Returns the image for the tile.
     * @return the loaded image
     */
	Image getImage() {
		return image;
	}

}
