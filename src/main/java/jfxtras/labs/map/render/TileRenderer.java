package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.tile.Tile;
import jfxtras.labs.map.tile.TileCacheable;
import jfxtras.labs.map.tile.TileSource;

/**
 * Rendered for map tiles.
 * 
 * @author Mario Schroeder
 */
public class TileRenderer implements TileRenderable {

	/**
	 * stroke width
	 */
	private static final double WIDTH = 0.4;
	
	private static final int START = 0;

	private static final Point[] directions = { new Point(1, 0),
			new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	private final TileCacheable tileCache;

	private boolean monoChrome;

	private boolean tileGridVisible;
	
	private List<TileImage> tileImages;

	public TileRenderer(TileCacheable tileCache) {
		this.tileCache = tileCache;
		this.tileImages = Collections.emptyList();
	}

	@Override
	public int prepareTiles(MapControlable mapController) {

		tileImages = loadTiles(mapController);
		return tileImages.size();
	}

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 */
	private List<TileImage> loadTiles(MapControlable mapController) {

		List<TileImage> tiles = new ArrayList<>();

		TileSource tileSource = tileCache.getTileSource();

		int iMove = 2;
		int tilesize = tileSource.getTileSize();
		Point center = mapController.getCenter();

		int zoom = mapController.getZoom();

		int x_max = mapController.getMapWidth();
		int y_max = mapController.getMapHeight();

		int x_min = -tilesize, y_min = -tilesize;

		int posx = getInitialPositionX(tilesize, center, x_max);
		int posy = getInitialPositionY(tilesize, center, y_max);

		int tilex = (center.x / tilesize);
		int tiley = (center.y / tilesize);

		boolean added = true;
		int x = START;
		while (added) {
			added = false;
			for (int i = START; i < 4; i++) {
				if (i % 2 == START) {
					x++;
				}
				for (int j = START; j < x; j++) {
					if (x_min <= posx && posx <= x_max && y_min <= posy
							&& posy <= y_max) {
						// tile is visible
						Tile tile = tileCache.getTile(tilex, tiley, zoom);
						if (tile != null) {
							added = true;
							TileImage tileImage = new TileImage(tile.getImageView(), posx, posy);
							tileImage.setTileX(tilex);
							tileImage.setTileY(tiley);
							tiles.add(tileImage);
						}
					}
					Point next = directions[iMove];
					posx += next.x * tilesize;
					posy += next.y * tilesize;
					tilex += next.x;
					tiley += next.y;
				}
				iMove = (iMove + 1) % directions.length;
			}
		}

		Collections.sort(tiles);
		return tiles;
	}

	private int getInitialPositionY(int tilesize, Point center, int y_max) {
		int diff_top = (center.y % tilesize);
		return (y_max / 2) - diff_top;
	}

	private int getInitialPositionX(int tilesize, Point center, int x_max) {
		int diff_left = (center.x % tilesize);
		return (x_max / 2) - diff_left;
	}

	@Override
	public void doRender(MapControlable mapController) {
		renderTileImages(mapController, tileImages);
	}

	private void renderTileImages(MapControlable mapController,
			List<TileImage> tileImages) {
		
		int tilesize = tileCache.getTileSource().getTileSize();

		Group tilesGroup = mapController.getTilesGroup();
		
		renderTiles(tileImages, tilesize, tilesGroup);
	}
	
	private Point[] getBounds(List<TileImage> tileImages, int tilesize){
		TileImage min = Collections.min(tileImages);
		TileImage max = Collections.max(tileImages);
		Point[] bounds = new Point[2];
		bounds[0] = new Point(min.getPosX(), min.getPosY());
		bounds[1] = new Point(max.getPosX() + tilesize, max.getPosY() + tilesize);
		return bounds;
	}

	private void renderTiles(List<TileImage> tileImages, int tilesize,
			Group tilesGroup) {
		
		tilesGroup.getChildren().clear();
		
		for (TileImage tileImage : tileImages) {
			
			ImageView imageView = tileImage.getImageView();
			int posx = tileImage.getPosX();
			int posy = tileImage.getPosY();
			
			imageView.translateXProperty().set(posx);
			imageView.translateYProperty().set(posy);
			tilesGroup.getChildren().add(imageView);
			
			if (monoChrome) {
				setMonochromeEffect(imageView);
			}

			if (tileGridVisible) {
				tilesGroup.getChildren().add(createGrid(posx, posy, tilesize));
			}
		}
	}

	private void setMonochromeEffect(ImageView imageView) {

		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1);
		monochrome.setContrast(-0.3);
		monochrome.setBrightness(-0.3);
		imageView.setEffect(monochrome);
	}

	protected Path createGrid(int posx, int posy, int tilesize) {

		Path path = new Path();
		path.getElements().add(new MoveTo(posx, posy));
		path.getElements().add(new LineTo(posx + tilesize, posy));
		path.getElements().add(new LineTo(posx + tilesize, posy + tilesize));
		path.getElements().add(new LineTo(posx, posy + tilesize));
		path.getElements().add(new LineTo(posx, posy));
		path.setStrokeWidth(WIDTH);
		path.setStroke(Color.BLACK);

		return path;
	}

	public void setTileSource(TileSource tileSource) {
		tileCache.setTileSource(tileSource);
	}

	public TileSource getTileSource() {
		return tileCache.getTileSource();
	}

	public void setMonoChrome(boolean monoChrome) {
		this.monoChrome = monoChrome;
	}

	public void setTileGridVisible(boolean tileGridVisible) {
		this.tileGridVisible = tileGridVisible;
	}

	/**
	 * 
	 * This class combines the image view with the position.
	 * 
	 */
	private class TileImage implements Comparable<TileImage>{

		private ImageView imageView;

		private int posX, posY;
		
		private int tileX, tileY;

		TileImage(ImageView imageView, int posX, int posY) {
			super();
			this.imageView = imageView;
			this.posX = posX;
			this.posY = posY;
		}

		ImageView getImageView() {
			return imageView;
		}

		int getPosX() {
			return posX;
		}

		int getPosY() {
			return posY;
		}

		void setTileX(int tileX) {
			this.tileX = tileX;
		}

		void setTileY(int tileY) {
			this.tileY = tileY;
		}

		@Override
		public int compareTo(TileImage other) {
			return (this.tileX < other.tileX) ? -1 :
				    (this.tileX > other.tileX) ? +1 :
				    (this.tileY < other.tileY) ? -1 :
				    (this.tileY > other.tileY) ? +1 :
				    0;
		}

	}

}