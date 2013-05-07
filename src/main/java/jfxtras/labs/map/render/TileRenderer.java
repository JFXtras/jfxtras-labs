package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.tile.Tile;
import jfxtras.labs.map.tile.TileCacheable;
import jfxtras.labs.map.tile.TileSource;

/**
 * Rendered for map tiles.
 * 
 * @author Mario Schroeder
 */
public class TileRenderer implements MapRenderable {

	private static final Point[] movePoints = { new Point(1, 0),
			new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	private static final int START = 0;

	private final TileCacheable tileCache;

	private boolean monoChrome;

	private boolean tileGridVisible;

	public TileRenderer(TileCacheable tileCache) {
		this.tileCache = tileCache;
	}

	@Override
	public boolean render(MapControlable mapController) {

		boolean changed = false;
		List<TileImage> tileImages = loadTiles(mapController);
		if (!tileImages.isEmpty()) {
			renderTileImages(mapController, tileImages);
			changed = true;
		}
		return changed;
	}

	/**
	 * Load the tiles in a spiral, starting from center of the map.
	 */
	private List<TileImage> loadTiles(MapControlable mapController) {

		List<TileImage> tileImages = new ArrayList<>();

		TileSource tileSource = tileCache.getTileSource();

		int iMove;
		int tilesize = tileSource.getTileSize();
		Point center = mapController.getCenter();

		int diff_left = (center.x % tilesize);
		int diff_right = tilesize - diff_left;
		int diff_top = (center.y % tilesize);
		int diff_bottom = tilesize - diff_top;

		boolean start_left = diff_left < diff_right;
		boolean start_top = diff_top < diff_bottom;

		if (start_top) {
			if (start_left) {
				iMove = 2;
			} else {
				iMove = 3;
			}
		} else {
			if (start_left) {
				iMove = 1;
			} else {
				iMove = START;
			}
		}

		int zoom = mapController.getZoom();

		int x_max = mapController.getMapWidth();
		int y_max = mapController.getMapHeight();

		int x_min = -tilesize, y_min = -tilesize;

		int posx = (x_max / 2) - diff_left;
		int posy = (y_max / 2) - diff_top;

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
							tileImages.add(tileImage);
						}
					}
					Point next = movePoints[iMove];
					posx += next.x * tilesize;
					posy += next.y * tilesize;
					tilex += next.x;
					tiley += next.y;
				}
				iMove = (iMove + 1) % movePoints.length;
			}
		}

		return tileImages;
	}

	private void renderTileImages(MapControlable mapController,
			List<TileImage> tileImages) {

		int tilesize = tileCache.getTileSource().getTileSize();

		Group tilesGroup = mapController.getTilesGroup();
		tilesGroup.getChildren().clear();

		for (TileImage tileImage : tileImages) {

			ImageView imageView = tileImage.getImageView();
			int posx = tileImage.getPosX();
			int posy = tileImage.getPosY();

			imageView.translateXProperty().set(posx);
			imageView.translateYProperty().set(posy);

			if (monoChrome) {
				setMonochromeEffect(imageView);
			}

			if (tileGridVisible) {
				tilesGroup.getChildren().add(createGrid(posx, posy, tilesize));
			}

			tilesGroup.getChildren().add(imageView);
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
		path.setStrokeWidth(1);
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

	}

}
