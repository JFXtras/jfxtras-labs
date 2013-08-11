package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import static java.util.Collections.min;
import static java.util.Collections.max;
import static java.util.Collections.sort;
import static java.util.Collections.emptyList;

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

	private static final TileComparator TILE_COMPARATOR = new TileComparator();

	public TileRenderer(TileCacheable tileCache) {
		this.tileCache = tileCache;
		this.tileImages = emptyList();
	}

	@Override
	public int prepareTiles(MapControlable mapController) {

		tileImages = loadTiles(mapController);
		return tileImages.size();
	}

	@Override
	public Point[] getBounds() {
		Point[] bounds = new Point[0];
		if (!tileImages.isEmpty()) {
			TileImage min = min(tileImages, TILE_COMPARATOR);
			TileImage max = max(tileImages, TILE_COMPARATOR);

			int tilesize = getTileSize();
			bounds = new Point[2];
			bounds[0] = new Point(min.getPosX(), min.getPosY());
			bounds[1] = new Point(max.getPosX() + tilesize, max.getPosY()
					+ tilesize);
		}
		return bounds;
	}

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 */
	private List<TileImage> loadTiles(MapControlable mapController) {

		List<TileImage> tiles = new ArrayList<>();

		int tilesize = getTileSize();
		int iMove = 2;
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
							TileImage tileImage = new TileImage(
									tile.getImageView(), posx, posy);
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

		sort(tiles, TILE_COMPARATOR);
		return tiles;
	}

	private int getTileSize() {
		TileSource tileSource = tileCache.getTileSource();
		return tileSource.getTileSize();
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
	public void doRender(Group tilesGroup) {

		int tilesize = getTileSize();

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
	private class TileImage {

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
	}

	private static class TileComparator implements Comparator<TileImage> {

		@Override
		public int compare(TileImage left, TileImage right) {
			return (left.tileX < right.tileX) ? -1
					: (left.tileX > right.tileX) ? +1
							: (left.tileY < right.tileY) ? -1
									: (left.tileY > right.tileY) ? +1 : 0;
		}

	}

}