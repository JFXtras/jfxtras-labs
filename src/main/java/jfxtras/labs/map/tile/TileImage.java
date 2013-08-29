package jfxtras.labs.map.tile;

import javafx.scene.image.ImageView;

/**
 * 
 * This class combines the image view with the position.
 * @author Mario Schroeder
 */
public class TileImage {

	private ImageView imageView;

	private int posX, posY;

	private int tileX, tileY;

	public TileImage(ImageView imageView, int posX, int posY) {
		super();
		this.imageView = imageView;
		this.posX = posX;
		this.posY = posY;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	
}